package Interpreter;

import Interpreter.Context.Mode;

public class Update implements Expression {
    private String tableName;
    private Expression set;

    public Update(String tableName, Set set) {
        this.tableName = tableName;
        this.set = set;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.UPDATE);
        context.setActiveTable(tableName);
        return set.interpret(context);
    }

}