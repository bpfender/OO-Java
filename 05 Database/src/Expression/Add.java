package Expression;

public class Add implements Expression {
    private String attribute;

    public Add(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) {
        if (context.add(attribute)) {
            return "OK";
        }

        return "ERROR Attribute already exists";
    }

}