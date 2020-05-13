package Interpreter;

import Interpreter.Context.Mode;

// Use implements USE <database> functionality to select the active database through the
// DatabaseHandler
public class Use implements Expression {
    private String databaseName;

    public Use(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.USE);
        context.setActiveDatabase(databaseName);
        return context.execute();
    }

}