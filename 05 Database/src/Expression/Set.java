package Expression;

import java.util.HashMap;

public class Set implements Expression {
    HashMap<String, String> values;
    Expression where;

    public Set(HashMap<String, String> values, Expression where) {
        this.values = values;
        this.where = where;

    }

    @Override
    public String interpret(Context context) {
        if (context.setNameValuePairs(values)) {
            return where.interpret(context);
        }
        return "ERROR Unknown attribute specified";
    }

}