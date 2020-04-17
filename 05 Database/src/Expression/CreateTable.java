package Expression;

import java.util.List;

public class CreateTable implements Expression {
    private String tableName;

    public CreateTable(String tableName, List<String> attributeList) {
        this.tableName = tableName;
    }

    @Override
    public String interpret(Context context) {
        // TODO Auto-generated method stub
        // context.createTable(attributeList);
        return null;
    }

}