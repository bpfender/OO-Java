package Expression;

public class DropDatabase implements Expression {
    String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
        // TODO error checking of expression type passed in?
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.dropDatabase(databaseName);
        return null;
    }

}