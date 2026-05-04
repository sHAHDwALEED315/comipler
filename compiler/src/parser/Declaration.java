package parser;

/**
 * Represents a variable declaration
 * Example: int x;
 */
public class Declaration implements ASTNode {
    public String type;
    public String name;
    public Expression initializer;

    public Declaration(String type, String name) {
        this(type, name, null);
    }

    public Declaration(String type, String name, Expression initializer) {
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "Declaration: " + type + " " + name);
        if (initializer != null) {
            System.out.println(getIndent(indent + 1) + "Initializer:");
            initializer.print(indent + 2);
        }
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
