package Database;

import java.io.Serializable;
import java.util.ArrayList;

abstract class AbstractColumn<T> implements Serializable {
    private String attributeName;
    // TODO does this have to be protected?
    protected ArrayList<T> column;

    public AbstractColumn() {
        column = new ArrayList<T>();
    }

    public void addValue(T value) {
        column.add(value);
    }

    public void deleteValue(int index) {
        column.remove(index);
    }

    public void updateValue(int index, T value) {
        column.set(index, value);
    }

    public T getValue(int index) {
        return column.get(index);
    }

    public ArrayList<T> getColumnValues() {
        return column;
    }

}