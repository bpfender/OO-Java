package Interpreter;

import java.util.List;

import Interpreter.Context.Mode;

public class CreateTable implements Expression {
    private String tableName;
    private List<String> attributeList;

    public CreateTable(String tableName, List<String> attributeList) {
        this.tableName = tableName;
        this.attributeList = attributeList;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.setMode(Mode.CREATE);
        context.createTable(tableName, attributeList);
        return context.execute();
    }

}