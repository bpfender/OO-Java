package Interpreter;

public class DropDatabase implements Expression {
    String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.dropDatabase(databaseName);
        return "OK";
    }

}