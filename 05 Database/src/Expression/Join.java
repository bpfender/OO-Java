package Expression;

public class Join implements Expression {
    String table;
    Expression and;

    public Join(String table, Expression and) {
        this.table = table;
        this.and = and;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        return null;
    }

}