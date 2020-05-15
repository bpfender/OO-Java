package Database;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

// Database class contains map of tables and provides functionality to create, retrieve and 
// delete tables
public class Database implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private HashMap<String, Table> tables = new HashMap<String, Table>();

    public void createTable(String tableName, List<String> attributes) throws RuntimeException {
        if (tables.containsKey(tableName)) {
            throw new RuntimeException("Table " + tableName + " already exists.");
        }

        tables.put(tableName, new Table(tableName, attributes));
    }

    public Table getTable(String tableName) throws RuntimeException {
        Table table = tables.get(tableName);
        if (table == null) {
            throw new RuntimeException("Unknown table " + tableName + ".");
        }
        return table;
    }

    public void dropTable(String tableName) throws RuntimeException {
        if (tables.remove(tableName) == null) {
            throw new RuntimeException("Unknown table " + tableName + ".");
        }
    }
}