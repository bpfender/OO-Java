package Expression;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

import ConditionTree.Node;
import Database.*;

// Could potentially have hashmap associating name with file path?
// FIXME currently non-persistent

public class Context {
    public enum Mode {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN
    };

    private Database activeDatabase;

    private Table activeTable;
    private ArrayList<String> activeAttributes;
    private ArrayList<Integer> activeIndices;
    private HashMap<String, String> updateValues;
    private Mode mode;

    private Map<String, Database> databases = new HashMap<>();

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
        return false;
    }

    public boolean selectQuery() {
        Collection<String> tableAttributes = activeTable.getAttributes();

        if (!(activeAttributes.size() == 1 && activeAttributes.contains("*"))) {
            for (String attribute : activeAttributes) {
                if (!tableAttributes.contains(attribute)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean setFilter(Node conditionTree) {
        if ((activeIndices = conditionTree.returnIndices(activeTable)) == null) {
            return false;
        }
        return true;
    }

    // FIXME this will have to be renamed to execute - possible use of strategy
    // pattern here
    public String search() {

        switch (mode) {
            case UPDATE:
                return update();
            case DELETE:
                return delete();
            default:
                break;
        }

        String searchString = new String();

        for (Integer i : activeIndices) {
            searchString += activeTable.getId(i);
            for (String attrib : activeAttributes) {
                searchString += activeTable.getColumn(attrib).getColumnValues().get(i);
            }
        }

        return searchString;
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
        for (Integer i : activeIndices) {
            activeTable.deleteId(i);

            for (Column col : activeTable.getColumns()) {
                col.deleteValue(i);
            }
        }

        return "OK";
    }

}