package Parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ConditionTree.Node;
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

        if (!tokenizer.tokenize(query)) {
            error = "ERROR: Invalid token in query";
            return null;
        }

        tokens = tokenizer.tokens;

        try {
            return parseCommand()
        } catch (Exception e) {
            return null;
        }
    }

    private void nextToken() {
        if (tokens.isEmpty()) {
            activeToken = null;
        }

        activeToken = tokens.pop();
    }

    private Expression parseCommand() throws Exception {
        Expression expression = null;
        nextToken();

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
                break;
            case DELETE:
                break;
            case JOIN:
                break;
            default:
                error = "ERROR Unexpected token. Invalid command";
                break;
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
                throw new Exception ("ERROR Must specify what to DROP")
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

        return new Select(attributes, parseFrom());

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
                break;
            case NAME:
                break;
            default:
                break;
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
