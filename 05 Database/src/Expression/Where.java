package Expression;

public class Where implements Expression {
    private String conditions;

    public Where(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.setConditions(conditions);
        // return context.select();
        return null;
    }

}