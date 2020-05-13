package Database;

import java.io.Serializable;
import java.util.ArrayList;

//The original plan was to build this as an abstract class to allow varying data types.
// However given that the spec says that only strings is fine, this appraoach wasn't taken.
public class Column implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private ArrayList<String> column;

    // Generate an empty column
    public Column() {
        column = new ArrayList<String>();
    }

    // Used when an attribute is added to a table, to populate the column with empty
    // values
    public Column(int size) {
        column = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            column.add("");
        }

    }

    public String getValue(int index) {
        return column.get(index);
    }

    public void addValue(String value) {
        column.add(value);
    }

    public void deleteValue(int index) {
        column.remove(index);
    }

    public void updateValue(int index, String value) {
        column.set(index, value);
    }

}