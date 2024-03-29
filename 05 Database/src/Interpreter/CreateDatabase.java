package Interpreter;

import Interpreter.Context.Mode;

public class CreateDatabase implements Expression {
    private String databaseName;

    public CreateDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.CREATE);
        context.createDatabase(databaseName);
        return context.execute();
    }

}