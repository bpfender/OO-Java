package Database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
    private String tableName = new String();

    private ArrayList<Integer> ids = new ArrayList<>();
    private Map<String, Column> columns = new LinkedHashMap<>();
    private int lastId = 0;

    // TODO reusing ids?
    private Stack<Integer> freeIds = new Stack<>();

    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;

        if (attributes != null) {
            for (String col : attributes) {
                columns.put(col, new Column());
            }
        }
    }

    public Collection<String> getAttributes() {
        return columns.keySet();
    }

    public boolean insertValues(List<String> data) {
        if (data.size() == columns.size()) {
            ids.add(++lastId);

            // QUESTION neater way to do this?
            int index = 0;
            for (Column col : columns.values()) {
                col.addValue(data.get(index));
                index++;
            }
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

        columns.put(attribute, new Column());
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

}