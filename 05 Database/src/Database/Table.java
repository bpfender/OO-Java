package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Table stores data as columns, so each attribute is stored as an array of values. 
// This makes for easier searching when applying filters. Indeed uinitially the plan was to 
//  structure this as a b-tree for faster searching but this turned out to be a little outside 
// of scope, adding complications in terms of matching indices between columns and maintain 
// references effectively. Instead I decide to focus on the overall structure of all the 
// components. Table is ddesigned to be called with relevant requests for data and throws 
// exceptions (which are handled further down the chain) if problems do occur.
public class Table implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String tableName;

    private Map<String, Column> columns = new LinkedHashMap<>();
    private int records = 0;
    private int lastInsertId = 0;

    // Constructor generates an initial id column that exists in all tables. This
    // also ensures it is a reserved value as it can't be added, and validation
    // ensures it can't be deleted either. If attributes are specified, these are
    // added.
    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;

        columns.put("id", new Column());

        if (attributes != null) {
            for (String attributeName : attributes) {
                columns.put(attributeName, new Column());
            }
        }
    }

    public Column getColumn(String attribute) throws RuntimeException {
        Column column = columns.get(attribute);
        if (column == null) {
            throw new RuntimeException("Invalid attribute " + attribute + " specified.");
        }
        return column;
    }

    public void checkAttributeExists(String attribute) throws RuntimeException {
        if (!columns.containsKey(attribute)) {
            throw new RuntimeException("Invalid attribute " + attribute + " specified.");
        }
    }

    public ArrayList<String> getAttributeList() {
        ArrayList<String> attributeList = new ArrayList<>();
        attributeList.addAll(columns.keySet());
        return attributeList;
    }

    public ArrayList<String> getAttributeListWithoutId() {
        ArrayList<String> attributeList = getAttributeList();
        attributeList.remove("id");
        return attributeList;
    }

    public void deleteRow(int index) throws RuntimeException {
        if (records == 0) {
            throw new RuntimeException("Table contains no data to delete");
        }

        for (Column col : columns.values()) {
            col.deleteValue(index);
        }
        records--;
    }

    public void insertValues(List<String> values) throws RuntimeException {
        // Values won't contain id, so +1 to account for id column stored in table
        if (values.size() + 1 != columns.size()) {
            throw new RuntimeException("Values do not match table attributes.");
        }
        lastInsertId++;

        // Add id value to values to be insert into table
        List<String> idAndValues = new ArrayList<>();
        idAndValues.add(String.valueOf(lastInsertId));
        idAndValues.addAll(values);

        int index = 0;
        for (Column col : columns.values()) {
            col.addValue(idAndValues.get(index));
            index++;
        }

        records++;
    }

    public void addAttribute(String attribute) throws RuntimeException {
        // Not strictly needed, as hashmap will reject addition anyway as idc is added
        // as a column on initialisation. However, provides some additional diagnostics
        if (attribute.equals("id")) {
            throw new RuntimeException("Cannot add id. Reserved attribute.");
        }
        if (columns.containsKey(attribute)) {
            throw new RuntimeException("Cannot add attribute " + attribute + ".");
        }

        columns.put(attribute, new Column(records));
    }

    public void dropAttribute(String attribute) throws RuntimeException {
        if (attribute.equals("id")) {
            throw new RuntimeException("Cannot drop 'id'. Reserved column.");
        }
        if (columns.remove(attribute) == null) {
            throw new RuntimeException("Cannot drop attribute " + attribute + ". Does not exist.");
        }
    }

    public String getTableName() {
        return tableName;
    }

    public int getTableSize() {
        return records;
    }

}