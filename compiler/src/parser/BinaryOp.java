package parser;

/**
 * Represents a binary operation (two operands and an operator)
 * Example: 3 + 4
 */
public class BinaryOp implements Expression {
    public Expression left;
    public String operator;
    public Expression right;

    public BinaryOp(Expression left, String operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "BinaryOp: " + operator);
        System.out.println(getIndent(indent + 1) + "Left:");
        left.print(indent + 2);
        System.out.println(getIndent(indent + 1) + "Right:");
        right.print(indent + 2);
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
