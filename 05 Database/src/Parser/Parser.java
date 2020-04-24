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
        if (tokens.empty()) {
            error = "ERROR Require query arguments";
            return null;
        }

        return parseCommand();
    }

    private void tokenizeQuery(String query) {
        Matcher m = Pattern.compile("[a-zA-Z_0-9]+|[(,)]|[><=!]+|[*]").matcher(query);
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
            return parseUse();
        } else if (token.equals("create")) {
            return parseCreate();
        } else if (token.equals("drop")) {
            return parseDrop();
        } else if (token.equals("alter")) {
            return parseAlter();
        } else if (token.equals("insert")) {
            return parseInsert();
        } else if (token.equals("select")) {
            return parseSelect();
        } else if (token.equals("update")) {

        } else if (token.equals("delete")) {

        } else if (token.equals("join")) {

        } else {
            error = "ERROR Invalid command " + token;
            return null;
        }
        return null;
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

        if (token.equals("table")) {
            return parseCreateTable();
        } else if (token.equals("database")) {
            return parseCreateDatabase();
        } else {
            error = "ERROR Unexpected token";
            return null;
        }
    }

    private Expression parseCreateDatabase() {
        if (tokens.empty()) {
            error = "ERROR Must specify name";
            return null;
        }

        String token = tokens.pop();

        if (!tokens.empty()) {
            error = "ERROR Unexpected arguments at end of query";
            return null;
        }

        return new CreateDatabase(token);
    }

    private Expression parseCreateTable() {
        if (tokens.empty()) {
            error = "ERROR Must specify name";
            return null;
        }

        String token = tokens.pop();

        if (tokens.empty()) {
            return new CreateTable(token, null);
        }

        if (tokens.pop().equals("(")) {
            List<String> attributes = parseAttributeList();
            if (!tokens.empty()) {
                error = "ERROR Unexpected tokens after attribute list";
                return null;
            }
            return new CreateTable(token, attributes);

        } else {
            error = "ERROR Expected (<attributeList>)";
            return null;
        }

    }

    private List<String> parseAttributeList() {
        List<String> attributes = new ArrayList<>();

        while (!tokens.empty()) {
            String token = tokens.pop();

            if (token.equals(")") || token.equals(",")) {
                error = "ERROR Must specify values";
                return null;
            }

            attributes.add(token);
            if (!tokens.empty()) {
                token = tokens.pop();
                if (token.equals(")")) {
                    return attributes;
                }
            }

        }
        error = "ERROR Expected closing )";
        return null;

    }

    private Expression parseDrop() {
        String token = tokens.pop();

        String name = parseName();
        if (!tokens.empty()) {
            error = "ERROR more tokens than expected";
            return null;
        }

        if (token.equals("database")) {
            if (name != null) {
                return new DropDatabase(name);
            }
        } else if (token.equals("table")) {
            if (name != null) {
                return new DropTable(name);
            }
        } else {
            error = "ERROR Unexpected token";
            return null;
        }

        return null;
    }

    private String parseName() {
        if (tokens.empty()) {
            error = "ERROR No name specified";
            return null;
        }

        String name = tokens.pop();
        return name;
    }

    private Expression parseAlter() {
        String token = tokens.pop();

        if (!token.equals("table")) {
            error = "ERROR Expected TABLE argument";
            return null;
        }

        String name = parseName();
        if (name == null) {
            return null;
        }

        Expression alterationType = parseAlterationType();
        if (alterationType != null) {
            return new Alter(name, alterationType);
        }

        return null;

    }

    private Expression parseAlterationType() {
        if (tokens.empty()) {
            error = "ERROR Expected more tokens";
            return null;
        }

        String name = parseName();
        if (!tokens.empty()) {
            error = "ERROR more tokens than expected";
            return null;
        }

        String token = tokens.pop();
        if (token.equals("add")) {
            if (name != null) {
                return new Add(name);
            }
        } else if (token.equals("drop")) {
            if (name != null) {
                return new Drop(name);
            }
        } else {
            error = "ERROR Unexpected token";
            return null;
        }

        return null;
    }

    private Expression parseInsert() {
        String token = tokens.pop();
        if (!token.equals("into")) {
            error = "ERROR Expected INTO";
            return null;
        }

        String name = parseName();

        if (name != null) {
            List<String> values = parseAttributeList();
            if (values != null) {
                return new Insert(name, values);
            }
        }

        return null;
    }

    private Expression parseSelect() {

        return null;
    }
}