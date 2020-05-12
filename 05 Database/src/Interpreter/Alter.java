package Interpreter;

public class Alter implements Expression {
    private String tableName;
    private Expression expression;

    public Alter(String tableName, Expression expression) {
        this.tableName = tableName;
        this.expression = expression;
    }

    public Alter(String tableName, Drop expression) {
        this.tableName = tableName;
        this.expression = expression;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.setTable(tableName);
        return expression.interpret(context);
    }

}