package Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Database class contains map of tables and provides functionality to create, retrieve and 
// delete tables
public class Database {
    private String databaseName;

    private Map<String, Table> tables = new HashMap<String, Table>();

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    // FIXME creating table with no attributes
    public boolean createTable(String tableName, List<String> attributes) {
        // Check that table doesn't exist already. If it does, return false.
        if (tables.containsKey(tableName)) {
            return false;
        }

        tables.put(tableName, new Table(tableName, attributes));
        return true;
    }

    // Returns table, or null if it doesn't exist
    public Table getTable(String tableName) {
        return tables.get(tableName);
    }

    public boolean dropTable(String tableName) {
        // remove() returns null if there was no mapping. As such, return false to
        // indicate table does not exist
        if (tables.remove(tableName) == null) {
            return false;
        }

        return true;
    }
}