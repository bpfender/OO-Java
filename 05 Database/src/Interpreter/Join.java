package Interpreter;

import Interpreter.Context.Mode;

public class Join implements Expression {
    private And and;
    private On on;

    public Join(And and, On on) {
        this.and = and;
        this.on = on;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.JOIN);
        and.interpret(context);
        on.interpret(context);
        return context.execute();
    }

}