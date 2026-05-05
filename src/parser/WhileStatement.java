package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a while loop
 */
public class WhileStatement implements ASTNode {
    public Expression condition;
    public List<ASTNode> body;

    public WhileStatement(Expression condition) {
        this.condition = condition;
        this.body = new ArrayList<>();
    }

    public void addStatement(ASTNode stmt) {
        body.add(stmt);
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "WhileStatement:");
        System.out.println(getIndent(indent + 1) + "Condition:");
        condition.print(indent + 2);
        System.out.println(getIndent(indent + 1) + "Body:");
        for (ASTNode stmt : body) {
            stmt.print(indent + 2);
        }
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
