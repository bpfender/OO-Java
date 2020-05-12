package Interpreter;

import java.util.ArrayList;

import Interpreter.Context.Mode;

public class Select implements Expression {
    private ArrayList<String> attributes;
    private Expression from;

    // TODO overloading to select between * and attributes?
    public Select(ArrayList<String> attributes, Expression from) {
        this.attributes = attributes;
        this.from = from;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        // TODO work on how operating modes are set
        context.setMode(Mode.SELECT);

        context.select(attributes);
        return from.interpret(context);

    }

}