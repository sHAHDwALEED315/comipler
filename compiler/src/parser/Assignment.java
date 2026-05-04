package parser;

/**
 * Represents an assignment statement
 * Example: x = 5;
 */
public class Assignment implements ASTNode {
    public String variable;
    public Expression value;

    public Assignment(String variable, Expression value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "Assignment: " + variable + " =");
        value.print(indent + 1);
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
