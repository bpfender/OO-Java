package Database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private String databaseName = new String();
    private Map<String, Table> tables = new HashMap<String, Table>();

    public Database(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean createTable(String tableName, List<String> attributes) {
        // check that table doesn't exist with that name
        // create new Table and populate if attributes have values
        // what to do with attribute list?
        if (tables.containsKey(tableName)) {
            return false;
        }

        tables.put(tableName, new Table(tableName, attributes));
        return true;
    }

    public Table fromTable(String table) {
        return tables.get(table);
    }

    public boolean dropTable(String table) {
        // Check that table exists
        // Delete table and contents if it does
        return false;
    }

}