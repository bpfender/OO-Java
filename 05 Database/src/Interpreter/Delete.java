package Interpreter;

import Interpreter.Context.Mode;

public class Delete implements Expression {
    Expression from;

    public Delete(From from) {
        this.from = from;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.DELETE);
        return from.interpret(context);
    }

}