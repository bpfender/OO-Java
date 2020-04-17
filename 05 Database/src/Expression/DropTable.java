package Expression;

public class DropTable implements Expression {
    String tableName;

    public DropTable(String tableName) {
        this.tableName = tableName;
        // TODO error checking of expression type passed in?
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.dropTable(tableName);
        return null;
    }

}