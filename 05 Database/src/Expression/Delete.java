package Expression;

import Expression.Context.Mode;

public class Delete implements Expression {
    Expression from;

    public Delete(Expression from) {
        this.from = from;
    }

    @Override
    public String interpret(Context context) {
        context.setMode(Mode.DELETE);
        return from.interpret(context);
    }

}