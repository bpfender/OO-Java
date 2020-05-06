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
// This can then be interpreted as a query

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
    private void nextToken() {
        if (tokenQueue.isEmpty()) {
            activeToken = new Token(TokenType.END, null);
        } else {
            activeToken = tokenQueue.pop();
        }
    }

    private Expression parseCommand() throws Exception {
        nextToken();

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
                throw new Exception("ERROR: Unexpected token. Invalid command");
        }
    }

    private Use parseUse() throws Exception {
        return new Use(parseName());
    }

    private Expression parseCreate() throws Exception {
        nextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                String name = parseName();
                if (tokenQueue.isEmpty()) {
                    return new CreateTable(name, null);
                } else {
                    parseOpenBracket();
                    List<String> attributes = parseList(TokenType.NAME);
                    parseCloseBracket();

                    return new CreateTable(name, attributes);
                }

            case DATABASE:
                return new CreateDatabase(parseName());
            default:
                throw new Exception("ERROR Must specify whether to create database or table");
        }
    }

    private Expression parseDrop() throws Exception {
        nextToken();

        switch (activeToken.getToken()) {
            case TABLE:
                return new DropTable(parseName());
            case DATABASE:
                return new DropDatabase(parseName());
            default:
                throw new Exception("ERROR Must specify what to DROP");
        }
    }

    private Expression parseAlter() throws Exception {
        nextToken();
        if (activeToken.getToken() != TokenType.TABLE) {
            throw new Exception("ERROR Expected TABLE token");
        }

        String name = parseName();
        Expression alterTokenType;

        nextToken();
        switch (activeToken.getToken()) {
            case ADD:
                alterTokenType = new Add(parseName());
                break;
            case DROP:
                alterTokenType = new Drop(parseName());
                break;
            default:
                throw new Exception("ERROR Expected ADD or DROP");
        }

        return new Alter(name, alterTokenType);
    }

    private Expression parseInsert() throws Exception {
        nextToken();
        if (activeToken.getToken() != TokenType.INTO) {
            throw new Exception("ERROR Expected INTO");
        }

        String name = parseName();

        nextToken();
        if (activeToken.getToken() != TokenType.VALUES) {
            throw new Exception("ERROR Expected VALUES token");
        }

        parseOpenBracket();
        List<String> attributes = parseList(TokenType.LITERAL);
        parseCloseBracket();

        return new Insert(name, attributes);
    }

    private Expression parseSelect() throws Exception {

        List<String> attributes = new ArrayList<>();

        switch (tokenQueue.peek().getToken()) {
            case WILD:
                nextToken();
                attributes.add("*");
                break;
            case NAME:
                attributes = parseList(TokenType.NAME);
                break;
            default:
                throw new Exception("ERROR Expected * or attributes");
        }

        return new Select((ArrayList<String>) attributes, parseFrom());

    }

    private Expression parseUpdate() throws Exception {
        String name = parseName();
        return new Update(name, parseSet());
    }

    private Expression parseDelete() throws Exception {
        return new Delete(parseFrom());
        // TODO this doesn't handle variable where at the moment i.e. must have where-
        // ensure!
    }

    private Expression parseJoin() throws Exception {
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
        nextToken();
        switch (activeToken.getToken()) {
            case AND:
                break;
            default:
                throw new Exception("ERROR Expected AND");
        }
    }

    private void parseOn() throws Exception {
        nextToken();
        switch (activeToken.getToken()) {
            case ON:
                break;
            default:
                throw new Exception("ERROR Expected ON");
        }

    }

    private Expression parseSet() throws Exception {
        nextToken();

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
            nextToken(); // consume comma
            parseNameValuePair(nameValuePairs);
        }

        return nameValuePairs;
    }

    private void parseNameValuePair(HashMap<String, String> nameValuePairs) throws Exception {
        String name = parseName();

        nextToken();
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
        nextToken();

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
        nextToken();
        switch (activeToken.getToken()) {
            case WHERE:
                return new Where(parseCondition());
            default:
                return null;
        }
    }

    private Node parseCondition() throws Exception {
        nextToken();

        switch (activeToken.getToken()) {
            case OPENBRACKET:
                Node condition;
                Node leftNode = parseCondition();
                parseCloseBracket();

                nextToken();
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
        nextToken();
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
        nextToken();
        switch (activeToken.getToken()) {
            case LITERAL:
                return activeToken.getValue();
            default:
                throw new Exception("ERROR Expected literal value");
        }
    }

    private List<String> parseList(TokenType type) throws Exception {
        List<String> attributes = new ArrayList<>();

        nextToken();

        while (activeToken.getToken() == type) {
            attributes.add(activeToken.getValue());

            if (tokenQueue.peek().getToken() != TokenType.COMMA) {
                return attributes;
            }
            nextToken(); // consume comma
            nextToken(); // consume attirbute
        }

        throw new Exception("ERROR Unexprected value in list");

    }

    private String parseName() throws Exception {
        nextToken();

        switch (activeToken.getToken()) {
            case NAME:
                return activeToken.getValue();
            default:
                throw new Exception("ERROR Must specify name");

        }
    }

    private void parseOpenBracket() throws Exception {
        nextToken();
        switch (activeToken.getToken()) {
            case OPENBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected opening bracket");
        }
    }

    private void parseCloseBracket() throws Exception {
        nextToken();
        switch (activeToken.getToken()) {
            case CLOSEBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected closing bracket");
        }
    }

}
