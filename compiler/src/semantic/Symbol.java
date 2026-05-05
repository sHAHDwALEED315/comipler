package semantic;

/**
 * Represents a single entry in the Symbol Table.
 * Each symbol stores info about one declared variable.
 */
public class Symbol {
    private String name;
    private String type;
    private Object value;     // null if not initialized yet
    private int scopeLevel;   // 0 = global, 1 = inside block, etc.

    public Symbol(String name, String type, int scopeLevel) {
        this.name = name;
        this.type = type;
        this.value = null;
        this.scopeLevel = scopeLevel;
    }

    public Symbol(String name, String type, Object value, int scopeLevel) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.scopeLevel = scopeLevel;
    }

    // ---------- Getters & Setters ----------

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    @Override
    public String toString() {
        return String.format("Symbol{name='%s', type='%s', value=%s, scope=%d}",
                name, type, value, scopeLevel);
    }
}