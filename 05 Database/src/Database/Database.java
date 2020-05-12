package Database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Database class contains map of tables and provides functionality to create, retrieve and 
// delete tables
public class Database implements Serializable {
    private String databaseName;

    private Map<String, Table> tables = new HashMap<String, Table>();

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    // FIXME creating table with no attributes
    public void createTable(String tableName, List<String> attributes) throws RuntimeException {
        // Check that table doesn't exist already. If it does, return false.
        if (tables.containsKey(tableName)) {
            throw new RuntimeException("ERROR: Table " + tableName + " already exists.");
        }

        tables.put(tableName, new Table(tableName, attributes));
    }

    // Returns table, or null if it doesn't exist
    public Table getTable(String tableName) throws RuntimeException {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new RuntimeException("ERROR: Unknown table " + tableName + ".");
        }
        return table;
    }

    public void dropTable(String tableName) throws RuntimeException {
        // remove() returns null if there was no mapping. As such, return false to
        // indicate table does not exist
        if (tables.remove(tableName) == null) {
            throw new RuntimeException("ERROR: Unknown table " + tableName + ".");
        }
    }
}