package Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ConditionTree.*;
import ConditionTree.AndNode;
import ConditionTree.Node;
import ConditionTree.OperatorNode;
import Expression.*;
import Parser.Tokenizer.Token;
import Parser.Tokenizer.Type;

//TODO error should be thrown!!

public class Parser {
    Tokenizer tokenizer = new Tokenizer();
    LinkedList<Token> tokens;
    Token activeToken;

    String query;
    String error;

    public Expression parseQuery(String input) {
        query = input.trim();

        if (!query.endsWith(";")) {
            error = "ERROR: Semicolon missing from end of query";
            return null;
        }
        query = query.substring(0, query.length() - 1);

        if (!tokenizer.tokenize(query)) {
            error = "ERROR: Invalid token in query";
            return null;
        }

        tokens = tokenizer.tokens;

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

    private void nextToken() {
        if (tokens.isEmpty()) {
            activeToken.token = Type.END;
            activeToken.value = null;
        } else {
            activeToken = tokens.pop();
        }
    }

    private Expression parseCommand() throws Exception {
        Expression expression = null;
        nextToken();

        System.out.println("COMMAND: " + activeToken.value);
        System.out.println("TOKEN: " + activeToken.token);

        switch (activeToken.token) {
            case USE:
                expression = parseUse();
                break;
            case CREATE:
                expression = parseCreate();
                break;
            case DROP:
                expression = parseDrop();
                break;
            case ALTER:
                expression = parseAlter();
                break;
            case INSERT:
                expression = parseInsert();
                break;
            case SELECT:
                expression = parseSelect();
                break;
            case UPDATE:
                expression = parseUpdate();
                break;
            case DELETE:
                expression = parseDelete();
                break;
            case JOIN:
                expression = parseJoin();
                break;
            default:
                throw new Exception("ERROR Unexpected token. Invalid command");
        }
        // TODO check no tokens left
        return expression;
    }

    private Expression parseUse() throws Exception {
        return new Use(parseName());
    }

    private Expression parseCreate() throws Exception {
        nextToken();

        switch (activeToken.token) {
            case TABLE:
                String name = parseName();
                if (tokens.isEmpty()) {
                    return new CreateTable(name, null);
                } else {
                    parseOpenBracket();
                    List<String> attributes = parseList(Type.NAME);
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

        switch (activeToken.token) {
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
        if (activeToken.token != Type.TABLE) {
            throw new Exception("ERROR Expected TABLE token");
        }

        String name = parseName();
        Expression alterType;

        nextToken();
        switch (activeToken.token) {
            case ADD:
                alterType = new Add(parseName());
                break;
            case DROP:
                alterType = new Drop(parseName());
                break;
            default:
                throw new Exception("ERROR Expected ADD or DROP");
        }

        return new Alter(name, alterType);
    }

    private Expression parseInsert() throws Exception {
        nextToken();
        if (activeToken.token != Type.INTO) {
            throw new Exception("ERROR Expected INTO");
        }

        String name = parseName();

        nextToken();
        if (activeToken.token != Type.VALUES) {
            throw new Exception("ERROR Expected VALUES token");
        }

        parseOpenBracket();
        List<String> attributes = parseList(Type.LITERAL);
        parseCloseBracket();

        return new Insert(name, attributes);
    }

    private Expression parseSelect() throws Exception {
        nextToken();

        List<String> attributes = new ArrayList<>();

        switch (activeToken.token) {
            case WILD:
                attributes.add("*");
                break;
            case OPENBRACKET:
                attributes = parseList(Type.NAME);
                parseCloseBracket();
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
        return parseFrom();
        // TODO this doesn't handle variable where at the moment.
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
        switch (activeToken.token) {
            case AND:
                break;
            default:
                throw new Exception("ERROR Expected AND");
        }
    }

    private void parseOn() throws Exception {
        nextToken();
        switch (activeToken.token) {
            case OR:
                break;
            default:
                throw new Exception("ERROR Expected OR");
        }

    }

    private Expression parseSet() throws Exception {
        nextToken();

        switch (activeToken.token) {
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

        while (tokens.peek().token == Type.COMMA) {
            nextToken();
            parseNameValuePair(nameValuePairs);
        }

        return nameValuePairs;
    }

    private void parseNameValuePair(HashMap<String, String> nameValuePairs) throws Exception {
        String name = parseName();

        nextToken();
        switch (activeToken.token) {
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
        switch (activeToken.token) {
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
        switch (activeToken.token) {
            case WHERE:
                return new Where(parseCondition());
            default:
                return null;
        }
    }

    private Node parseCondition() throws Exception {
        nextToken();

        switch (activeToken.token) {
            case OPENBRACKET:
                Node condition;
                Node leftNode = parseCondition();
                parseCloseBracket();

                nextToken();
                switch (activeToken.token) {
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
                String name = activeToken.value;
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

        switch (activeToken.token) {
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
        switch (activeToken.token) {
            case LITERAL:
                return activeToken.value;
            default:
                throw new Exception("ERROR Expected literal value");
        }
    }

    private List<String> parseList(Type type) throws Exception {
        List<String> attributes = new ArrayList<>();

        nextToken();

        while (activeToken.token == type) {
            attributes.add(activeToken.value);

            if (tokens.peek().token != Type.COMMA) {
                return attributes;
            }
            nextToken(); // consume comma
            nextToken(); // consume attirbute
        }

        throw new Exception("ERROR Unexprected value in list");

    }

    private String parseName() throws Exception {
        nextToken();

        switch (activeToken.token) {
            case NAME:
                return activeToken.value;
            default:
                throw new Exception("ERROR Must specify name");

        }
    }

    private void parseOpenBracket() throws Exception {
        nextToken();
        switch (activeToken.token) {
            case OPENBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected opening bracket");
        }
    }

    private void parseCloseBracket() throws Exception {
        nextToken();
        switch (activeToken.token) {
            case CLOSEBRACKET:
                return;
            default:
                throw new Exception("ERROR Expected closing bracket");
        }
    }

}
