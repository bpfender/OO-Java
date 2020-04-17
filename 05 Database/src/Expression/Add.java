package Expression;

public class Add implements Expression {
    private String attribute;

    public Add(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.add(attribute);
        return null;
    }

}