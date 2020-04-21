package Expression;

public class CreateDatabase implements Expression {
    private String databaseName;

    public CreateDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) {
        if (context.createDatabase(databaseName)) {
            return "OK";
        }
        return "ERROR " + databaseName + " already exists";
    }

}