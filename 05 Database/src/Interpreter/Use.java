package Interpreter;

// Use implements USE <database> functionality to select the active database through the
// DatabaseHandler
public class Use implements Expression {
    private String databaseName;

    public Use(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setActiveDatabase(databaseName);
        return "OK";
    }

}