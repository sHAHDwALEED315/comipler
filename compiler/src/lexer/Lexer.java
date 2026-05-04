package lexer;

public class Lexer {

    private String source;
    private int position = 0;
    private int line = 1;

    public Lexer(String source) {
        this.source = source;
    }

    public char peek() {
        if (position >= source.length())
            return '\0';
        return source.charAt(position);
    }

    public char advance() {
        return source.charAt(position++);
    }

    public boolean isAtEnd() {
        return position >= source.length();
    }

    public Token nextToken() {
        skipWhitespace();

        if (isAtEnd()) {
            return new Token(TokenType.EOF, "", line);
        }

        char current = advance();

        if (Character.isLetter(current)) {
            return identifier(current);
        }

        if (Character.isDigit(current)) {
            return number(current);
        }

        switch (current) {
            case '+':
                return new Token(TokenType.PLUS, "+", line);
            case '-':
                return new Token(TokenType.MINUS, "-", line);
            case '*':
                return new Token(TokenType.STAR, "*", line);
            case '/':
                return new Token(TokenType.SLASH, "/", line);
            case ';':
                return new Token(TokenType.SEMICOLON, ";", line);
            case '(':
                return new Token(TokenType.LPAREN, "(", line);
            case ')':
                return new Token(TokenType.RPAREN, ")", line);
            case '{':
                return new Token(TokenType.LBRACE, "{", line);
            case '}':
                return new Token(TokenType.RBRACE, "}", line);

            case '=':
                if (peek() == '=') {
                    advance();
                    return new Token(TokenType.EQUAL, "==", line);
                }
                return new Token(TokenType.ASSIGN, "=", line);

            case '!':
                if (peek() == '=') {
                    advance();
                    return new Token(TokenType.NOT_EQUAL, "!=", line);
                }
                break;

            case '>':
                if (peek() == '=') {
                    advance();
                    return new Token(TokenType.GREATER_EQUAL, ">=", line);
                }
                return new Token(TokenType.GREATER, ">", line);

            case '<':
                if (peek() == '=') {
                    advance();
                    return new Token(TokenType.LESS_EQUAL, "<=", line);
                }
                return new Token(TokenType.LESS, "<", line);
        }

        throw new RuntimeException("Unexpected character: " + current);
    }

    private void skipWhitespace() {
        while (!isAtEnd()) {
            char c = peek();
            if (c == ' ' || c == '\r' || c == '\t') {
                advance();
            } else if (c == '\n') {
                line++;
                advance();
            } else {
                break;
            }
        }
    }

    private Token identifier(char firstChar) {
        StringBuilder builder = new StringBuilder();
        builder.append(firstChar);

        while (Character.isLetterOrDigit(peek())) {
            builder.append(advance());
        }

        String lexeme = builder.toString();

        if (lexeme.equals("int"))
            return new Token(TokenType.INT, lexeme, line);
        if (lexeme.equals("float"))
            return new Token(TokenType.FLOAT, lexeme, line);  
        if (lexeme.equals("boolean"))
            return new Token(TokenType.BOOLEAN, lexeme, line);  
        if (lexeme.equals("string"))
            return new Token(TokenType.STRING, lexeme, line); 
        if (lexeme.equals("bool"))
            return new Token(TokenType.BOOL, lexeme, line);
        if (lexeme.equals("if"))
            return new Token(TokenType.IF, lexeme, line);
        if (lexeme.equals("while"))
            return new Token(TokenType.WHILE, lexeme, line);
        if (lexeme.equals("print"))
            return new Token(TokenType.PRINT, lexeme, line);

        return new Token(TokenType.IDENTIFIER, lexeme, line);
    }

    private Token number(char firstChar) {
        StringBuilder builder = new StringBuilder();
        builder.append(firstChar);

        while (Character.isDigit(peek())) {
            builder.append(advance());
        }

        return new Token(TokenType.NUMBER, builder.toString(), line);
    }
}