package Database;

import java.util.ArrayList;

public class Column {
    protected String columnName = new String();
    private ArrayList<String> column;

    public Column() {
        column = new ArrayList<>();
    }

    // QUESTION slightly dubious about initialisation here
    public Column(int size) {
        column = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            column.add("");
        }

    }

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