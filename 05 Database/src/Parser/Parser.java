package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import ConditionTree.AndNode;
import ConditionTree.Node;
import ConditionTree.OperatorNode;
import ConditionTree.OrNode;
import Interpreter.Add;
import Interpreter.Alter;
import Interpreter.AlterationType;
import Interpreter.And;
import Interpreter.CreateDatabase;
import Interpreter.CreateTable;
import Interpreter.Delete;
import Interpreter.Drop;
import Interpreter.DropDatabase;
import Interpreter.DropTable;
import Interpreter.Expression;
import Interpreter.From;
import Interpreter.Insert;
import Interpreter.Join;
import Interpreter.On;
import Interpreter.Select;
import Interpreter.Set;
import Interpreter.Update;
import Interpreter.Use;
import Interpreter.Where;
import Tokenizer.Token;
import Tokenizer.TokenType;
import Tokenizer.Tokenizer;

// Would have like to try and implement the parse according to the reference below. 
// Ultimately ran out of time though. The below makes for interesting reading though ;)
// https://www.clear.rice.edu/comp212/05-spring/lectures/36/

// The parser analyses the tokenized query to check that it conforms to the grammar. 
// Based on this, it builds an expression which is returned as an Expression type. 
// This can then be interpreted as a query. THe parser doesn't do validation on input. It 
// simply builds the expression to be run

public class Parser {
    Tokenizer tokenizer = new Tokenizer();

    Token activeToken;
    LinkedList<Token> tokenQueue;

    // First check that string ends with ;. Trim last character and pass to
    // tokenizer and then parser.
    public Expression parseQuery(String query) throws RuntimeException {
        String input = query.trim();
        System.out.println("\nQUERY: " + input);

        if (!input.endsWith(";")) {
            throw new RuntimeException("Semicolon missing from end of query");
        }
        input = input.substring(0, input.length() - 1);

        tokenQueue = tokenizer.tokenize(input);
        Expression expression = parseCommand();

        if (!tokenQueue.isEmpty()) {
            throw new RuntimeException("Unexpected tokens remaining");
        }

        return expression;
    }

    // The main switch statement that determines which command is being parsed. All
    // parsing methods throw an exception on unexpected input, which will get passed
    // back all the way to here, allowing errors to be handled centrally
    private Expression parseCommand() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case USE:
                return parseUse();
            case CREATE:
                return parseCreate();
            case DROP:
                return parseDrop();
            case ALTER:
                return parseAlter();
            case INSERT:
                return parseInsert();
            case SELECT:
                return parseSelect();
            case UPDATE:
                return parseUpdate();
            case DELETE:
                return parseDelete();
            case JOIN:
                return parseJoin();
            default:
                throw new RuntimeException("Unexpected input. Invalid command '" + activeToken.getValue() + "'");
        }
    }

    /* ------ MAIN COMMAND TOKENS ------ */

    private Use parseUse() throws RuntimeException {
        return new Use(parseName());
    }

    private Expression parseCreate() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                return parseCreateTable();
            case DATABASE:
                return parseCreateDatabase();
            default:
                throw new RuntimeException("Must specify whether to CREATE database or table");
        }
    }

    private Expression parseDrop() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                return new DropTable(parseName());
            case DATABASE:
                return new DropDatabase(parseName());
            default:
                throw new RuntimeException("Must specify what to DROP");
        }
    }

    private Alter parseAlter() throws RuntimeException {
        consumeRequiredToken(TokenType.TABLE);
        String tableName = parseName();

        return new Alter(tableName, parseAlterationType());
    }

    private Insert parseInsert() throws RuntimeException {
        consumeRequiredToken(TokenType.INTO);

        String name = parseName();
        consumeRequiredToken(TokenType.VALUES);

        consumeRequiredToken(TokenType.OPENBRACKET);
        List<String> values = parseList(TokenType.LITERAL);
        consumeRequiredToken(TokenType.CLOSEBRACKET);

        return new Insert(name, values);
    }

    private Select parseSelect() throws RuntimeException {
        List<String> attributes = new ArrayList<>();

        // Must first peek to check if the token is wildcard or not. If it is, consume,
        // otherwise process the list
        switch (tokenQueue.peek().getToken()) {
            case WILD:
                getNextToken();
                return new Select(null, parseFrom());
            case NAME:
                attributes = parseList(TokenType.NAME);
                return new Select((ArrayList<String>) attributes, parseFrom());
            default:
                throw new RuntimeException("Expected * or attributes");
        }
    }

    private Update parseUpdate() throws RuntimeException {
        String name = parseName();
        return new Update(name, parseSet());
    }

    private Delete parseDelete() throws RuntimeException {
        return new Delete(parseFrom());
    }

    private Join parseJoin() throws RuntimeException {
        String name1 = parseName();
        consumeRequiredToken(TokenType.AND);
        String name2 = parseName();

        consumeRequiredToken(TokenType.ON);

        String attrib1 = parseName();
        consumeRequiredToken(TokenType.AND);
        String attrib2 = parseName();

        return new Join(new And(name1, name2), new On(attrib1, attrib2));
    }

    /* ------- Other reserved string commands ------ */

    private CreateTable parseCreateTable() throws RuntimeException {
        String name = parseName();
        List<String> attributes = null;

        if (!tokenQueue.isEmpty()) {
            consumeRequiredToken(TokenType.OPENBRACKET);
            attributes = parseList(TokenType.NAME);
            consumeRequiredToken(TokenType.CLOSEBRACKET);
        }

        return new CreateTable(name, attributes);
    }

    private CreateDatabase parseCreateDatabase() throws RuntimeException {
        return new CreateDatabase(parseName());
    }

    private AlterationType parseAlterationType() throws RuntimeException {
        getNextToken();
        switch (activeToken.getToken()) {
            case ADD:
                return new Add(parseName());
            case DROP:
                return new Drop(parseName());
            default:
                throw new RuntimeException("Expected ADD or DROP");
        }
    }

    private Set parseSet() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case SET:
                HashMap<String, String> nameValuePairs = parseNameValueList();
                return new Set(nameValuePairs, parseWhere());
            default:
                throw new RuntimeException("Expected SET token");
        }
    }

    private From parseFrom() throws RuntimeException {
        getNextToken();

        String name;
        switch (activeToken.getToken()) {
            case FROM:
                name = parseName();
                break;
            default:
                throw new RuntimeException("Expected FROM");
        }

        return new From(name, parseWhere());
    }

    // Slightly fudged. Doesn't require WHERE for Delete. Non-specified WHERE will
    // delete all records from table. Let's call it additional functionality. Could
    // also just have added a flag in the attributes to require WHERE or not
    private Where parseWhere() throws RuntimeException {
        getNextToken();
        switch (activeToken.getToken()) {
            case WHERE:
                return new Where(parseCondition());
            case END:
                return null;
            default:
                throw new RuntimeException("Unexpected token");
        }
    }

    /* ------- Parse lists ------ */

    // Parses single element (rather than namevaluepair) lists based on type passed
    // in
    private List<String> parseList(TokenType type) throws RuntimeException {
        List<String> attributes = new ArrayList<>();

        getNextToken();

        while (activeToken.getToken() == type) {
            attributes.add(activeToken.getValue());

            if (tokenQueue.peek().getToken() != TokenType.COMMA) {
                return attributes;
            }
            getNextToken(); // consume comma
            getNextToken(); // consume attirbute
        }

        throw new RuntimeException("Unexpected value in list");
    }

    private HashMap<String, String> parseNameValueList() throws RuntimeException {
        HashMap<String, String> nameValuePairs = new HashMap<>();
        parseNameValuePair(nameValuePairs);

        while (tokenQueue.peek().getToken() == TokenType.COMMA) {
            getNextToken(); // consume comma
            parseNameValuePair(nameValuePairs);
        }

        return nameValuePairs;
    }

    private void parseNameValuePair(HashMap<String, String> nameValuePairs) throws RuntimeException {
        String name = parseName();

        getNextToken();
        switch (activeToken.getToken()) {
            case PAIR:
                break;
            default:
                throw new RuntimeException("Expect = operator");
        }

        String value = parseValue();

        nameValuePairs.put(name, value);
    }

    /* ----- Functions to build condition tree ------ */

    // Builds condition tree for where statement.
    private Node parseCondition() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case OPENBRACKET:
                Node condition;
                Node leftNode = parseCondition();
                consumeRequiredToken(TokenType.CLOSEBRACKET);

                getNextToken();
                switch (activeToken.getToken()) {
                    case AND:
                        consumeRequiredToken(TokenType.OPENBRACKET);
                        condition = new AndNode(leftNode, parseCondition());
                        break;
                    case OR:
                        consumeRequiredToken(TokenType.OPENBRACKET);
                        condition = new OrNode(leftNode, parseCondition());
                        break;
                    default:
                        throw new RuntimeException("Expected AND or OR");

                }
                consumeRequiredToken(TokenType.CLOSEBRACKET);
                return condition;
            case NAME:
                String name = activeToken.getValue();
                Predicate<String> operator = parseOperator();
                return new OperatorNode(name, operator);
            default:
                throw new RuntimeException("In condition attribute");
        }

    }

    // Select relevant predicate based on comparison operator
    private Predicate<String> parseOperator() throws RuntimeException {
        getNextToken();
        Predicate<String> operator;
        String value;

        switch (activeToken.getToken()) {
            case EQUAL:
                value = parseValue();
                operator = i -> (i.equals(value));
                break;
            case GREATER:
                value = parseValue();
                operator = i -> (Float.parseFloat(i) > Float.parseFloat(value));
                break;
            case LESS:
                value = parseValue();
                operator = i -> (Float.parseFloat(i) < Float.parseFloat(value));
                break;
            case GREATEREQUAL:
                value = parseValue();
                operator = i -> (Float.parseFloat(i) >= Float.parseFloat(value));
                break;
            case LESSEQUAL:
                value = parseValue();
                operator = i -> (Float.parseFloat(i) <= Float.parseFloat(value));
                break;
            case NOTEQUAL:
                value = parseValue();
                operator = i -> (!i.equals(value));
                break;
            case LIKE:
                value = parseValue();
                // If parse float throws an exception, it's not a number so a valid LIKE
                // comparison can be made
                try {
                    Float.parseFloat(value);
                    throw new RuntimeException("String expected");
                } catch (NumberFormatException e) {
                    operator = i -> (i.contains(value));
                }

                break;
            default:
                throw new RuntimeException("Invalid comparison operator");
        }

        return operator;
    }

    /* ------ Parse tokens where value matters and is returned ------ */

    private String parseValue() throws RuntimeException {
        getNextToken();
        switch (activeToken.getToken()) {
            case LITERAL:
                return activeToken.getValue();
            default:
                throw new RuntimeException("Expected literal value");
        }
    }

    private String parseName() throws RuntimeException {
        getNextToken();

        switch (activeToken.getToken()) {
            case NAME:
                return activeToken.getValue();
            default:
                throw new RuntimeException("Must specify name");
        }
    }

    // Helper function to consume required tokens that are expected but don't
    // actually do anything
    private void consumeRequiredToken(TokenType type) throws RuntimeException {
        getNextToken();
        if (activeToken.getToken() != type) {
            throw new RuntimeException("Expected " + type);
        }
    }

    // Updates the activeToken with the next token in the queue. If the end of the
    // queue is reached, an END type is returned, which can be used to check that a
    // query terminated correctly/too/early/too late
    private void getNextToken() {
        if (tokenQueue.isEmpty()) {
            activeToken = new Token(TokenType.END, null);
        } else {
            activeToken = tokenQueue.pop();
        }
    }

}
