package Parser;

public class Token {
    public enum Type {
        USE, CREATE, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN, TABLE, DATABASE, BRACKET, COMMA, OPERATOR, ERROR
    };

    Type tokenType;
    String value;

    public Token(String value) {
        this.value = value;
        setTokenType();
    }

    private void setTokenType() {
        if (value.equals("use")) {
            tokenType = Type.USE;
        }
        if (value.equals("create")) {
            tokenType = Type.CREATE;
        }
        if (value.equals("drop")) {
            tokenType = Type.DROP;
        }
        if (value.equals("alter")) {
            tokenType = Type.ALTER;
        }
        if (value.equals("insert")) {
            tokenType = Type.INSERT;
        }
        if (value.equals("select")) {
            tokenType = Type.SELECT;
        }
        if (value.equals("update")) {
            tokenType = Type.UPDATE;
        }
        if (value.equals("delete")) {
            tokenType = Type.DELETE;
        }
        if (value.equals("join")) {
            tokenType = Type.JOIN;
        }
        if (value.equals("table")) {
            tokenType = Type.TABLE;
        }
        if (value.equals("database")) {
            tokenType = Type.DATABASE;
        }
        if (value.equals("(") || value.equals(")")) {
            tokenType = Type.BRACKET;
        }
        if (value.equals(",")) {
            tokenType = Type.COMMA;
        }
        if (value.equals("=") || value.equals("==") || value.equals(">") || value.equals("<") || value.equals(">=")
                || value.equals("<=") || value.equals("!=") || value.equals("LIKE")) {
            tokenType = Type.OPERATOR;
        }

    }

    public Type getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }
}