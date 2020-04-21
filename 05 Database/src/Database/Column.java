package Database;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private List<String> column = new ArrayList<>();

    public void addValue(String value) {
        column.add(value);

    }

    public void addValue(int index, String value) {
        column.add(index, value);
    }

}