package Expression;

import ConditionTree.*;

public class Where implements Expression {
    private Node conditions;

    public Where(Node conditions) {
        this.conditions = conditions;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.setFilter(conditions);
        return context.execute();
    }

}