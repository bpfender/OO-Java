package Expression;

public class CreateDatabase implements Expression {
    private String databaseName;

    public CreateDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.createDatabase(databaseName);
        return null;
    }

}