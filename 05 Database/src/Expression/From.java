package Expression;

public class From implements Expression {
    private String table;
    private Expression where;

    public From(String table, Expression where) {
        this.table = table;
        this.where = where;
    }

    @Override
    public String interpret(Context context) {
        // context.setTable(table);
        if (where == null) {
            // return context.select();
        }
        return where.interpret(context);
    }

}