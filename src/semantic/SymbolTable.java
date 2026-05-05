package semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Symbol Table with scope support.
 *
 * Uses a Stack of HashMaps — each HashMap = one scope.
 *
 * Example:
 *   Global scope  → { x: int, y: float }
 *   if scope      → { z: int }         ← pushed when entering { }
 *                                       ← popped when leaving  { }
 */
public class SymbolTable {

    // كل عنصر في الـ Stack ده scope — فوق = الأحدث
    private Stack<Map<String, Symbol>> scopes;

    public SymbolTable() {
        scopes = new Stack<>();
        enterScope(); // ابدأ بـ global scope تلقائياً
    }

    // -------- Scope Management --------

    /** ادخل scope جديد (عند { ) */
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    /** اخرج من الـ scope الحالي (عند } ) */
    public void exitScope() {
        if (scopes.size() <= 1) {
            throw new RuntimeException("SemanticError: Cannot exit global scope.");
        }
        scopes.pop();
    }

    /** رقم الـ scope الحالي — مفيد للـ Symbol */
    public int currentScopeLevel() {
        return scopes.size() - 1;
    }

    // -------- Symbol Operations --------

    /**
     * أضف متغير جديد في الـ scope الحالي.
     * لو الاسم موجود في نفس الـ scope → error.
     */
    public void declare(String name, String type) {
        Map<String, Symbol> currentScope = scopes.peek();
        if (currentScope.containsKey(name)) {
            throw new RuntimeException(
                "SemanticError: Variable '" + name + "' is already declared in this scope.");
        }
        currentScope.put(name, new Symbol(name, type, currentScopeLevel()));
    }

    /**
     * أضف متغير مع قيمة ابتدائية.
     */
    public void declare(String name, String type, Object value) {
        Map<String, Symbol> currentScope = scopes.peek();
        if (currentScope.containsKey(name)) {
            throw new RuntimeException(
                "SemanticError: Variable '" + name + "' is already declared in this scope.");
        }
        currentScope.put(name, new Symbol(name, type, value, currentScopeLevel()));
    }

    /**
     * دور على المتغير في كل الـ scopes من الأحدث للأقدم.
     * لو مش موجود → ارجع null.
     */
    public Symbol lookup(String name) {
        // ابدأ من فوق (الـ scope الأحدث) لتحت
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                return scopes.get(i).get(name);
            }
        }
        return null;
    }

    /**
     * دور على المتغير — لو مش موجود throw error.
     */
    public Symbol lookupOrError(String name) {
        Symbol symbol = lookup(name);
        if (symbol == null) {
            throw new RuntimeException(
                "SemanticError: Variable '" + name + "' is not declared.");
        }
        return symbol;
    }

    /**
     * حدّث قيمة متغير موجود.
     */
    public void update(String name, Object value) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(name)) {
                scopes.get(i).get(name).setValue(value);
                return;
            }
        }
        throw new RuntimeException(
            "SemanticError: Cannot assign to undeclared variable '" + name + "'.");
    }

    /**
     * اطبع محتوى الجدول كامل — مفيد للـ debugging.
     */
    public void print() {
        System.out.println("===== Symbol Table =====");
        for (int i = scopes.size() - 1; i >= 0; i--) {
            System.out.println("  Scope Level " + i + ":");
            if (scopes.get(i).isEmpty()) {
                System.out.println("    (empty)");
            }
            for (Symbol sym : scopes.get(i).values()) {
                System.out.println("    " + sym);
            }
        }
        System.out.println("========================");
    }
}