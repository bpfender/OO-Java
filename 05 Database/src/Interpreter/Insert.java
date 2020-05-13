package Interpreter;

import java.util.List;

import Interpreter.Context.Mode;

public class Insert implements Expression {
    private String tableName;
    private List<String> values;

    public Insert(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.INSERT);
        context.setActiveTable(tableName);
        context.insertIntoActiveTable(values);
        return context.execute();
    }

}