package Interpreter;

import Interpreter.Context.Mode;

public class Delete implements Expression {
    Expression from;

    public Delete(Expression from) {
        this.from = from;
    }

    @Override
    // TODO no handling for where error with delete? although this should be picked
    // up in parsing
    public String interpret(Context context) throws RuntimeException {
        System.out.println("EXPRESSION DELETE");
        context.setMode(Mode.DELETE);
        return from.interpret(context);
    }

}