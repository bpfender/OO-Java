package Expression;

public class Drop implements Expression {
    String attribute;

    public Drop(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) {
        if (context.drop(attribute)) {
            return "OK";
        }

        return "ERROR Unknown attribute " + attribute;
    }

}