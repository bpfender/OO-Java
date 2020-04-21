package Database;

import java.util.List;
import java.util.Map;

public class Table {
    private String tableName = new String();

    private Map<String, Integer> columnNames;

    private List<Record> rows;

    public Table(String tableName, List<String> attributes) {
        this.tableName = tableName;

        if (attributes != null) {

        }
    }

    public void insertValue(List<String> data) {

    }

    public void getRowByIndex(int index) {

    }

    public void deleteRow(int index) {

    }

    public void addAttribute(String attribute) {
        // adding null elements?
    }

    public void dropAttribute(String attribute) {
        // delete elements from memory?
    }

}