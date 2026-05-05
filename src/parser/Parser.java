package parser;

import java.util.List;
import java.util.ArrayList;
import lexer.*;

public class Parser {
    private List<Token> Tokens;
    private int current = 0;
    private Program program;

    public Parser(List<Token> tokens) {
        this.Tokens = tokens;
        this.program = new Program();
    }

    private boolean isType(TokenType type) {
        return type == TokenType.INT ||
                type == TokenType.FLOAT ||
                type == TokenType.STRING ||
                type == TokenType.BOOLEAN ||
                type == TokenType.BOOL;
    }

    public Program Parse() {
        while (!isAtEnd()) {
            if (isType(peek().getType())) {
                program.addStatement(declaration());
            } else {
                ASTNode stmt = Statement();
                if (stmt != null) {
                    program.addStatement(stmt);
                }
            }
        }
        return program;
    }

    private Declaration declaration() {
        String type = advance().getLexeme();
        String name = match(TokenType.IDENTIFIER).getLexeme();
        Expression initializer = null;
        if (check(TokenType.ASSIGN)) {
            advance();
            initializer = Expression();
        }
        match(TokenType.SEMICOLON);
        return new Declaration(type, name, initializer);
    }

    /*
     * public void Parse() {
     * while (!isAtEnd()) {
     * Statement();
     * } }
     */

    private boolean check(TokenType type) {
        return peek().getType() == type;
    }

    private List<ASTNode> block() {
        match(TokenType.LBRACE);
        List<ASTNode> statements = new ArrayList<>();
        while (!check(TokenType.RBRACE)) {
            ASTNode stmt = Statement();
            if (stmt != null) {
                statements.add(stmt);
            }
        }
        match(TokenType.RBRACE);
        return statements;
    }

    private IfStatement ifStatement() {
        match(TokenType.IF);
        match(TokenType.LPAREN);
        Expression condition = Expression();
        match(TokenType.RPAREN);

        IfStatement ifStmt = new IfStatement(condition);
        List<ASTNode> stmts = block();
        for (ASTNode stmt : stmts) {
            ifStmt.addStatement(stmt);
        }
        return ifStmt;
    }

    private WhileStatement whileStatement() {
        match(TokenType.WHILE);
        match(TokenType.LPAREN);
        Expression condition = Expression();
        match(TokenType.RPAREN);

        WhileStatement whileStmt = new WhileStatement(condition);
        List<ASTNode> stmts = block();
        for (ASTNode stmt : stmts) {
            whileStmt.addStatement(stmt);
        }
        return whileStmt;
    }

    private ASTNode Statement() {
        if (peek().getType() == TokenType.PRINT) {
            return printStatement();
        } else if (peek().getType() == TokenType.IDENTIFIER &&
                peekNext().getType() == TokenType.ASSIGN) {
            // assignment
            String varName = advance().getLexeme();
            advance(); // consume =
            Expression expr = Expression();
            match(TokenType.SEMICOLON);
            return new Assignment(varName, expr);
        } else if (peek().getType() == TokenType.LBRACE) {
            return new Block(block());
        } else if (peek().getType() == TokenType.IF) {
            return ifStatement();
        } else if (peek().getType() == TokenType.WHILE) {
            return whileStatement();
        } else {
            Expression expr = Expression();
            match(TokenType.SEMICOLON);
            return expr;
        }
    }

    /*
     * private void Statement() {
     * if (peek().getType() == TokenType.PRINT) {
     * PrintStatement();
     * } else if (peek().getType() == TokenType.IDENTIFIER && peekNext().getType()
     * == TokenType.ASSIGN) {
     * // x = expr;
     * Token name = advance(); // consume identifier
     * advance(); // consume '='
     * Expr value = Expression();
     * match(TokenType.SEMICOLON);
     * } else if (isKeyword(peek().getType())) {
     * advance();
     * Token name = advance();
     * match(TokenType.ASSIGN);
     * Expr value = Expression();
     * match(TokenType.SEMICOLON);
     * } else {
     * Expression();
     * match(TokenType.SEMICOLON);
     * }
     * }
     * 
     * private boolean isKeyword(TokenType type) {
     * return type == TokenType.INT ||
     * type == TokenType.FLOAT ||
     * type == TokenType.STRING ||
     * type == TokenType.BOOLEAN ||
     * type == TokenType.BOOL;
     * }
     */

    private Token peekNext() {
        if (current + 1 >= Tokens.size())
            return Tokens.get(Tokens.size() - 1);
        return Tokens.get(current + 1);
    }

    private PrintStatement printStatement() {
        advance(); // consume print
        match(TokenType.LPAREN);
        Expression expr = Expression();
        match(TokenType.RPAREN);
        match(TokenType.SEMICOLON);
        return new PrintStatement(expr);
    }

    private Token match(TokenType type) {
        if (peek().getType() == type) {
            return advance();
        } else {
            throw new RuntimeException("Expected " + type + " but got " + peek().getType());
        }
    }

    public Expression Expression() {
        return Equality();
    }

    public Expression Equality() {
        Expression expr = Comparison();
        while (peek().getType() == TokenType.EQUAL ||
                peek().getType() == TokenType.NOT_EQUAL) {
            Token operator = advance();
            Expression right = Comparison();
            expr = new BinaryOp(expr, operator.getLexeme(), right);
        }
        return expr;
    }

    public Expression Comparison() {
        Expression expr = Term();
        while (peek().getType() == TokenType.LESS ||
                peek().getType() == TokenType.GREATER
                || peek().getType() == TokenType.LESS_EQUAL
                || peek().getType() == TokenType.GREATER_EQUAL) {
            Token operator = advance();
            Expression right = Term();
            expr = new BinaryOp(expr, operator.getLexeme(), right);
        }
        return expr;
    }

    public Expression Term() {
        Expression expr = Factor();
        while (peek().getType() == TokenType.PLUS ||
                peek().getType() == TokenType.MINUS) {
            Token operator = advance();
            Expression right = Factor();
            expr = new BinaryOp(expr, operator.getLexeme(), right);
        }
        return expr;
    }

    public Expression Factor() {
        Expression expr = Unary();
        while (peek().getType() == TokenType.STAR ||
                peek().getType() == TokenType.SLASH) {
            Token operator = advance();
            Expression right = Unary();
            expr = new BinaryOp(expr, operator.getLexeme(), right);
        }
        return expr;
    }

    private Expression Unary() {
        if (peek().getType() == TokenType.NUMBER) {
            Token number = advance();
            return new parser.Number(number.getLexeme());
        }

        if (peek().getType() == TokenType.IDENTIFIER) {
            Token id = advance();
            return new Variable(id.getLexeme());
        }

        if (peek().getType() == TokenType.LPAREN) {
            advance();
            Expression expr = Expression();

            if (peek().getType() != TokenType.RPAREN) {
                throw new RuntimeException("Expected ')'");
            }

            advance();
            return expr;
        }
        throw new RuntimeException("Unexpected token: " + peek());
    }

    private Token peek() {
        return Tokens.get(current);
    }

    private Token advance() {
        if (!isAtEnd())
            current++;
        return Tokens.get(current - 1);
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }
}
