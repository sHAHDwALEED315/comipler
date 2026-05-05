# Mini Compiler — Java

A mini compiler built from scratch in Java **without any external libraries**.  
It processes a custom programming language through three phases: **Lexing → Parsing → Semantic Analysis**.
The material to build this Project is [compiler practical](https://peat-vision-dfb.notion.site/Compiler-Design-30e755b65aa4803eafb5feba83e47914)

---

## Project Structure

```
src/
├── Main.java
├── code.txt                  ← source code file to compile
│
├── lexer/                    ← Phase 1: Lexical Analysis
│   ├── Lexer.java
│   ├── Token.java
│   └── TokenType.java
│
├── parser/                   ← Phase 2: Parsing & AST
│   ├── Parser.java
│   ├── ASTNode.java
│   ├── Expression.java
│   ├── Program.java
│   ├── Declaration.java
│   ├── Assignment.java
│   ├── BinaryOp.java
│   ├── Block.java
│   ├── IfStatement.java
│   ├── WhileStatement.java
│   ├── PrintStatement.java
│   ├── Number.java
│   ├── Variable.java
│   ├── ASTUtils.java
│   └── TreePrinter.java
│
└── semantic/                 ← Phase 3: Semantic Analysis
    ├── Symbol.java
    ├── SymbolTable.java
    └── SemanticAnalyzer.java
```

---

## Compiler Phases

### Phase 1 — Lexical Analysis (Lexer)
Reads the raw source code character by character and produces a flat list of **Tokens**.

```
"int x = 5;"  →  [INT, IDENTIFIER(x), ASSIGN, NUMBER(5), SEMICOLON]
```

### Phase 2 — Parsing (Parser + AST)
Takes the token list and builds an **Abstract Syntax Tree (AST)** using **Recursive Descent Parsing**.  
Each grammar rule maps directly to a method in `Parser.java`.

```
[INT, IDENTIFIER(x), ASSIGN, NUMBER(5), SEMICOLON]
  →  Declaration{ type=int, name=x, initializer=Number{5} }
```

The `TreePrinter` displays the AST visually in the console.

### Phase 3 — Semantic Analysis (Symbol Table)
Walks the AST and checks for semantic correctness:

| Check | Example Error |
|-------|--------------|
| Variable declared twice in same scope | `int x; int x;` |
| Variable used before declaration | `print(x);` before `int x;` |
| Assignment to undeclared variable | `x = 5;` before `int x;` |
| Nested scope isolation | variable declared inside `if` not visible outside |

---

## Context-Free Grammar (CFG)

The language supported by this compiler:

```
program         → statement* EOF

statement       → declaration
                | assignment
                | printStatement
                | ifStatement
                | whileStatement
                | block
                | expression ";"

declaration     → type IDENTIFIER ("=" expression)? ";"
type            → "int" | "float" | "string" | "boolean" | "bool"

assignment      → IDENTIFIER "=" expression ";"

printStatement  → "print" "(" expression ")" ";"

ifStatement     → "if" "(" expression ")" block

whileStatement  → "while" "(" expression ")" block

block           → "{" statement* "}"

expression      → equality
equality        → comparison (("==" | "!=") comparison)*
comparison      → term (("<" | ">" | "<=" | ">=") term)*
term            → factor (("+" | "-") factor)*
factor          → unary (("*" | "/") unary)*
unary           → NUMBER
                | IDENTIFIER
                | "(" expression ")"
```

### Operator Precedence (low → high)

| Level | Operators       |
|-------|-----------------|
| 1     | `==`  `!=`      |
| 2     | `<`  `>`  `<=`  `>=` |
| 3     | `+`  `-`        |
| 4     | `*`  `/`        |
| 5     | literals, variables, `(expr)` |

---

## Sample Input (`code.txt`)

```
int x = 5;
int y = 10;
int z = x + y;
print(z);

if (x < y) {
    int result = x * 2;
    print(result);
}

while (x < y) {
    x = x + 1;
}
```

### Expected Output

```
Lexing Done Successfully

Parsing Done Successfully
        =
       / \
      z   +
         / \
        x   y
...

Starting Semantic Analysis...
[SemanticAnalyzer] Declared: int x
[SemanticAnalyzer] Declared: int y
...

Semantic Analysis Done Successfully

===== Symbol Table =====
  Scope Level 0:
    Symbol{name='x', type='int', value=null, scope=0}
    Symbol{name='y', type='int', value=null, scope=0}
    Symbol{name='z', type='int', value=null, scope=0}
========================
```

---

## How to Run

### Requirements
- Java 8 or higher
- No external libraries needed

### Compile

```bash
# من داخل مجلد src
javac -d out lexer/*.java parser/*.java semantic/*.java Main.java
```

### Run

```bash
java -cp out Main
```

### One-liner (compile + run)

```bash
javac -d out lexer/*.java parser/*.java semantic/*.java Main.java && java -cp out Main
```

> **Note:** Make sure `code.txt` is inside the `src/` folder before running.

---

## Semantic Errors — Examples

```java
// Error 1: variable used before declaration
print(x);       // SemanticError: Variable 'x' is not declared.
int x = 5;

// Error 2: variable declared twice
int x = 5;
int x = 10;     // SemanticError: Variable 'x' is already declared in this scope.

// Error 3: assignment to undeclared variable
y = 20;         // SemanticError: Cannot assign to undeclared variable 'y'.
```
