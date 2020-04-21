package Expression;

import java.util.List;

public class CreateTable implements Expression {
    private String tableName;
    private List<String> attributeList;

    public CreateTable(String tableName, List<String> attributeList) {
        this.tableName = tableName;
        this.attributeList = attributeList;
    }

    @Override
    public String interpret(Context context) {
        switch (context.createTable(tableName, attributeList)) {
            case 0:
                return "OK";
            case -1:
                return "ERROR No database specified";
            case -2:
                return "ERROR Table " + tableName + " already exists";
            default:
                return "ERROR Undefined";
        }
    }

}