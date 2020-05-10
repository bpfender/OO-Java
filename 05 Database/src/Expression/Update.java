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
    public String interpret(Context context) throws Exception {
        context.setTable(tableName);
        context.setMode(Mode.UPDATE);
        return set.interpret(context);
    }

}