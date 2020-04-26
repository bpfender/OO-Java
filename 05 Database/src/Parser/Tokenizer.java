package Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    public class Token {
        public Type token;
        public String value;

        public Token(Type token, String value) {
            this.token = token;
            this.value = value;
        }

        public Type getToken() {
            return token;
        }

        public String getValue() {
            return value;
        }

    }

    private class TokenInfo {
        Pattern regex;
        Type type;

        public TokenInfo(Pattern regex, Type type) {
            this.regex = regex;
            this.type = type;
        }
    }

    public enum Type {
        USE, CREATE, ADD, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN, TABLE, DATABASE, VALUES, FROM, WHERE, SET,
        AND, ON, INTO, OPENBRACKET, CLOSEBRACKET, COMMA, OPERATOR, WILD, LITERAL, STRING, BOOLEAN, FLOAT, INTEGER, NAME,
        ERROR
    };

    List<TokenInfo> tokenSelectors;
    LinkedList<Token> tokens = new LinkedList<>();

    public Tokenizer() {
        tokenSelectors = new LinkedList<>();

        addRegex("^(use\s+)", Type.USE);
        addRegex("^(create\s+)", Type.CREATE);
        addRegex("^(add\s+)", Type.ADD);
        addRegex("^(drop\s+)", Type.DROP);
        addRegex("^(alter\s+)", Type.ALTER);
        addRegex("^(insert\s+)", Type.INSERT);
        addRegex("^(select\s+)", Type.SELECT);
        addRegex("^(update\s+)", Type.UPDATE);
        addRegex("^(delete\s+)", Type.DELETE);
        addRegex("^(join\s+)", Type.JOIN);
        addRegex("^(table\s+)", Type.TABLE);
        addRegex("^(database\s+)", Type.DATABASE);
        addRegex("^(values\s+)", Type.VALUES);
        addRegex("^(from\s+", Type.FROM);
        addRegex("^(where\s+)", Type.WHERE);
        addRegex("^(set\s+)", Type.SET);
        addRegex("^(and\s+)", Type.AND);
        addRegex("^(on\s+)", Type.ON);
        addRegex("^(into\s+)", Type.INTO);

        addRegex("^(\s*\\(\s*)", Type.OPENBRACKET);
        addRegex("^(\s*\\)\s*)", Type.CLOSEBRACKET);
        addRegex("^(\s*,\s*)", Type.COMMA);
        addRegex("^(\s*((==)|(>)|(<)|(>=)|(<=)|(!=)|(LIKE\s+))\s*)", Type.OPERATOR); // QUESTION does like need a \s+
        addRegex("^(\s*\\*\s*)", Type.WILD);
        addRegex("^(\s*'.*'\s*)", Type.LITERAL);// allows empty string
        addRegex("^(\s*((true)|(false))\s*)", Type.LITERAL);
        addRegex("^(\s*[0-9]+.[0-9]+\s*)", Type.LITERAL);
        addRegex("^(\s*[0-9]+\s*)", Type.LITERAL); // order is important here
        addRegex("^(\s*[a-zA-Z_]+\s*)", Type.NAME); // disallows digits in name
    }

    private void addRegex(String regex, Type type) {
        tokenSelectors.add(new TokenInfo(Pattern.compile(regex), type));
    }

    // http://cogitolearning.co.uk/2013/04/writing-a-parser-in-java-the-tokenizer/
    public boolean tokenize(String input) {
        tokens.clear();
        String query = input.trim();

        while (!query.isBlank()) {
            boolean match = false;

            for (TokenInfo info : tokenSelectors) {
                Matcher m = info.regex.matcher(query);
                if (m.find()) {
                    match = true;

                    String description = m.group().trim();
                    tokens.add(new Token(info.type, description));

                    query = m.replaceFirst("");
                    break;
                }

            }

            if (!match) {
                return false;
            }
        }
        return true;

    }

}