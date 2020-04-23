package Database;

import java.util.ArrayList;

public class Column {
    private String columnName = new String();
    private ArrayList<String> column = new ArrayList<>();

    public void addValue(String value) {
        column.add(value);

    }

    public void addValue(int index, String value) {
        column.add(index, value);
    }

    public void deleteValue(int index) {
        column.remove(index);
    }

    public void updateValue(int index, String value) {
        column.set(index, value);
    }

    public ArrayList<String> getColumnValues() {
        return column;
    }

}