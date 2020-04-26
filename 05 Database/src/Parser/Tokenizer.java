package Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Tokenizer {

    private class Token {
        private Type token;
        private String value;

        public Token(Type token, String value) {
            this.token = token;
            this.value = value;
        }
    }

    private class TokenInfo {
        private Pattern regex;
        private Type type;

        public TokenInfo(Pattern regex, Type type) {
            this.regex = regex;
            this.type = type;
        }
    }

    public enum Type {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN, TABLE, DATABASE, VALUES, FROM, WHERE, SET, AND,
        ON, OPENBRACKET, CLOSEBRACKET, COMMA, OPERATOR, WILD, STRING, BOOLEAN, FLOAT, INTEGER, NAME, ERROR
    };

    List<TokenInfo> tokenSelectors;
    List<Token> tokens;

    public Tokenizer() {
        tokenSelectors = new LinkedList<>();

        addRegex("^(use\s+)", Type.USE);
        addRegex("^(create\s+)", Type.CREATE);
        addRegex("^(drop\s+)", Type.DROP);
        addRegex("^(alter\s+table\s+)", Type.ALTER);
        addRegex("^(insert\s+into\s+)", Type.INSERT);
        addRegex("^(select\s+)", Type.SELECT);
        addRegex("^(update\s+)", Type.UPDATE);
        addRegex("^(delete\s+from\s+)", Type.DELETE);
        addRegex("^(join\s+)", Type.JOIN);
        addRegex("^(table\s+)", Type.TABLE);
        addRegex("^(database\s+)", Type.DATABASE);
        addRegex("^(values\s+)", Type.VALUES);
        addRegex("^(from\s+", Type.FROM);
        addRegex("^(where\s+)", Type.WHERE);
        addRegex("^(set\s+)", Type.SET);
        addRegex("^(and\s+)", Type.AND);
        addRegex("^(on\s+)", Type.ON);

        addRegex("^(\s*\\(\s*)", Type.OPENBRACKET);
        addRegex("^(\s*\\)\s*)", Type.CLOSEBRACKET);
        addRegex("^(\s*,\s*)", Type.COMMA);
        addRegex("^(\s*((==)|(>)|(<)|(>=)|(<=)|(!=)|(\s+LIKE\s+))\s*)", Type.OPERATOR);
        addRegex("^(\s*\\*\s*)", Type.WILD);
        addRegex("^(\s*'.*'\s*)", Type.STRING);// allows empty string
        addRegex("^(\s*((true)|(false))\s*)", Type.BOOLEAN);
        addRegex("^(\s*[0-9]+.[0-9]+\s*)", Type.FLOAT);
        addRegex("^(\s*[0-9]+\s*)", Type.INTEGER); // order is important here
        addRegex("^(\s*[a-zA-Z_]+\s*)", Type.NAME); // disallows digits in name
    }

    private void addRegex(String regex, Type type) {
        tokenSelectors.add(new TokenInfo(Pattern.compile(regex), type));
    }

    public void tokenize(String input) {
        tokens.clear();
        String query = input.trim();

    }

}