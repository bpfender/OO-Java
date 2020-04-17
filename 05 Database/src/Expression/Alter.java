package Expression;

public class Alter implements Expression {
    private String tableName;
    private Expression expression;

    public Alter(String tableName, Expression expression) {
        this.tableName = tableName;
        this.expression = expression;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.setTable(tableName);
        return expression.interpret(context);
    }

}