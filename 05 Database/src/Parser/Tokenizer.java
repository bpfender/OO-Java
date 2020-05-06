package Parser;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    // TokenInfo is declared as a private class because it will only ever be used by
    // the Tokenizer class. It is used to store the regex pattern and the
    // corresponding token type, which is in turn used to build the Token stack.
    private class TokenInfo {
        Pattern regex;
        TokenType type;

        public TokenInfo(Pattern regex, TokenType type) {
            this.regex = regex;
            this.type = type;
        }
    }

    List<TokenInfo> tokenSelectors;
    LinkedList<Token> tokens = new LinkedList<>();

    public Tokenizer() {
        tokenSelectors = new LinkedList<>();

        // COMMANDS
        addRegex("^(use\\s+)", TokenType.USE);
        addRegex("^(create\\s+)", TokenType.CREATE);
        addRegex("^(add\\s+)", TokenType.ADD);
        addRegex("^(drop\\s+)", TokenType.DROP);
        addRegex("^(alter\\s+)", TokenType.ALTER);
        addRegex("^(insert\\s+)", TokenType.INSERT);
        addRegex("^(select\\s+)", TokenType.SELECT);
        addRegex("^(update\\s+)", TokenType.UPDATE);
        addRegex("^(delete\\s+)", TokenType.DELETE);
        addRegex("^(join\\s+)", TokenType.JOIN);

        // OPTHER WORDS
        addRegex("^(table\\s+)", TokenType.TABLE);
        addRegex("^(database\\s+)", TokenType.DATABASE);
        addRegex("^(values\\s+)", TokenType.VALUES);
        addRegex("^(from\\s+)", TokenType.FROM);
        addRegex("^(where\\s+)", TokenType.WHERE);
        addRegex("^(set\\s+)", TokenType.SET);
        addRegex("^(and\\s+)", TokenType.AND);
        addRegex("^(or\\s+)", TokenType.OR);
        addRegex("^(on\\s+)", TokenType.ON);
        addRegex("^(into\\s+)", TokenType.INTO);

        // OPERATORS
        // order is important
        addRegex("^(\\s*==\\s*)", TokenType.EQUAL); // QUESTION does like need a \s+
        addRegex("^(\\s*>=\\s*)", TokenType.GREATEREQUAL);
        addRegex("^(\\s*<=\\s*)", TokenType.LESSEQUAL);
        addRegex("^(\\s*!=\\s*)", TokenType.NOTEQUAL);
        addRegex("^(\\s*>\\s*)", TokenType.GREATER);
        addRegex("^(\\s*<\\s*)", TokenType.LESS);
        addRegex("^(\\s*LIKE\\s+)", TokenType.LIKE);

        // PUCNCTUATION
        addRegex("^(\\s*\\(\\s*)", TokenType.OPENBRACKET);
        addRegex("^(\\s*\\)\\s*)", TokenType.CLOSEBRACKET);
        addRegex("^(\\s*,\\s*)", TokenType.COMMA);
        addRegex("^(\\s*=\\s*)", TokenType.PAIR);

        // addRegex("^(\s*((==)|(>)|(<)|(>=)|(<=)|(!=)|(LIKE\s+))\s*)",
        // TokenType.OPERATOR);

        // VALUES
        addRegex("^(\\s*\\*\\s*)", TokenType.WILD);

        addRegex("^(\\s*'[A-Za-z_ 0-9.]*'\\s*)", TokenType.LITERAL);// QUESTION make sure this matches everything
        addRegex("^(\\s*((true)|(false))\\s*)", TokenType.LITERAL);
        addRegex("^(\\s*[0-9]+.[0-9]+\\s*)", TokenType.LITERAL);
        addRegex("^(\\s*[0-9]+\\s*)", TokenType.LITERAL); // order is important here
        addRegex("^(\\s*[a-zA-Z_]+\\s*)", TokenType.NAME); // disallows digits in name
    }

    private void addRegex(String regex, TokenType type) {
        tokenSelectors.add(new TokenInfo(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), type));
    }

    // http://cogitolearning.co.uk/2013/04/writing-a-parser-in-java-the-tokenizer/
    public boolean tokenize(String input) {
        tokens.clear();
        String query = input.trim();
        System.out.println("QUERY:" + query);

        while (!query.isBlank()) {
            boolean match = false;

            for (TokenInfo info : tokenSelectors) {
                Matcher m = info.regex.matcher(query);
                if (m.find()) {
                    match = true;
                    String description = m.group().trim().replaceAll("'", ""); // FIXME super dirty
                    System.out.println("MATCH " + description + " " + info.type);
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