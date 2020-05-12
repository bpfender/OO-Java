package Interpreter;

import Interpreter.Context.Mode;

public class Join implements Expression {
    private Expression and;
    private Expression on;

    public Join(Expression and, Expression on) {
        this.and = and;
        this.on = on;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        // FIXME no error checking on this at the moment
        context.setMode(Mode.JOIN);
        and.interpret(context);
        on.interpret(context);
        return context.execute();
    }

}