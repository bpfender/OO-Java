package Interpreter;

import Interpreter.Context.Mode;

public class DropDatabase implements Expression {
    String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.DROP);
        context.dropDatabase(databaseName);
        return context.execute();
    }

}