import lexer.*;
import parser.*;
import semantic.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        //String source = new String(Files.readAllBytes(Paths.get("src/code.txt")));
        String source = new String(Files.readAllBytes(Paths.get("code.txt")));
        
        // Phase 1: Lexing 
        Lexer lexer = new Lexer(source);

        Token token;
        List<Token> tokens = new ArrayList<>();
        do {
            token = lexer.nextToken();
            tokens.add(token);
        } while (token.getType() != TokenType.EOF);
        System.out.println("\n LEXING Done SUCCESSFULLY"); 

        // Phase 2: Parsing 
        parser.Parser parser = new parser.Parser(tokens);
        Program program = parser.Parse();

        System.out.println("\n PARSING Done SUCCESSFULLY"); 
        TreePrinter.printTree(program);

        // Phase 3: Semantic Analysis 
        System.out.println("\nStarting Semantic Analysis...");
        SemanticAnalyzer analyzer = new SemanticAnalyzer();
        try {
            analyzer.analyze(program);
            System.out.println("\nSemantic Analysis Done Successfully");
        } catch (RuntimeException e) {
            System.err.println("\n" + e.getMessage());
            return;
        }
 
        analyzer.printSymbolTable();

    }
}
