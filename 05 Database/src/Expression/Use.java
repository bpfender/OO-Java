package Expression;

public class Use implements Expression {
    private String databaseName;

    public Use(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) {
        if (context.useDatabase(databaseName)) {
            return "OK";
        }
        return "ERROR Unknown database " + databaseName;
    }

}