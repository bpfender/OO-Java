package Expression;

import java.util.ArrayList;

public class Select implements Expression {
    private ArrayList<String> attributes;
    private Expression from;

    public Select(ArrayList<String> attributes, Expression from) {
        this.attributes = attributes;
        this.from = from;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        if (context.select(attributes)) {
            return from.interpret(context);
        }

        return "ERROR No database set";

    }

}