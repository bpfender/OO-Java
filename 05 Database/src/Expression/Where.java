package Expression;

public class Where implements Expression {
    private String conditions;

    public Where(String conditions) {
        this.conditions = conditions;
    }

    @Override
    public String interpret(Context context) {
        context.setFilter(conditions);
        return context.search();
    }

}