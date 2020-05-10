package Expression;

public class CreateDatabase implements Expression {
    private String databaseName;

    public CreateDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.createDatabase(databaseName);
        return "OK";
    }

}