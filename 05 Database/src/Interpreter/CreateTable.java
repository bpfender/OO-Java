package Interpreter;

import java.util.List;

public class CreateTable implements Expression {
    private String tableName;
    private List<String> attributeList;

    public CreateTable(String tableName, List<String> attributeList) {
        this.tableName = tableName;
        this.attributeList = attributeList;
    }

    @Override
    public String interpret(Context context) throws Exception {
        context.createTable(tableName, attributeList);
        return "OK";
    }

}