package Interpreter;

import Interpreter.Context.Mode;

public class DropTable implements Expression {
    String tableName;

    public DropTable(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.DROP);
        context.dropTable(tableName);
        return context.execute();
    }

}