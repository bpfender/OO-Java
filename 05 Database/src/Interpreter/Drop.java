package Interpreter;

import Interpreter.Context.Mode;

public class Drop implements Expression {
    String attribute;

    public Drop(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.dropActiveTableAttribute(attribute);
        return context.execute();
    }

}