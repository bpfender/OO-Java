package Expression;

public class DropTable implements Expression {
    String tableName;

    public DropTable(String tableName) {
        this.tableName = tableName;
        // TODO error checking of expression type passed in?
    }

    @Override
    public String interpret(Context context) {
        switch (context.dropTable(tableName)) {
            case 0:
                return "OK";
            case -1:
                return "ERROR No database specified";
            case -2:
                return "ERROR Unknown table " + tableName;
            default:
                return "ERROR Undefined error";
        }

    }

}