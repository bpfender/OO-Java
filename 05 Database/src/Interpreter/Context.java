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

//FIXME clearing values properly
public class Context {
    // Active table and database are loaded based on context of query
    private Database activeDatabase;
    private Table activeTable;

    // Active attributes and indicies define which data values to work with
    private ArrayList<String> activeAttributes;
    private ArrayList<Integer> activeIndices;

    private HashMap<String, String> nameValuePairsUpdate;

    private ArrayList<Table> joinTables = new ArrayList<>();

    // TODO proper execute strategy
    // Mode is used to determine what action the execute command should take
    protected enum Mode {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN
    };

    private Mode commandMode;

    // Calls the database handler to retrieve database if it exists
    public void setActiveDatabase(String databaseName) throws RuntimeException {
        activeDatabase = DatabaseHandler.getInstance().useDatabase(databaseName);
    }

    public void createDatabase(String databaseName) throws RuntimeException {
        DatabaseHandler.getInstance().createDatabase(databaseName);
    }

    public void createTable(String tableName, List<String> attributeList) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeDatabase.createTable(tableName, attributeList);
    }

    public void dropDatabase(String databaseName) throws RuntimeException {
        Database tmp = DatabaseHandler.getInstance().dropDatabase(databaseName);

        // TODO is this the neatest way of doing it?
        // If the dropped database was in use, make sure that reference to it is
        // removed.
        if (tmp == activeDatabase) {
            activeDatabase = null;
            activeTable = null;
        }
    }

    public void dropTable(String tableName) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeDatabase.dropTable(tableName);
        // TODO is this the most elegant way of achieving this?
        // Remove all references to make sure it gets cleared from memory
        activeTable = null;
    }

    public void setTable(String tableName) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeTable = activeDatabase.getTable(tableName);
    }

    // TODO would be nice to have diagnostics on reserved or exists keyword
    public void add(String attribute) throws RuntimeException {
        activeTable.addAttribute(attribute);
    }

    public void drop(String attribute) throws RuntimeException {
        activeTable.dropAttribute(attribute);
    }

    // TODO would be nice to have better diagnostics on failure
    public void insert(List<String> values) throws RuntimeException {
        if (!activeTable.insertValues(values)) {
            throw new RuntimeException("ERROR: Values do not match table attributes.");
        }
    }

    // TODO how to handle wildcard?
    public void select(ArrayList<String> attributes) throws RuntimeException {
        checkIfActiveDatabaseSet();
        activeAttributes = attributes;
    }

    // TOOD would prefer to use overloading for this stuff i.e. wildcard
    // specification. Not sure passing null around is nice
    public void validateSelectAttributes() throws RuntimeException {
        if (activeAttributes == null) {
            activeAttributes = activeTable.getAttributeList();
        } else {
            for (String attribute : activeAttributes) {
                if (!activeTable.checkAttributeExists(attribute)) {
                    throw new RuntimeException("ERROR: Invalid attribute " + attribute + " specified.");
                }
            }
        }
    }

    // TODO not a fan of the loop for no condition tree
    public void setFilter(Node conditionTree) throws RuntimeException {
        activeIndices.clear();

        if (conditionTree == null) {
            for (int i = 0; i < activeTable.getTableSize(); i++) {
                activeIndices.add(i);
            }

            // TODO invalid attribute handling in condition tree is interesting...
        } else {
            activeIndices = conditionTree.returnIndices(activeTable);
            if (activeIndices == null) {
                throw new RuntimeException("ERROR: Invalid attribute specified in WHERE clause.");
            }
        }
    }

    // FIXME this will have to be renamed to execute - possible use of strategy
    // pattern here
    public String execute() throws RuntimeException {
        System.out.println("CONTEXT MODE:" + commandMode);
        switch (commandMode) {
            case SELECT:
                return returnResult();
            case UPDATE:
                return update();
            case DELETE:
                return delete();
            case JOIN:
                return join();
            default:
                return "CURRENTLY A RANDOM OK";
        }
    }

    // TODO Streamify?
    public String returnResult() {
        return generateResultHeader() + generateDataView();
    }

    private String generateResultHeader() {
        String header = new String();
        for (String attribute : activeAttributes) {
            header += attribute + " ";
        }
        header += "\n";

        return header;
    }

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

    public void setJoinTables(String table1, String table2) throws RuntimeException {
        joinTables.clear();
        joinTables.add(activeDatabase.getTable(table1));
        joinTables.add(activeDatabase.getTable(table2));
    }

    // TODO join doesn't currently remove ids
    public String join() {
        return generateResultHeader() + generateDataView();
    }

    // TODO currently no error checknig
    public String setJoinOn(String column1, String column2) throws RuntimeException {
        System.out.println("setJoinOn()");

        ArrayList<String> col1 = joinTables.get(0).getColumn(column1).getColumnValues();
        ArrayList<String> col2 = joinTables.get(1).getColumn(column2).getColumnValues();

        ArrayList<Integer> indices1 = new ArrayList<>();
        ArrayList<Integer> indices2 = new ArrayList<>();
        ArrayList<SimpleEntry<Integer, Integer>> indices = new ArrayList<>();

        System.out.println("COL1 " + col1);
        System.out.println("COL2 " + col2);

        int i = 0, j = 0;
        for (String val1 : col1) {
            j = 0;
            System.out.print("Val1: " + val1);
            for (String val2 : col2) {
                System.out.println(" Val2: " + val2);
                if (val1.equals(val2)) {
                    System.out.println("JOIN MATCH i: " + i + " j: " + j);
                    indices1.add(i);
                    indices2.add(j);
                    indices.add(new SimpleEntry<Integer, Integer>(i, j));
                }

                j++;
            }
            i++;
        }

        System.out.println(indices);

        ArrayList<String> tempAttribs = new ArrayList<>();

        for (String attrib : joinTables.get(0).getAttributes()) {
            tempAttribs.add(joinTables.get(0).getTableName() + "." + attrib);
        }
        for (String attrib : joinTables.get(1).getAttributes()) {
            tempAttribs.add(joinTables.get(1).getTableName() + "." + attrib);
        }

        System.out.println(tempAttribs);

        activeTable = new Table("temp", tempAttribs);
        List<String> valueList = new ArrayList<>();

        for (SimpleEntry<Integer, Integer> entry : indices) {
            valueList.clear();
            for (Column col : joinTables.get(0).getColumns()) {
                valueList.add(col.getColumnValues().get(entry.getKey()));
            }

            for (Column col : joinTables.get(1).getColumns()) {
                valueList.add(col.getColumnValues().get(entry.getValue()));
            }
            System.out.println("VALUE LIST: " + valueList);

            activeTable.insertValues(valueList);
        }

        // TODO a bit messy at the moment
        setFilter(null);
        select(null);
        validateSelectAttributes();

        return null;

    }

    // TODO no handling of wildcards and id
    public void setNameValuePairs(HashMap<String, String> nameValuePairs) throws RuntimeException {
        for (String attribute : nameValuePairs.keySet()) {
            if (!activeTable.checkAttributeExists(attribute)) {
                throw new RuntimeException("ERROR: Unknown attribute " + attribute + " specified.");
            }
        }

        this.nameValuePairsUpdate = nameValuePairs;
    }

    public void setMode(Mode commandMode) {
        this.commandMode = commandMode;
    }

    public String update() {
        for (String key : nameValuePairsUpdate.keySet()) {
            String value = nameValuePairsUpdate.get(key);
            Column col = activeTable.getColumn(key);

            for (Integer i : activeIndices) {
                col.updateValue(i, value);
            }
        }

        return "OK";
    }

    public String delete() throws RuntimeException {
        System.out.println(activeIndices);

        // TODO not totally happy with this backwards loop
        for (int i = activeIndices.size() - 1; i >= 0; i--) {
            // TODO can this be cleaned up with finding active indices?
            activeTable.deleteRow(activeIndices.get(i));

        }
        return "OK";
    }

    private void checkIfActiveDatabaseSet() throws RuntimeException {
        if (activeDatabase == null) {
            throw new RuntimeException("ERROR: No database specified.");
        }
    }

}