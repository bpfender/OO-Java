package Interpreter;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ConditionTree.Node;
import Database.Column;
import Database.Database;
import Database.DatabaseHandler;
import Database.Table;

// Context provides functionality to execute queries. Any errors are handled by 
// throwing exceptions with the relevant error messages
public class Context {
    // Active table and database are loaded based on context of query
    private Database activeDatabase;
    private Table activeTable;

    // Active attributes and indicies define which data values to work with
    private ArrayList<String> activeAttributes;
    private ArrayList<Integer> activeIndices;

    private HashMap<String, String> nameValuePairsUpdate;

    private ArrayList<Table> joinTables = new ArrayList<>();

    // This could probabyl also have been implemented as a Strategy pattern, but I
    // ran out of timwe
    // Mode is used to determine what action the execute command should take
    protected enum Mode {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN
    };

    private Mode commandMode;

    // Calls the database handler to retrieve database if it exists
    public void setActiveDatabase(String databaseName) throws RuntimeException {
        activeDatabase = DatabaseHandler.getInstance().useDatabase(databaseName);
    }

    // Calls the database handler to create the database if it exists
    public void createDatabase(String databaseName) throws RuntimeException {
        DatabaseHandler.getInstance().createDatabase(databaseName);
    }

    // First checks that activeDatabase is set. If it is create table if it doesn't
    // yet exist
    public void createTable(String tableName, List<String> attributeList) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeDatabase.createTable(tableName, attributeList);
    }

    // Drops database if it exists. If is currently set as context's activeDB, then
    // both it and the activeTable are set to null
    public void dropDatabase(String databaseName) throws RuntimeException {
        Database tmp = DatabaseHandler.getInstance().dropDatabase(databaseName);

        if (tmp == activeDatabase) {
            activeDatabase = null;
            activeTable = null;
        }
    }

    // Drops table if activeDB is set and table exists.
    public void dropTable(String tableName) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeDatabase.dropTable(tableName);
    }

    // Sets activeTable if it exists in current database. This will always be called
    // where table query is involved, meaning that other functions don't have to
    // validate whether activeTable is set if it should be
    public void setActiveTable(String tableName) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeTable = activeDatabase.getTable(tableName);
    }

    public void addActiveTableAttribute(String attribute) throws RuntimeException {
        activeTable.addAttribute(attribute);
    }

    public void dropActiveTableAttribute(String attribute) throws RuntimeException {
        activeTable.dropAttribute(attribute);
    }

    public void insertIntoActiveTable(List<String> values) throws RuntimeException {
        activeTable.insertValues(values);
    }

    // Select attributes will receive null for a wildcard or a list of attributes.
    // Formatting this input is handled by the parser and verifying the attributes
    // set is done by validateSelectAttributes(). This function just loads the
    // attributes into the context.
    public void setSelectAttributes(ArrayList<String> attributes) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeAttributes = attributes;
    }

    // Validates whether specified attributes match the active table. If the input
    // was a wildcard (null value for activeAttributes) activeAttributes is
    // populated with all attributes
    public void validateSelectAttributes() throws RuntimeException {
        if (activeAttributes == null) {
            activeAttributes = activeTable.getAttributeList();
        } else {
            for (String attribute : activeAttributes) {
                activeTable.checkAttributeExists(attribute);
            }
        }
    }

    // Set filter is passed a conditionTree (built by th3e parser) which defines the
    // WHERE filter. The values can only be validated at this point after the
    // activeTable has been set. If there is no filter, the function is passed a
    // null, indicating that all records should be returned
    public void setFilter(Node conditionTree) throws RuntimeException {
        if (conditionTree == null) {
            activeIndices = new ArrayList<>();
            for (int i = 0; i < activeTable.getTableSize(); i++) {
                activeIndices.add(i);
            }
        } else {
            activeIndices = conditionTree.returnIndices(activeTable);
        }
    }

    // Validates the update values to ensure they are specifying valid attributes.
    // Excludes id, because this shouldn't be modified
    public void setNameValuePairs(HashMap<String, String> nameValuePairs) throws RuntimeException {
        for (String attribute : nameValuePairs.keySet()) {
            if (attribute.equals("id")) {
                throw new RuntimeException("ERROR: Cannot modify reserved attribute id.");
            }
            activeTable.checkAttributeExists(attribute);
        }

        this.nameValuePairsUpdate = nameValuePairs;
    }

    // Commands that filter and select things from tables share the setFilter(),
    // setSelectAttributes() functions etc. In order to execture the right action,
    // the first expression in the chain determines the mode, before execute is
    // called at the end of the chain. Depending on the mode set, execute does
    // slightly different things
    public String execute() throws RuntimeException {
        switch (commandMode) {
            case SELECT:
            case JOIN:
                return executeGenerateView();
            case UPDATE:
                return executeUpdate();
            case DELETE:
                return executeDelete();
            default:
                return "OK";
        }
    }

    // Validates that selected attributes corresoond to the activeTable set and
    // returns view generated. This is called by both join and select as both return
    // table views.
    public String executeGenerateView() {
        validateSelectAttributes();
        return generateAttributesView() + generateDataView();
    }

    // With everything having been validated, simply executes the update to the
    // table.
    public String executeUpdate() {
        for (String attribute : nameValuePairsUpdate.keySet()) {
            String value = nameValuePairsUpdate.get(attribute);

            Column col = activeTable.getColumn(attribute);

            for (Integer i : activeIndices) {
                col.updateValue(i, value);
            }
        }

        return "OK";
    }

    // Executes delete as everything has been validated
    public String executeDelete() throws RuntimeException {
        // Loops backwards to avoid any shifting of indices issues
        for (int i = activeIndices.size() - 1; i >= 0; i--) {
            activeTable.deleteRow(activeIndices.get(i));
        }
        return "OK";
    }

    // Generates string of attributes in table view
    private String generateAttributesView() {
        String header = new String();
        for (String attribute : activeAttributes) {
            header += attribute + " ";
        }
        header += "\n";

        return header;
    }

    // Generate string of selected data in view
    private String generateDataView() {
        String data = new String();
        for (Integer i : activeIndices) {
            for (String attribute : activeAttributes) {
                data += activeTable.getColumn(attribute).getValue(i) + " ";
            }
            data += "\n";
        }
        return data;
    }

    // Loads the two tables to be joined into an array to be worked on during
    // further processing.
    public void setJoinTables(String tableName1, String tableName2) throws RuntimeException {
        joinTables.clear();
        joinTables.add(activeDatabase.getTable(tableName1));
        joinTables.add(activeDatabase.getTable(tableName2));
    }

    // setJoinOn uses the two join columns from the join tables to determine the
    // indices that should be extracted from each table. Any errors are thrown via
    // exceptions automatically. A temporary table is created in context's
    // activeTable, where the join table is generated. Filters and attributes are
    // set so that the whole table is returned at the end of the query.
    public void generateJoinTable(String attributeTable1, String attributeTable2) throws RuntimeException {
        ArrayList<String> joinAttributes = generateJoinTableAttributes();
        ArrayList<SimpleEntry<Integer, Integer>> indices = calculateJoinIndices(attributeTable1, attributeTable2);

        activeTable = new Table("join", joinAttributes);

        for (SimpleEntry<Integer, Integer> entry : indices) {
            List<String> valueList = new ArrayList<>();

            for (String attribute : joinTables.get(0).getAttributeListWithoutId()) {
                valueList.add(joinTables.get(0).getColumn(attribute).getValue(entry.getKey()));
            }

            for (String attribute : joinTables.get(1).getAttributeListWithoutId()) {
                valueList.add(joinTables.get(1).getColumn(attribute).getValue(entry.getValue()));
            }
            activeTable.insertValues(valueList);

        }

        setFilter(null);
        setSelectAttributes(null);
    }

    // Generates a mapping of each row in table1 that matches a row in table 2
    private ArrayList<SimpleEntry<Integer, Integer>> calculateJoinIndices(String attributeTable1,
            String attributeTable2) throws RuntimeException {

        Table table1 = joinTables.get(0);
        Table table2 = joinTables.get(1);
        Column column1 = table1.getColumn(attributeTable1);
        Column column2 = table2.getColumn(attributeTable2);

        ArrayList<SimpleEntry<Integer, Integer>> indices = new ArrayList<>();

        for (int i = 0; i < table1.getTableSize(); i++) {
            for (int j = 0; j < table2.getTableSize(); j++) {
                if (column1.getValue(i).equals(column2.getValue(j))) {
                    indices.add(new SimpleEntry<>(i, j));
                }
            }
        }

        return indices;
    }

    // Generates attributes list for join table
    private ArrayList<String> generateJoinTableAttributes() {
        ArrayList<String> joinAttributes;
        joinAttributes = getConcatenatedTableAttributes(joinTables.get(0));
        joinAttributes.addAll(getConcatenatedTableAttributes(joinTables.get(1)));

        return joinAttributes;
    }

    // Concatenates attributes into form <tablename>.<attributename>. Excludes id
    // from concatenation as new id is generated for join table
    private ArrayList<String> getConcatenatedTableAttributes(Table table) {
        ArrayList<String> tempAttrib = new ArrayList<>();

        for (String attrib : table.getAttributeListWithoutId()) {
            tempAttrib.add(table.getTableName() + "." + attrib);
        }
        return tempAttrib;
    }

    public void setMode(Mode commandMode) {
        this.commandMode = commandMode;
    }

    private void checkIfActiveDatabaseSet() throws RuntimeException {
        if (activeDatabase == null) {
            throw new RuntimeException("ERROR: No database specified.");
        }
    }

}