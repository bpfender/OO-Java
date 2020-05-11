package Expression;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.function.IntBinaryOperator;

import ConditionTree.Node;
import Database.*;

// Could potentially have hashmap associating name with file path?
// FIXME currently non-persistent
//FIXME clearing values properly
public class Context {
    protected enum Mode {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN
    };

    // Active table and database are loaded based on context of query
    private Database activeDatabase;
    private Table activeTable;

    // Active attributes and indicies define which data values to work with
    private ArrayList<String> activeAttributes;
    private ArrayList<Integer> activeIndices = new ArrayList<>();

    private HashMap<String, String> nameValuePairsUpdate;

    private Mode mode = Mode.SELECT;

    private ArrayList<Table> joinTables = new ArrayList<>();

    private Map<String, Database> databases = new HashMap<>();

    Table temp;

    // TODO proper execute strategy

    public Context() {
    }

    // TODO this will need to be moved to a file handler for serialization
    public void createDatabase(String databaseName) throws Exception {
        if (databases.containsKey(databaseName)) {
            throw new Exception("ERROR: Database " + databaseName + " already exists.");
        }

        databases.put(databaseName, new Database(databaseName));
    }

    public void createTable(String tableName, List<String> attributeList) throws Exception {
        checkIfActiveDatabaseSet();
        if (!activeDatabase.createTable(tableName, attributeList)) {
            throw new Exception("ERROR: Table " + tableName + " already exists.");
        }
    }

    // TODO this will need to be moved to a file handler for serialization
    public void useDatabase(String databaseName) throws Exception {
        if ((activeDatabase = databases.get(databaseName)) == null) {
            throw new Exception("ERROR: Unknown database " + databaseName + ".");
        }
    }

    // TODO this will need to be moved to a file handler for serialization
    public void dropDatabase(String databaseName) throws Exception {
        Database tmp = databases.remove(databaseName);

        if (tmp == null) {
            throw new Exception("ERROR: Unknown database " + databaseName + ".");
        }

        // TODO is this the neatest way of doing it?
        // If the dropped database was in use, make sure that reference to it is
        // removed.
        if (tmp == activeDatabase) {
            activeDatabase = null;
            activeTable = null;
        }
    }

    public void dropTable(String tableName) throws Exception {
        checkIfActiveDatabaseSet();
        if (!activeDatabase.dropTable(tableName)) {
            throw new Exception("ERROR: Unknown table " + tableName + ".");
        }

        // TODO is this the most elegant way of achieving this?
        // Remove all references to make sure it gets cleared from memory
        activeTable = null;
    }

    public void setTable(String tableName) throws Exception {
        checkIfActiveDatabaseSet();

        activeTable = activeDatabase.getTable(tableName);
        if (activeTable == null) {
            throw new Exception("ERROR: Unknown table " + tableName + ".");
        }
    }

    // TODO would be nice to have diagnostics on reserved or exists keyword
    public void add(String attribute) throws Exception {
        activeTable.addAttribute(attribute);
    }

    public void drop(String attribute) throws Exception {
        activeTable.dropAttribute(attribute);
    }

    // TODO would be nice to have better diagnostics on failure
    public void insert(List<String> values) throws Exception {
        if (!activeTable.insertValues(values)) {
            throw new Exception("ERROR: Values do not match table attributes.");
        }
    }

    // TODO how to handle wildcard?
    public void select(ArrayList<String> attributes) throws Exception {
        checkIfActiveDatabaseSet();
        activeAttributes = attributes;
    }

    // TOOD would prefer to use overloading for this stuff i.e. wildcard
    // specification. Not sure passing null around is nice
    public void validateSelectAttributes() throws Exception {
        if (activeAttributes == null) {
            activeAttributes = activeTable.getAttributeList();
        } else {
            for (String attribute : activeAttributes) {
                if (!activeTable.checkAttributeExists(attribute)) {
                    throw new Exception("ERROR: Invalid attribute " + attribute + " specified.");
                }
            }
        }
    }

    // TODO not a fan of the loop for no condition tree
    public void setFilter(Node conditionTree) throws Exception {
        activeIndices.clear();

        if (conditionTree == null) {
            for (int i = 0; i < activeTable.getTableSize(); i++) {
                activeIndices.add(i);
            }

            // TODO invalid attribute handling in condition tree is interesting...
        } else {
            activeIndices = conditionTree.returnIndices(activeTable);
            if (activeIndices == null) {
                throw new Exception("ERROR: Invalid attribute specified in WHERE clause.");
            }
        }
    }

    // FIXME this will have to be renamed to execute - possible use of strategy
    // pattern here
    public String execute() throws Exception {
        System.out.println("CONTEXT MODE:" + mode);
        switch (mode) {
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

    // FIXME no error handling at the moment
    public String tablesToJoin(String table1, String table2) {
        joinTables.add(activeTable);
        joinTables.add(activeTable);

        return null;
    }

    public String join() {
        String searchString = new String();
        for (Integer i : activeIndices) {
            searchString += temp.getColumn("id").getValue(i) + " ";
            for (String attrib : activeAttributes) {
                System.out.println(attrib);
                searchString += temp.getColumn(attrib).getColumnValues().get(i) + " ";

            }
            searchString += "\n";
        }

        return searchString;
    }

    public String setJoinOn(String column1, String column2) {
        System.out.println(joinTables.get(0).getColumn(column1));

        ArrayList<String> col1 = joinTables.get(0).getColumn(column1).getColumnValues();
        ArrayList<String> col2 = joinTables.get(1).getColumn(column2).getColumnValues();

        ArrayList<Integer> indices1 = new ArrayList<>();
        ArrayList<Integer> indices2 = new ArrayList<>();
        ArrayList<SimpleEntry<Integer, Integer>> indices = new ArrayList<>();

        int i = 0, j = 0;
        for (String val1 : col1) {
            for (String val2 : col2) {
                if (val1.equals(val2)) {
                    indices1.add(i);
                    indices2.add(j);
                    indices.add(new SimpleEntry<Integer, Integer>(i, j));
                }

                j++;
            }
            i++;
        }

        List<String> tempAttribs = new ArrayList<>();

        for (String attrib : joinTables.get(0).getAttributes()) {
            tempAttribs.add(joinTables.get(0).getTableName() + "." + attrib);
        }
        for (String attrib : joinTables.get(1).getAttributes()) {
            tempAttribs.add(joinTables.get(1).getTableName() + "." + attrib);
        }

        temp = new Table("temp", tempAttribs);
        List<String> valueList = new ArrayList<>();
        for (SimpleEntry<Integer, Integer> entry : indices) {
            valueList.clear();
            for (Column col : joinTables.get(0).getColumns()) {
                valueList.add(col.getColumnValues().get(entry.getKey()));
            }

            for (Column col : joinTables.get(1).getColumns()) {
                valueList.add(col.getColumnValues().get(entry.getValue()));
            }
            temp.insertValues(valueList);
        }

        return null;

    }

    // TODO no handling of wildcards and id
    public void setNameValuePairs(HashMap<String, String> nameValuePairs) throws Exception {
        for (String attribute : nameValuePairs.keySet()) {
            if (!activeTable.checkAttributeExists(attribute)) {
                throw new Exception("ERROR: Unknown attribute " + attribute + " specified.");
            }
        }

        this.nameValuePairsUpdate = nameValuePairs;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
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

    public String delete() throws Exception {
        System.out.println(activeIndices);

        // TODO not totally happy with this backwards loop
        for (int i = activeIndices.size() - 1; i >= 0; i--) {
            activeTable.deleteRow(i);

        }
        return "OK";
    }

    private void checkIfActiveDatabaseSet() throws Exception {
        if (activeDatabase == null) {
            throw new Exception("ERROR: No database specified.");
        }
    }

}