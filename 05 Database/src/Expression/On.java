package Expression;

public class On implements Expression {
    private String attribute1;
    private String attribute2;

    public On(String attribute1, String attribute2) {
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
    }

    @Override
    public String interpret(Context context) throws Exception {
        System.out.println("JOINS: " + attribute1 + " " + attribute2);
        context.setJoinOn(attribute1, attribute2);
        return null;
    }

}