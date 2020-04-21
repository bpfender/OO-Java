package Expression;

public class DropDatabase implements Expression {
    String databaseName;

    public DropDatabase(String databaseName) {
        this.databaseName = databaseName;
        // TODO error checking of expression type passed in?
    }

    @Override
    public String interpret(Context context) {
        if (context.dropDatabase(databaseName)) {
            return "OK";
        }
        return "ERROR Unknown table " + databaseName;
    }

}