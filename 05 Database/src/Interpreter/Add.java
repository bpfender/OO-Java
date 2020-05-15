package Interpreter;

public class Add implements AlterationType {
    private String attribute;

    public Add(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.addActiveTableAttribute(attribute);
        return context.execute();
    }
}