import Expression.Context;
import Expression.Expression;
import Parser.Parser;

public class Test {
    public static void main(String[] args) {
        System.out.println("TEST Started");

        Parser parser = new Parser();
        Context context = new Context();

        Expression expression = parser.parseQuery("create table name;");
        if (expression == null) {
            System.out.println(parser.getError());
        } else {
            System.out.println(expression.interpret(context));
        }
    }

}