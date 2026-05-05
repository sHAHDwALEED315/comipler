package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the entire program
 */
public class Program implements ASTNode {
    public List<ASTNode> statements;

    public Program() {
        this.statements = new ArrayList<>();
    }

    public void addStatement(ASTNode stmt) {
        statements.add(stmt);
    }

    @Override
    public void print(int indent) {
        System.out.println(getIndent(indent) + "Program");
        for (ASTNode stmt : statements) {
            stmt.print(indent + 1);
        }
    }

    private String getIndent(int indent) {
        return ASTUtils.getIndent(indent);
    }
}
