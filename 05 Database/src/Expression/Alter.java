package Expression;

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
    public String interpret(Context context) {

        switch (context.setTable(tableName)) {
            case 0:
                // QUESTION successful type passing?
                return expression.interpret(context);
            case -1:
                return "ERROR No database specified";
            case -2:
                return "ERROR Unknown table " + tableName;
            default:
                return "ERROR Undefined error";

        }
    }

}