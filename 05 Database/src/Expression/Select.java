package Expression;

public class Select implements Expression {
    private String attributes;
    private Expression from;

    public Select(String attributes, Expression from) {
        this.attributes = attributes;
        this.from = from;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.setAttributes(attributes);
        return from.interpret(context);
    }

}