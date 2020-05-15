package Tokenizer;

// This class defines all tokens that might occur in a query.This allows definition of tokens 
// for when the query is parsed.It also defines an END token which gets returned automatically 
// when the end of the query is reached.
public enum TokenType {
    USE, CREATE, ADD, DROP, ALTER, INSERT, SELECT, UPDATE, DELETE, JOIN, TABLE, DATABASE, VALUES, FROM, WHERE, SET, AND,
    OR, ON, INTO, OPENBRACKET, CLOSEBRACKET, COMMA, PAIR, OPERATOR, WILD, LITERAL, NAME, ERROR, EQUAL, GREATER, LESS,
    GREATEREQUAL, LESSEQUAL, NOTEQUAL, LIKE, END
};