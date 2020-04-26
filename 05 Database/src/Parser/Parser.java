package Parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Expression.*;
import Parser.Tokenizer.Token;
import Parser.Tokenizer.Type;

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

        return parseCommand();
    }

    private void nextToken() {
        if (tokens.isEmpty()) {
            activeToken = null;
        }

        activeToken = tokens.pop();
    }

    private Expression parseCommand() {
        Expression expression = null;
        nextToken();

        switch (activeToken.token) {
            case USE:
                expression = parseUse();
                break;
            case CREATE:
                expresion = parseCreate();
                break;
            case DROP:
                break;
            case ALTER:
                break;
            case INSERT:
                break;
            case SELECT:
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

        return expression;
    }

    private Expression parseUse() {
        nextToken();

        switch (activeToken.token) {
            case NAME:
                return new Use(activeToken.value);
            default:
                error = "ERROR Expected name to be specifed";
                return null;
        }
    }

    private Expression parseCreate() {
        nextToken();


        switch (activeToken.token) {
            case TABLE:
                break;
            case DATABASE:
            default:
                error = "ERROR Must specify whether to create database or table";
                return null;
        }
    }

    private 

}
