package Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
    private String tableName = new String();

    public ArrayList<Integer> ids = new ArrayList<>();
    private Map<String, Column> columns = new LinkedHashMap<>();
    private int lastId = 0;

    // TODO reusing ids?
    private Stack<Integer> freeIds = new Stack<>();

    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;
        columns.put("id", new Column());

        if (attributes != null) {
            for (String col : attributes) {
                columns.put(col, new Column());
            }
        }
    }

    public Column getColumn(String attribute) {
        return columns.get(attribute);
    }

    public String getId(Integer index) {
        return String.valueOf(ids.get(index));
    }

    public void deleteId(Integer index) {
        ids.remove(index);
    }

    public Collection<String> getAttributes() {
        return columns.keySet();
    }

    public Collection<Column> getColumns() {
        return columns.values();
    }

    public boolean insertValues(List<String> data) {

        if (data.size() == columns.size()) {
            System.out.println(columns);
            ids.add(++lastId);

            // QUESTION neater way to do this?
            int index = 0;
            for (Column col : columns.values()) {
                if (col.columnName.equals("id")) {
                    col.addValue(String.valueOf(lastId));
                }
                col.addValue(data.get(index));
                index++;
            }
            return true;
        }
        // No reusing of indexes;
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

        columns.put(attribute, new Column(ids.size()));
        return true;

        // FIXME prevent adding * and id
    }

    public boolean dropAttribute(String attribute) {
        // FIXME prevent dropping id
        if (columns.remove(attribute) == null) {
            return false;
        }
        return true;
    }

    public String getTableName() {
        return tableName;
    }

}