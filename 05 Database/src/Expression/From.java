package Expression;

public class From implements Expression {
    private String table;
    private Expression where;

    public From(String table, Expression where) {
        this.table = table;
        this.where = where;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.setTable(table);

        context.validateSelectAttributes();

        if (where == null) {
            context.setFilter(null);
            return context.execute();
        } else {
            return where.interpret(context);
        }
    }

}
