package Interpreter;

import Interpreter.Context.Mode;

public class Update implements Expression {
    private String tableName;
    private Expression set;

    public Update(String tableName, Expression set) {
        this.tableName = tableName;
        this.set = set;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setActiveTable(tableName);
        context.setMode(Mode.UPDATE);
        return set.interpret(context);
    }

}