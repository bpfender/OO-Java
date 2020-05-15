package Tokenizer;

// This class is defined to hold a token extracted from the query. It holds the TokenType 
// (defined as an enum) and the value of the token (relevant for names and values)
public class Token {
    private TokenType token;
    private String value;

    public Token(TokenType token, String value) {
        this.token = token;
        this.value = value;
    }

    public TokenType getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

}