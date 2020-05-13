package Interpreter;

import Interpreter.Context.Mode;

public class Alter implements Expression {
    private String tableName;
    private Expression expression;

    // TODO this needs to be fixed
    public Alter(String tableName, Expression expression) {
        this.tableName = tableName;
        this.expression = expression;
    }

    public Alter(String tableName, Drop expression) {
        this.tableName = tableName;
        this.expression = expression;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.ALTER);
        context.setActiveTable(tableName);
        return expression.interpret(context);
    }

}