package semantic;

import parser.*;
import parser.Number;

/**
 * Semantic Analyzer — بيمشي على الـ AST
 * ويتحقق من القواعد الدلالية باستخدام الـ SymbolTable.
 *
 * الأخطاء اللي بيكشفها:
 *   1. متغير اتعرف مرتين في نفس الـ scope
 *   2. استخدام متغير قبل ما يتعرف
 *   3. Assignment لمتغير غير معرّف
 */
public class SemanticAnalyzer {

    private SymbolTable symbolTable;

    public SemanticAnalyzer() {
        this.symbolTable = new SymbolTable();
    }

    // -------- Entry Point --------

    public void analyze(Program program) {
        for (ASTNode statement : program.statements) {
            analyzeNode(statement);
        }
    }

    // -------- Node Dispatcher --------

    private void analyzeNode(ASTNode node) {
        if (node instanceof Declaration) {
            analyzeDeclaration((Declaration) node);

        } else if (node instanceof Assignment) {
            analyzeAssignment((Assignment) node);

        } else if (node instanceof PrintStatement) {
            analyzePrint((PrintStatement) node);

        } else if (node instanceof IfStatement) {
            analyzeIf((IfStatement) node);

        } else if (node instanceof WhileStatement) {
            analyzeWhile((WhileStatement) node);

        } else if (node instanceof Block) {
            analyzeBlock((Block) node);

        } else if (node instanceof Expression) {
            analyzeExpression((Expression) node);
        }
    }

    // -------- Statement Analyzers --------

    /**
     * Declaration: int x; أو int x = 5;
     * → أضف المتغير للجدول، لو فيه initializer حللها الأول.
     */
    private void analyzeDeclaration(Declaration decl) {
        // لو فيه initializer، تحقق منها الأول
        if (decl.initializer != null) {
            analyzeExpression(decl.initializer);
        }
        // أضف المتغير للجدول — هيرمي error لو متكرر
        symbolTable.declare(decl.name, decl.type);
        System.out.println("[SemanticAnalyzer] Declared: " + decl.type + " " + decl.name);
    }

    /**
     * Assignment: x = expr;
     * → تأكد إن x متعرف، وحلل الـ expression.
     */
    private void analyzeAssignment(Assignment assign) {
        // تأكد إن المتغير موجود
        symbolTable.lookupOrError(assign.variable);
        // حلل الجانب الأيمن
        analyzeExpression(assign.value);
        System.out.println("[SemanticAnalyzer] Assignment to: " + assign.variable);
    }

    /**
     * print(expr);
     * → حلل الـ expression جوا الـ print.
     */
    private void analyzePrint(PrintStatement print) {
        analyzeExpression(print.expression);
    }

    /**
     * if (condition) { body }
     * → حلل الـ condition، افتح scope جديد للـ body، اقفله.
     */
    private void analyzeIf(IfStatement ifStmt) {
        analyzeExpression(ifStmt.condition);

        symbolTable.enterScope();
        for (ASTNode stmt : ifStmt.thenBranch) {
            analyzeNode(stmt);
        }
        symbolTable.exitScope();
    }

    /**
     * while (condition) { body }
     * → حلل الـ condition، افتح scope جديد للـ body، اقفله.
     */
    private void analyzeWhile(WhileStatement whileStmt) {
        analyzeExpression(whileStmt.condition);

        symbolTable.enterScope();
        for (ASTNode stmt : whileStmt.body) {
            analyzeNode(stmt);
        }
        symbolTable.exitScope();
    }

    /**
     * { statements }
     * → scope جديد للبلوك كامل.
     */
    private void analyzeBlock(Block block) {
        symbolTable.enterScope();
        for (ASTNode stmt : block.statements) {
            analyzeNode(stmt);
        }
        symbolTable.exitScope();
    }

    // -------- Expression Analyzers --------

    private void analyzeExpression(Expression expr) {
        if (expr instanceof BinaryOp) {
            analyzeBinaryOp((BinaryOp) expr);

        } else if (expr instanceof Variable) {
            analyzeVariable((Variable) expr);

        } else if (expr instanceof Number) {
            // الأرقام مش محتاجة تحقق
        }
    }

    /**
     * BinaryOp: left op right
     * → حلل الطرفين.
     */
    private void analyzeBinaryOp(BinaryOp op) {
        analyzeExpression(op.left);
        analyzeExpression(op.right);
    }

    /**
     * Variable: x
     * → تأكد إنه متعرف في الجدول.
     */
    private void analyzeVariable(Variable var) {
        symbolTable.lookupOrError(var.name);
        System.out.println("[SemanticAnalyzer] Resolved variable: " + var.name);
    }

    // -------- Getter --------

    /** لو عايزة تطبعي الجدول بعد التحليل */
    public void printSymbolTable() {
        symbolTable.print();
    }
}