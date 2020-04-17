package Expression;

public class Delete implements Expression {
    Expression from;

    public Delete(Expression from) {
        this.from = from;
    }

    @Override
    public String interpret(Context context) {
        return from.interpret(context);
    }

}