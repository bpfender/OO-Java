package Interpreter;

public class DropDatabase implements Expression {
    String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.dropDatabase(databaseName);
        return "OK";
    }

}