package Expression;

public class Drop implements Expression {
    String attribute;

    public Drop(String attribute) {
        this.attribute = attribute;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.drop(attribute);
        return null;
    }

}