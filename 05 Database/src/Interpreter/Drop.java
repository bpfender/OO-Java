package Interpreter;

public class Drop implements Expression {
    String attribute;

    public Drop(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.drop(attribute);
        return "OK";
    }

}