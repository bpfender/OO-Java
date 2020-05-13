package Interpreter;

public class From implements Expression {
    private String table;
    private Expression where;

    public From(String table, Where where) {
        this.table = table;
        this.where = where;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setActiveTable(table);

        if (where == null) {
            context.setFilter(null);
            return context.execute();
        } else {
            return where.interpret(context);
        }
    }

}
