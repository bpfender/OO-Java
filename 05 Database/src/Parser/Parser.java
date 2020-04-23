package Parser;

import Expression.*;

public class Parser {
    String query;

    public Parser(String query) {
        this.query = query.trim();
    }

    public String parseQuery() {
        if (!checkTerminator()) {
            return "ERROR: Semicolon missing from end of query";
        }

        return commandType();
    }

    private boolean checkTerminator() {
        if (query.endsWith(";")) {
            query = query.substring(0, query.length() - 1);
            return true;
        }

        return false;
    }

    private String commandType() {
        String[] splitQuery = query.split("\\s", 2);

        if (splitQuery[0].equals("use")) {

        } else if (splitQuery[0].equals("create")) {

        } else if (splitQuery[0].equals("drop")) {

        } else if (splitQuery[0].equals("alter")) {

        } else if (splitQuery[0].equals("insert")) {

        } else if (splitQuery[0].equals("select")) {

        } else if (splitQuery[0].equals("update")) {

        } else if (splitQuery[0].equals("delete")) {

        } else if (splitQuery[0].equals("join")) {

        } else {
            return "ERROR";
        }
        return null;
    }

}