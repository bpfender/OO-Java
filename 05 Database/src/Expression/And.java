package Expression;

public class And implements Expression {
    private String arg1;
    private String arg2;

    public And(String arg1, String arg2) {
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.boolean(arg1,arg2);
        // context.run();
        return null;
    }

}