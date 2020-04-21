package Database;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
    private String tableName = new String();

    private Map<String, Column> columns = new LinkedHashMap<>();

    // TODO reusing ids?
    private Stack<Integer> freeIds = new Stack<>();

    private List<Record> rows;

    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;

        if (attributes != null) {
            for (String col : attributes) {
                columns.put(col, new Column());
            }
        }
    }

    public boolean insertValues(List<String> data) {
        if (data.size() == columns.size()) {
            for (Column col : columns.values()) {

            }
        }
        // No reusing of indexes;
        // No analysis on data types currently
        return false;
    }

    public void getRowByIndex(int index) {

    }

    public void deleteRow(int index) {

    }

    public boolean addAttribute(String attribute) {
        if (columns.containsKey(attribute)) {
            return false;
        }

        columns.put(attribute, new Column());
        return true;
    }

    public boolean dropAttribute(String attribute) {
        if (columns.remove(attribute) == null) {
            return false;
        }
        return true;
    }

}