package Interpreter;

public class On implements Expression {
    private String attributeTable1;
    private String attributeTable2;

    public On(String attributeTable1, String attributeTable2) {
        this.attributeTable1 = attributeTable1;
        this.attributeTable2 = attributeTable2;
    }

    @Override
    public String interpret(Context context) throws RuntimeException {
        context.generateJoinTable(attributeTable1, attributeTable2);
        return null;
    }

}