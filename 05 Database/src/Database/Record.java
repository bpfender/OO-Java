package Database;

import java.util.ArrayList;
import java.util.List;

public class Record {
    private List<String> row = new ArrayList<String>();

    public Record(List<String> row) {
        this.row = row;
    }

    public boolean addRecordByAttribute(int index, String data) {
        if (index < row.size()) {
            row.set(index, data);
            return true;
        }
        return false;
    }

    public String getRecordByAttribute(int index) {
        if (index < row.size()) {
            return row.get(index);
        }
        return null;
    }

}