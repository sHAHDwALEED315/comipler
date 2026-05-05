package lexer;

public enum TokenType {
    // Keywords
    INT,
    FLOAT,
    STRING,
    BOOLEAN,
    BOOL,
    IF,
    WHILE,
    PRINT,

    // Identifiers & literals
    IDENTIFIER,
    NUMBER,

    // Operators
    PLUS, MINUS, STAR, SLASH,
    ASSIGN,
    EQUAL, NOT_EQUAL,
    GREATER, LESS, GREATER_EQUAL, LESS_EQUAL,

    // Symbols
    SEMICOLON,
    LPAREN, RPAREN,
    LBRACE, RBRACE,

    EOF
}