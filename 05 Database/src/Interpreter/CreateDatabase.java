package Interpreter;

public class CreateDatabase implements Expression {
    private String databaseName;

    public CreateDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.createDatabase(databaseName);
        return "OK";
    }

}