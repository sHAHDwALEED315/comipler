package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a statement block: { ... }
 */
public class Block implements ASTNode {
    public List<ASTNode> statements;

    public Block(List<ASTNode> statements) {
        this.statements = new ArrayList<>(statements);
    }

    @Override
    public void print(int indent) {
        System.out.println(ASTUtils.getIndent(indent) + "Block");
        for (ASTNode stmt : statements) {
            stmt.print(indent + 1);
        }
    }
}
