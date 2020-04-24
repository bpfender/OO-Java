package Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Expression.*;

public class Parser {
    Stack<String> tokens = new Stack<>();
    String error;

    public Expression parseQuery(String query) {
        if (!query.endsWith(";")) {
            error = "ERROR: Semicolon missing from end of query";
        }

        tokenizeQuery(query);
        return parseCommand();
    }

    private void tokenizeQuery(String query) {
        Matcher m = Pattern.compile("[a-zA-Z_0-9]+|[(,)]|[><=!]+").matcher(query);
        while (m.find()) {
            tokens.push(m.group());
        }
    }

    private Expression parseCommand() {
        String token = tokens.pop();

        if (tokens.empty()) {
            error = "ERROR Expected more arguments";
            return null;
        }

        if (token.equals("use")) {
            parseUse();
        } else if (token.equals("create")) {
            parseCreate();
        } else if (token.equals("drop")) {

        } else if (token.equals("alter")) {

        } else if (token.equals("insert")) {

        } else if (token.equals("select")) {

        } else if (token.equals("update")) {

        } else if (token.equals("delete")) {

        } else if (token.equals("join")) {

        } else {
            error = "ERROR Invalid command";
            return null;
        }

    }

    private Expression parseUse() {
        String token = tokens.pop();

        if (!tokens.empty()) {
            error = "ERROR Unexpected arguments after name";
            return null;
        }
        return new Use(token);
    }

    private Expression parseCreate() {
        String token = tokens.pop();

        if (tokens.empty()) {
            error = "ERROR expected more arguments";
            return null;
        }

        if (token.equals("table")) {
            return parseCreateTable(splitQuery[1]);

        } else if (token.equals("database")) {
            return parseCreateDatabase(splitQuery[1]);
        }

        error = "ERROR Unexpected token";
        return null;
    }

    private Expression parseCreateDatabase(String remainingQuery) {
        String[] splitQuery = remainingQuery.split("\\s", 2);

        if (splitQuery.length > 1) {
            error = "ERROR unexpected arguments at end of query";
            return null;
        }

        return new CreateDatabase(splitQuery[0]);
    }

    private Expression parseCreateTable(String remainingQuery) {
        String[] splitQuery = remainingQuery.split("\\s", 2);
        List<String> attributeList = null;

        if (splitQuery.length > 1) {

        }

        return new CreateTable(splitQuery[0], attributeList);
    }

}