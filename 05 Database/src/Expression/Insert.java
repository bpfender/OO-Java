package Expression;

public class Insert implements Expression {
    private String tableName;
    private String values;

    public Insert(String tableName, String values) {
        this.tableName = tableName;
        this.values = values;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.insert(tableName, values);
        return null;
    }

}