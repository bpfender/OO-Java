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
        switch (context.setTable(table)) {
            case 0:
                if (where == null) {
                    if (context.selectQuery()) {
                        return context.search();
                    }
                    return "ERROR Invalid attribute specified";
                }
                return where.interpret(context);
            case -1:
                return "ERROR No database set";
            // QUESTION, repetition from previous
            case -2:
                return "ERROR Unknown table";
            default:
                return "ERROR Undefined";
        }

    }

}