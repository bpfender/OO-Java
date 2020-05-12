package Database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Table implements Serializable {
    private String tableName;

    private int records = 0;
    private int lastInsertId = 0;
    private Map<String, Column> columns = new LinkedHashMap<>();

    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;

        columns.put("id", new Column());

        if (attributes != null) {
            for (String attributeName : attributes) {
                columns.put(attributeName, new Column());
            }
        }
    }

    public int getTableSize() {
        return records;
    }

    public Column getColumn(String attribute) {
        return columns.get(attribute);
    }

    public Collection<String> getAttributes() {
        return columns.keySet();
    }

    public Collection<Column> getColumns() {
        return columns.values();
    }

    public boolean checkAttributeExists(String attribute) {
        return columns.containsKey(attribute);
    }

    public ArrayList<String> getAttributeList() {
        ArrayList<String> attributeList = new ArrayList<>();
        attributeList.addAll(columns.keySet());
        return attributeList;
    }

    // TODO throw exception if empty
    public void deleteRow(int index) throws Exception {
        if (records == 0) {
            throw new Exception("ERROR: Table contains no data to delete");
        }

        for (Column col : columns.values()) {
            col.deleteValue(index);
        }
        records--;
    }

    // TODO this is fucking messy at the moment. Please clean up
    public boolean insertValues(List<String> values) {
        if (values.size() + 1 == columns.size()) {
            System.out.println(columns);
            lastInsertId++;

            List<String> idAndValues = new ArrayList<>();
            idAndValues.add(String.valueOf(lastInsertId));
            idAndValues.addAll(values);

            // QUESTION neater way to do this?
            int index = 0;
            for (Column col : columns.values()) {
                col.addValue(idAndValues.get(index));
                index++;
            }

            records++;
            return true;
        }
        return false;
    }

    // TODO prevent adding of reserved keywords id and * - not needed for *, id
    // automatically excluded
    public void addAttribute(String attribute) throws Exception {
        if (columns.containsKey(attribute)) {
            throw new Exception("ERROR: Cannot add attribute " + attribute + ".");
        }

        columns.put(attribute, new Column(records));
    }

    public void dropAttribute(String attribute) throws Exception {
        if (attribute.equals("id")) {
            throw new Exception("ERROR: Cannot drop 'id'. Reserved column.");
        }

        if (columns.remove(attribute) == null) {
            throw new Exception("ERROR: Cannot drop attribute " + attribute + ". Does not exist.");

        }
    }

    public String getTableName() {
        return tableName;
    }

}