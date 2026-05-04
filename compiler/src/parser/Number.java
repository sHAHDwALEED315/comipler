package parser;

/**
 * Represents a numeric literal
 * Example: 42, 3.14
 */
public class Number implements Expression {
    public String value;

    public Number(String value) {
        this.value = value;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "Number: " + value);
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
