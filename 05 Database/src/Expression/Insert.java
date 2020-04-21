package Expression;

import java.util.List;

public class Insert implements Expression {
    private String tableName;
    private List<String> values;

    public Insert(String tableName, List<String> values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public String interpret(Context context) {
        switch (context.setTable(tableName)) {
            case 0:
                // FIXME this is too nested. Add extra interp step
                if (context.insert(values)) {
                    return "OK";
                }
                return "ERROR Incorrect input";
            case -1:
                return "ERROR No db specced";
            case -2:
                return "ERROR Unknown table";
            default:
                return "ERRROR Undefined";
        }

    }

}