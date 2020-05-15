package Interpreter;

public class Drop implements AlterationType {
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