package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import ConditionTree.*;
import ConditionTree.AndNode;
import ConditionTree.Node;
import ConditionTree.OperatorNode;
import Expression.*;
import Tokenizer.*;

//https://www.clear.rice.edu/comp212/05-spring/lectures/36/

// The parser analyses the tokenized query to check that it conforms to the grammar. 
// Based on this, it builds an expression which is returned as an Expression type. 
// This can then be interpreted as a query. THe parser doesn't do validation on input. It 
// simply builds the expression to be run

public class Parser {
    // TODO seperate out the tokenizer constructor call
    Tokenizer tokenizer = new Tokenizer();

    Token activeToken;
    LinkedList<Token> tokenQueue;

    String error;

    // TODO this should probably just throw an error
    // TODO this functionality can probably be modified. One try catch block,
    // handling multi-line queries etc.
    // TODO still need to deal with too long queries
    // TODO error can be concatenated ERROR: +?
    public Expression parseQuery(String query) {
        String input = query.trim();
        System.out.println("QUERY: " + query);

        if (!input.endsWith(";")) {
            error = "ERROR: Semicolon missing from end of query";
            return null;
        }
        input = input.substring(0, input.length() - 1);

        try {
            tokenQueue = tokenizer.tokenize(input);
        } catch (Exception e) {
            error = e.getMessage();
        }

        try {
            return parseCommand();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public String getError() {
        return error;
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

    // TODO can this be refactored into a design pattern (strategy/chain of
    // responsibiltiy)?
    // TODO return types of parseMethods
    // The main switch statement that determines which command is being parsed. All
    // parsing methods throw an exception on unexpected input, which will get passed
    // back all the way to here, allowing errors to be handled centrally
    private Expression parseCommand() throws Exception {
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
                throw new Exception("ERROR: Unexpected input. Invalid command '" + activeToken.getValue() + "'");
        }
    }

    private Use parseUse() throws Exception {
        return new Use(parseName());
    }

    private Expression parseCreate() throws Exception {
        getNextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                return parseCreateTable();
            case DATABASE:
                return parseCreateDatabase();
            default:
                throw new Exception("ERROR: Must specify whether to CREATE database or table");
        }
    }

    private CreateTable parseCreateTable() throws Exception {
        String name = parseName();
        List<String> attributes = null;

        if (!tokenQueue.isEmpty()) {
            parseOpenBracket();
            attributes = parseList(TokenType.NAME);
            parseCloseBracket();
        }

        return new CreateTable(name, attributes);

    }

    private CreateDatabase parseCreateDatabase() throws Exception {
        return new CreateDatabase(parseName());
    }

    private Expression parseDrop() throws Exception {
        getNextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                return new DropTable(parseName());
            case DATABASE:
                return new DropDatabase(parseName());
            default:
                throw new Exception("ERROR: Must specify what to DROP");
        }
    }

    private Alter parseAlter() throws Exception {
        consumeRequiredToken(TokenType.TABLE);

        // FIXME this relies on order of evaluation, may be brittle
        return new Alter(parseName(), parseAlterationType());
    }

    private Expression parseAlterationType() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case ADD:
                return new Add(parseName());
            case DROP:
                return new Drop(parseName());
            default:
                throw new Exception("ERROR: Expected ADD or DROP");
        }
    }

    // Helper function to consume required tokens that are expected but don't
    // actually do anything
    private void consumeRequiredToken(TokenType type) throws Exception {
        getNextToken();
        if (activeToken.getToken() != type) {
            throw new Exception("ERROR: Expected " + type);
        }
    }

    private Insert parseInsert() throws Exception {
        consumeRequiredToken(TokenType.INTO);

        String name = parseName();

        getNextToken();
        if (activeToken.getToken() != TokenType.VALUES) {
            throw new Exception("ERROR Expected VALUES token");
        }

        consumeRequiredToken(TokenType.OPENBRACKET);
        List<String> attributes = parseList(TokenType.LITERAL);
        consumeRequiredToken(TokenType.CLOSEBRACKET);

        return new Insert(name, attributes);
    }

    // TODO
    private Select parseSelect() throws Exception {

        List<String> attributes = new ArrayList<>();

        switch (tokenQueue.peek().getToken()) {
            case WILD:
                getNextToken();
                return new Select(null, parseFrom());
            case NAME:
                attributes = parseList(TokenType.NAME);
                return new Select((ArrayList<String>) attributes, parseFrom());
            default:
                throw new Exception("ERROR Expected * or attributes");
        }

    }

    private Update parseUpdate() throws Exception {
        String name = parseName();
        return new Update(name, parseSet());
    }

    private Delete parseDelete() throws Exception {
        return new Delete(parseFrom());
        // TODO this doesn't handle variable where at the moment i.e. must have where-
        // ensure!
    }

    private Join parseJoin() throws Exception {
        String name1 = parseName();
        parseAnd();
        String name2 = parseName();

        parseOn();

        String attrib1 = parseName();
        parseAnd();
        String attrib2 = parseName();

        return new Join(new And(name1, name2), new On(attrib1, attrib2));

    }

    private void parseAnd() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case AND:
                break;
            default:
                throw new Exception("ERROR Expected AND");
        }
    }

    private void parseOn() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case ON:
                break;
            default:
                throw new Exception("ERROR Expected ON");
        }

    }

    private Expression parseSet() throws Exception {
        getNextToken();

        switch (activeToken.getToken()) {
            case SET:
                HashMap<String, String> nameValuePairs = parseNameValueList();
                return new Set(nameValuePairs, parseWhere());
            default:
                throw new Exception("ERROR Expected SET token");
        }
    }

    private HashMap<String, String> parseNameValueList() throws Exception {
        HashMap<String, String> nameValuePairs = new HashMap<>();
        parseNameValuePair(nameValuePairs);

        while (tokenQueue.peek().getToken() == TokenType.COMMA) {
            getNextToken(); // consume comma
            parseNameValuePair(nameValuePairs);
        }

        return nameValuePairs;
    }

    private void parseNameValuePair(HashMap<String, String> nameValuePairs) throws Exception {
        String name = parseName();

        getNextToken();
        switch (activeToken.getToken()) {
            case PAIR:
                break;
            default:
                throw new Exception("ERROR Expect = operator");
        }

        String value = parseValue();

        nameValuePairs.put(name, value);
    }

    private Expression parseFrom() throws Exception {
        getNextToken();

        String name;
        switch (activeToken.getToken()) {
            case FROM:
                name = parseName();
                break;
            default:
                throw new Exception("ERROR Expected FROM");

        }
        return new From(name, parseWhere());
    }

    private Expression parseWhere() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case WHERE:
                return new Where(parseCondition());
            default:
                return null;
        }
    }

    private Node parseCondition() throws Exception {
        getNextToken();

        switch (activeToken.getToken()) {
            case OPENBRACKET:
                Node condition;
                Node leftNode = parseCondition();
                parseCloseBracket();

                getNextToken();
                switch (activeToken.getToken()) {
                    case AND:
                        parseOpenBracket();
                        condition = new AndNode(leftNode, parseCondition());
                        break;
                    case OR:
                        parseOpenBracket();
                        condition = new OrNode(leftNode, parseCondition());
                        break;
                    default:
                        throw new Exception("ERROR Expected AND or OR");

                }
                parseCloseBracket();
                return condition;
            case NAME:
                String name = activeToken.getValue();
                Predicate<String> operator = parseOperator();
                return new OperatorNode(name, operator);
            default:
                throw new Exception("ERROR In condition 1");
        }

    }

    private Predicate<String> parseOperator() throws Exception {
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
                operator = i -> (i.contains(value));
                break;
            default:
                throw new Exception("ERROR Invalid comparison operator");
        }

        return operator;
    }

    private String parseValue() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case LITERAL:
                return activeToken.getValue();
            default:
                throw new Exception("ERROR Expected literal value");
        }
    }

    private List<String> parseList(TokenType type) throws Exception {
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

        throw new Exception("ERROR Unexprected value in list");

    }

    private String parseName() throws Exception {
        getNextToken();

        switch (activeToken.getToken()) {
            case NAME:
                return activeToken.getValue();
            default:
                throw new Exception("ERROR Must specify name");

        }
    }

    private void parseOpenBracket() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case OPENBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected opening bracket");
        }
    }

    private void parseCloseBracket() throws Exception {
        getNextToken();
        switch (activeToken.getToken()) {
            case CLOSEBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected closing bracket");
        }
    }

}
