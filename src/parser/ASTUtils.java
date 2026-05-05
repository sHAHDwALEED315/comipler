package parser;

/**
 * Utility class for AST printing
 */
public class ASTUtils {
    public static String getIndent(int indent) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }
}
