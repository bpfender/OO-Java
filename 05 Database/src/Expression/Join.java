package Expression;

import Expression.Context.Mode;

public class Join implements Expression {
    private Expression and;
    private Expression on;

    public Join(Expression and, Expression on) {
        this.and = and;
        this.on = on;
    }

    @Override
    public String interpret(Context context) {
        // FIXME no error checking on this at the moment
        context.setMode(Mode.JOIN);
        and.interpret(context);
        on.interpret(context);
        return context.search();
    }

}