package Database;

import java.util.ArrayList;

public class Column extends AbstractColumn<String> {
    public Column() {
        super();
    }

    // QUESTION slightly dubious about initialisation here
    public Column(int size) {
        super.column = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            super.column.add("");
        }

    }
}