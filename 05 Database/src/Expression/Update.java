package Expression;

import Expression.Context.Mode;

public class Update implements Expression {
    private String tableName;
    private Expression set;

    public Update(String tableName, Expression set) {
        this.tableName = tableName;
        this.set = set;
    }

    @Override
    public String interpret(Context context) {
        switch (context.setTable(tableName)) {
            case 0:
                context.setMode(Mode.UPDATE);
                return set.interpret(context);
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