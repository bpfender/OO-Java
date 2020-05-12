package Interpreter;

public class Add implements Expression {
    private String attribute;

    public Add(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.add(attribute);
        return "OK";
    }
}