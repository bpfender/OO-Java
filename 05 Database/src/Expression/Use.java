package Expression;

public class Use implements Expression {
    private String databaseName;

    public Use(String databaseName) {
        this.databaseName = databaseName;
    }

    @Override
    public String interpret(Context context) {
        // TODO context should load the appropriate database
        // context.use(databaseName);
        if (true) {
            return databaseName;
        } else {
            return "An error ocurred";
        }
    }

}