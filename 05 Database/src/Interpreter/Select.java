package Interpreter;

import java.util.ArrayList;

import Interpreter.Context.Mode;

public class Select implements Expression {
    private ArrayList<String> attributes;
    private Expression from;

    // TODO overloading to select between * and attributes?
    public Select(ArrayList<String> attributes, From from) {
        this.attributes = attributes;
        this.from = from;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.SELECT);

        context.setSelectAttributes(attributes);
        return from.interpret(context);

    }

}