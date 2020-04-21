package Expression;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

import Database.*;

// Could potentially have hashmap associating name with file path?
// FIXME currently non-persistent

public class Context {
    private Database activeDatabase;

    private Table activeTable;

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

}