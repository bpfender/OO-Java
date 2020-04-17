package Expression;

//https://lukaszwrobel.pl/blog/interpreter-design-pattern/
//https://www.baeldung.com/java-interpreter-pattern

public interface Expression {
    public String interpret(Context context);

}