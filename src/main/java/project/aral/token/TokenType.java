package project.aral.token;

public enum TokenType {
    KEYWORD,

    IDENTIFIER,
    NUMBER,
    STRING,

    PLUS,               // +
    MINUS,              // -
    MULTIPLY,           // *
    DIVIDE,             // /
    EQUAL,              // =
    EQUAL_EQUAL,        // ==
    GREATER,            // >
    LESS,               //
    GREATER_OR_EQUAL,   // >=
    LESS_OR_EQUAL,      // <=
    NOT_EQUAL,          // !=
    AND,                // &&
    OR,                 // ||

    LEFT_BRACE,         // {
    RIGHT_BRACE,        // }
    LEFT_PAREN,         // (
    RIGHT_PAREN,        // )
    SEMICOLON,          // ;
    COLON,              // :
    COMMA,              // ,

    EOF
}