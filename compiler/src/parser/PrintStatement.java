package parser;

/**
 * Represents a print statement
 * Example: print(x);
 */
public class PrintStatement implements ASTNode {
    public Expression expression;

    public PrintStatement(Expression expression) {
        this.expression = expression;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "PrintStatement:");
        expression.print(indent + 1);
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
