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
    public enum Mode {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN
    };

    private Database activeDatabase;
    private Table activeTable;
    private ArrayList<String> activeAttributes;
    private ArrayList<Integer> activeIndices = new ArrayList<>();
    private HashMap<String, String> updateValues;
    private Mode mode = Mode.SELECT;

    private ArrayList<Table> joinTables = new ArrayList<>();

    private Map<String, Database> databases = new HashMap<>();

    Table temp;

    public Context() {
    }

    public boolean createDatabase(String databaseName) {
        // Check that database exists with that name
        // if not create database file
        if (databases.containsKey(databaseName)) {
            return false;
        }

        databases.put(databaseName, new Database(databaseName));
        return true;
    }

    // TODO these returns are ungainly. Try catch block?
    public int createTable(String tableName, List<String> attributeList) {
        if (activeDatabase == null) {
            return -1;
        }

        if (activeDatabase.createTable(tableName, attributeList)) {
            return 0;
        }

        return -2;
    }

    public boolean useDatabase(String databaseName) {
        // find database file corresponding to name and load.
        // return true if successful, false if not
        if ((activeDatabase = databases.get(databaseName)) != null) {
            return true;
        }

        return false;
    }

    public boolean dropDatabase(String databaseName) {
        Database tmp = databases.remove(databaseName);

        if (tmp == null) {
            return false;
        }

        // QUESTION does this remove all references to database
        if (tmp == activeDatabase) {
            activeDatabase = null;
        }

        return true;
    }

    public int dropTable(String tableName) {
        if (activeDatabase == null) {
            return -1;
        }

        if (activeDatabase.dropTable(tableName)) {
            return 0;
        }

        return -2;
    }

    public int setTable(String tableName) {
        if (activeDatabase == null) {
            return -1;
        }

        Table tmp = activeDatabase.fromTable(tableName);

        if (tmp == null) {
            return -2;
        }

        activeTable = tmp;
        return 0;
        // QUESTION resetting activeTable after query is over?
    }

    // QUESTION better to have overloaded execute method?
    public boolean add(String attribute) {
        return activeTable.addAttribute(attribute);
    }

    public boolean drop(String attribute) {
        return activeTable.dropAttribute(attribute);
    }

    public boolean insert(List<String> data) {
        return activeTable.insertValues(data);
    }

    public boolean select(ArrayList<String> attributes) {
        if (activeDatabase == null) {
            return false;
        }

        activeAttributes = attributes;
        return true;
    }

    public boolean selectQuery() {
        Collection<String> tableAttributes = activeTable.getAttributes();

        if (!(activeAttributes.size() == 1 && activeAttributes.contains("*"))) {
            for (String attribute : activeAttributes) {
                if (!tableAttributes.contains(attribute)) {
                    return false;
                }
            }
        } else {
            activeAttributes.clear();
            for (String attribute : tableAttributes) {
                activeAttributes.add(attribute);
            }
        }

        return true;
    }

    public boolean setFilter(Node conditionTree) {

        if (conditionTree == null) {
            activeIndices.clear();
            for (int i = 0; i < activeTable.ids.size(); i++) {
                activeIndices.add(i);
            }
            return true;

        }

        if ((activeIndices = conditionTree.returnIndices(activeTable)) == null) {
            return false;
        }
        // System.out.println(conditionTree.returnIndices(activeTable));
        return true;
    }

    // FIXME this will have to be renamed to execute - possible use of strategy
    // pattern here
    public String search() {
        System.out.println("CONTEXT MODE:" + mode);
        switch (mode) {
            case UPDATE:
                return update();
            case DELETE:
                return delete();
            case JOIN:
                return join();
            default:
                break;
        }

        String searchString = new String();
        for (Integer i : activeIndices) {
            searchString += activeTable.getId(i) + " ";
            for (String attrib : activeAttributes) {
                System.out.println(attrib);
                searchString += activeTable.getColumn(attrib).getColumnValues().get(i) + " ";

            }
            searchString += "\n";
        }

        return searchString;
    }

    // FIXME no error handling at the moment
    public String tablesToJoin(String table1, String table2) {
        System.out.println(setTable(table1));
        joinTables.add(activeTable);
        System.out.println(setTable(table2));
        joinTables.add(activeTable);

        return null;
    }

    public String join() {
        String searchString = new String();
        for (Integer i : activeIndices) {
            searchString += temp.getId(i) + " ";
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

    public boolean setNameValuePairs(HashMap<String, String> values) {
        this.updateValues = values;

        for (String key : values.keySet()) {
            Collection<String> attributes = activeTable.getAttributes();
            if (!attributes.contains(key)) {
                return false;
            }
        }
        return true;

    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String update() {
        for (String key : updateValues.keySet()) {
            String value = updateValues.get(key);
            Column col = activeTable.getColumn(key);

            for (Integer i : activeIndices) {
                col.updateValue(i, value);
            }
        }

        return "OK";
    }

    public String delete() {
        System.out.println(activeIndices);

        for (int i = activeIndices.size() - 1; i >= 0; i--) {
            System.out.println(activeTable.ids);

            int index = activeIndices.get(i);

            activeTable.ids.remove(index); // something funny going on here... check remove ids function

            for (Column col : activeTable.getColumns()) {
                col.deleteValue(index);
            }
        }
        return "OK";
    }

}