package Interpreter;

public class And implements Expression {
    private String tableName1;
    private String tableName2;

    public And(String tableName1, String tableName2) {
        this.tableName1 = tableName1;
        this.tableName2 = tableName2;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setJoinTables(tableName1, tableName2);
        return null;
    }

}