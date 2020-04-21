package Expression;

public class Join implements Expression {
    private Expression and;
    private Expression on;

    public Join(Expression and, Expression on) {
        this.and = and;
        this.on = on;
    }

    @Override
    public String interpret(Context context) {
        // context.setAction("join");
        and.interpret(context);
        on.interpret(context);
        return null;
    }

}