package parser;

/**
 * Represents a variable reference
 * Example: x
 */
public class Variable implements Expression {
    public String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "Variable: " + name);
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
