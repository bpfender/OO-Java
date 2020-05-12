package Interpreter;

// Various references used for the design of this system
//https://lukaszwrobel.pl/blog/interpreter-design-pattern/
//https://www.baeldung.com/java-interpreter-pattern
//https://www.oodesign.com/interpreter-pattern.html
//https://stackoverflow.com/questions/1052189/how-to-write-a-simple-database-engine
//https://shardingsphere.apache.org/document/current/en/features/sharding/principle/parse/

// The interpreter package makes use of the interpreter design pattern. Expression provides
// the interface for various components that implement features of the syntax. These all 
// share the interpret method, which varies in its implementation depending on what it is 
// supposed to do. By chaining together Expressions in the right order, commands can be
// executed. Context is manipulated and passed between expressions via the call
// to interpret(Context) to eventually return the desired result.
public interface Expression {
    public String interpret(Context context) throws RuntimeException;

}