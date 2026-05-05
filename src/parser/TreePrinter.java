package parser;

import java.util.ArrayList;
import java.util.List;

/**
 * Prints the AST as a visual tree.
 */
public class TreePrinter {
    private static final int GAP = 4;

    public static void printTree(ASTNode node) {
        if (node == null) {
            return;
        }

        Drawing drawing = draw(toPrintableNode(node));
        for (String line : drawing.lines) {
            System.out.println(trimRight(line));
        }
    }

    private static Drawing draw(PrintableNode node) {
        if (node.children.isEmpty()) {
            List<String> lines = new ArrayList<>();
            lines.add(node.label);
            return new Drawing(lines, node.label.length(), node.label.length() / 2);
        }

        List<Drawing> children = new ArrayList<>();
        int childrenWidth = 0;
        int maxChildHeight = 0;

        for (int i = 0; i < node.children.size(); i++) {
            Drawing child = draw(node.children.get(i));
            children.add(child);
            childrenWidth += child.width;
            maxChildHeight = Math.max(maxChildHeight, child.lines.size());
            if (i < node.children.size() - 1) {
                childrenWidth += GAP;
            }
        }

        int[] starts = new int[children.size()];
        int[] centers = new int[children.size()];
        int cursor = 0;
        for (int i = 0; i < children.size(); i++) {
            Drawing child = children.get(i);
            starts[i] = cursor;
            centers[i] = cursor + child.rootColumn;
            cursor += child.width + GAP;
        }

        int rootColumn = (centers[0] + centers[centers.length - 1]) / 2;
        int rootStart = rootColumn - node.label.length() / 2;
        int leftPad = Math.max(0, -rootStart);
        int rightPad = Math.max(0, rootStart + node.label.length() - childrenWidth);

        if (leftPad > 0) {
            rootColumn += leftPad;
            rootStart += leftPad;
            for (int i = 0; i < starts.length; i++) {
                starts[i] += leftPad;
                centers[i] += leftPad;
            }
        }

        int width = childrenWidth + leftPad + rightPad;
        List<String> lines = new ArrayList<>();
        lines.add(placeText(width, rootStart, node.label));
        lines.add(connectorLine(width, rootColumn, centers));

        for (int row = 0; row < maxChildHeight; row++) {
            char[] line = blankLine(width);
            for (int i = 0; i < children.size(); i++) {
                Drawing child = children.get(i);
                if (row < child.lines.size()) {
                    putText(line, starts[i], child.lines.get(row));
                }
            }
            lines.add(new String(line));
        }

        return new Drawing(lines, width, rootColumn);
    }

    private static String connectorLine(int width, int rootColumn, int[] centers) {
        char[] line = blankLine(width);

        if (centers.length == 1) {
            line[rootColumn] = '|';
            return new String(line);
        }

        for (int i = 0; i < centers.length; i++) {
            if (i == 0) {
                line[centers[i]] = '/';
            } else if (i == centers.length - 1) {
                line[centers[i]] = '\\';
            } else {
                line[centers[i]] = '|';
            }
        }

        return new String(line);
    }

    private static PrintableNode toPrintableNode(ASTNode node) {
        if (node instanceof Program) {
            Program program = (Program) node;
            if (program.statements.size() == 1) {
                return toPrintableNode(program.statements.get(0));
            }

            PrintableNode printable = new PrintableNode("Program");
            for (ASTNode statement : program.statements) {
                printable.addChild(toPrintableNode(statement));
            }
            return printable;
        }

        if (node instanceof Declaration) {
            Declaration declaration = (Declaration) node;
            PrintableNode printable = new PrintableNode("Declaration");
            printable.addChild(new PrintableNode(declaration.type));
            printable.addChild(new PrintableNode(declaration.name));
            if (declaration.initializer != null) {
                printable.addChild(toPrintableNode(declaration.initializer));
            }
            return printable;
        }

        if (node instanceof Assignment) {
            Assignment assignment = (Assignment) node;
            PrintableNode printable = new PrintableNode("=");
            printable.addChild(new PrintableNode(assignment.variable));
            printable.addChild(toPrintableNode(assignment.value));
            return printable;
        }

        if (node instanceof BinaryOp) {
            BinaryOp binaryOp = (BinaryOp) node;
            PrintableNode printable = new PrintableNode(binaryOp.operator);
            printable.addChild(toPrintableNode(binaryOp.left));
            printable.addChild(toPrintableNode(binaryOp.right));
            return printable;
        }

        if (node instanceof parser.Number) {
            parser.Number number = (parser.Number) node;
            return new PrintableNode(number.value);
        }

        if (node instanceof Variable) {
            Variable variable = (Variable) node;
            return new PrintableNode(variable.name);
        }

        if (node instanceof PrintStatement) {
            PrintStatement printStatement = (PrintStatement) node;
            PrintableNode printable = new PrintableNode("print");
            printable.addChild(toPrintableNode(printStatement.expression));
            return printable;
        }

        if (node instanceof Block) {
            Block block = (Block) node;
            PrintableNode printable = new PrintableNode("Block");
            for (ASTNode statement : block.statements) {
                printable.addChild(toPrintableNode(statement));
            }
            return printable;
        }

        if (node instanceof IfStatement) {
            IfStatement ifStatement = (IfStatement) node;
            PrintableNode printable = new PrintableNode("if");
            printable.addChild(toPrintableNode(ifStatement.condition));
            for (ASTNode statement : ifStatement.thenBranch) {
                printable.addChild(toPrintableNode(statement));
            }
            return printable;
        }

        if (node instanceof WhileStatement) {
            WhileStatement whileStatement = (WhileStatement) node;
            PrintableNode printable = new PrintableNode("while");
            printable.addChild(toPrintableNode(whileStatement.condition));
            for (ASTNode statement : whileStatement.body) {
                printable.addChild(toPrintableNode(statement));
            }
            return printable;
        }

        return new PrintableNode(node.getClass().getSimpleName());
    }

    private static String placeText(int width, int start, String text) {
        char[] line = blankLine(width);
        putText(line, start, text);
        return new String(line);
    }

    private static char[] blankLine(int width) {
        char[] line = new char[width];
        for (int i = 0; i < width; i++) {
            line[i] = ' ';
        }
        return line;
    }

    private static void putText(char[] line, int start, String text) {
        for (int i = 0; i < text.length() && start + i < line.length; i++) {
            if (start + i >= 0) {
                line[start + i] = text.charAt(i);
            }
        }
    }

    private static String trimRight(String text) {
        int end = text.length();
        while (end > 0 && text.charAt(end - 1) == ' ') {
            end--;
        }
        return text.substring(0, end);
    }

    private static class Drawing {
        private final List<String> lines;
        private final int width;
        private final int rootColumn;

        private Drawing(List<String> lines, int width, int rootColumn) {
            this.lines = lines;
            this.width = width;
            this.rootColumn = rootColumn;
        }
    }

    private static class PrintableNode {
        private final String label;
        private final List<PrintableNode> children;

        private PrintableNode(String label) {
            this.label = label;
            this.children = new ArrayList<>();
        }

        private void addChild(PrintableNode child) {
            children.add(child);
        }
    }
}
