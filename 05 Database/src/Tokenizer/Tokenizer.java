package Tokenizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    // TokenInfo is declared as a private class because it will only ever be used by
    // the Tokenizer class. It is used to store the regex pattern and the
    // corresponding token type, which is in turn used to build the Token stack.
    private class TokenInfo {
        protected Pattern regex;
        protected TokenType type;

        public TokenInfo(Pattern regex, TokenType type) {
            this.regex = regex;
            this.type = type;
        }
    }

    private ArrayList<TokenInfo> tokenSelectors = new ArrayList<>();

    // TODO: could this be defined as a queue?
    // The tokens queue is used to build the list of tokens during tokenization and
    // to return the next token during parsing
    private LinkedList<Token> tokenQueue = new LinkedList<>();

    // The constructor adds each regex to the tokenSelector array. Order matters,
    // with first entries added being matched before later entries. As such,
    // reserved keywords are matched first, followed by operators, then punctuation
    // and finally literal values. Regexes exclude whitespace automatically. For
    // words, there must be at elast one space to seperate it from the next token,
    // operators are able to deal with no spaces
    public Tokenizer() {
        // Command Tokens (reserved keywords)
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

        // Other reserved keywords
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

        // Operators (order is important, multiple character operators are checked
        // first)
        addRegex("^(\\s*==\\s*)", TokenType.EQUAL);
        addRegex("^(\\s*>=\\s*)", TokenType.GREATEREQUAL);
        addRegex("^(\\s*<=\\s*)", TokenType.LESSEQUAL);
        addRegex("^(\\s*!=\\s*)", TokenType.NOTEQUAL);
        addRegex("^(\\s*>\\s*)", TokenType.GREATER);
        addRegex("^(\\s*<\\s*)", TokenType.LESS);
        addRegex("^(\\s*LIKE\\s+)", TokenType.LIKE);

        // Punctuation
        addRegex("^(\\s*\\(\\s*)", TokenType.OPENBRACKET);
        addRegex("^(\\s*\\)\\s*)", TokenType.CLOSEBRACKET);
        addRegex("^(\\s*,\\s*)", TokenType.COMMA);
        addRegex("^(\\s*=\\s*)", TokenType.PAIR);

        // Values
        addRegex("^(\\s*\\*\\s*)", TokenType.WILD);
        addRegex("^(\\s*'[A-Za-z_ 0-9.]*'\\s*)", TokenType.LITERAL);// QUESTION make sure this matches everything
        addRegex("^(\\s*((true)|(false))\\s*)", TokenType.LITERAL);
        addRegex("^(\\s*[0-9]+.[0-9]+\\s*)", TokenType.LITERAL);
        addRegex("^(\\s*[0-9]+\\s*)", TokenType.LITERAL); // order is important here
        addRegex("^(\\s*[a-zA-Z_]+\\s*)", TokenType.NAME); // disallows digits in name
    }

    // Compiles a case insensitive pattern matcher, which gets added to a new
    // TokenInfo class and in turn added to the tokenSelectors list. This allows
    // later matching of the string against the pattern
    private void addRegex(String regex, TokenType type) {
        tokenSelectors.add(new TokenInfo(Pattern.compile(regex, Pattern.CASE_INSENSITIVE), type));
    }

    // Source:
    // http://cogitolearning.co.uk/2013/04/writing-a-parser-in-java-the-tokenizer/
    // Tokenize first ensures that the tokenQueue is cleared of previous tokens. A
    // while loop scans the input for tokens until no matching token can be found or
    // the string is empty. The begining of the string is scanned, if a matching
    // token is found, the token and value are added to the tokenQueue and that part
    // of the input is removed. This is repeated until the input is consumed. If no
    // token is matched, an error is thrown
    public LinkedList<Token> tokenize(String input) throws Exception {
        tokenQueue.clear();
        // TODO Streamify?
        // TODO is this actually needed
        // TODO .isBlank vs .isEmpty?
        // TODO Remove nesting
        String query = input.trim();
        while (!query.isBlank()) {
            boolean tokenMatch = false;

            for (TokenInfo tokenInfo : tokenSelectors) {
                Matcher m = tokenInfo.regex.matcher(query);

                if (m.find()) {
                    tokenMatch = true;

                    // TODO slightly neater string sanitisation
                    String description = m.group().trim().replaceAll("'", "");

                    tokenQueue.add(new Token(tokenInfo.type, description));

                    query = m.replaceFirst("");
                    break;
                }

            }

            if (!tokenMatch) {
                throw new Exception("ERROR Invalid input in region '" + query + "'");
            }
        }

        return tokenQueue;
    }

}