package Interpreter;

import java.util.List;

// TODO add extra step for VALUES token?
public class Insert implements Expression {
    private String tableName;
    private List<String> values;

    public Insert(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setTable(tableName);
        context.insert(values);
        return "OK";
    }

}