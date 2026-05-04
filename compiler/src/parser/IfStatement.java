package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an if statement
 */
public class IfStatement implements ASTNode {
    public Expression condition;
    public List<ASTNode> thenBranch;

    public IfStatement(Expression condition) {
        this.condition = condition;
        this.thenBranch = new ArrayList<>();
    }

    public void addStatement(ASTNode stmt) {
        thenBranch.add(stmt);
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "IfStatement:");
        System.out.println(getIndent(indent + 1) + "Condition:");
        condition.print(indent + 2);
        System.out.println(getIndent(indent + 1) + "Body:");
        for (ASTNode stmt : thenBranch) {
            stmt.print(indent + 2);
        }
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
