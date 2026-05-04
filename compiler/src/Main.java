import lexer.*;
import parser.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        String source = new String(Files.readAllBytes(Paths.get("src/code.txt")));

        Lexer lexer = new Lexer(source);

        Token token;
        List<Token> tokens = new ArrayList<>();
        do {
            token = lexer.nextToken();
            tokens.add(token);
        } while (token.getType() != TokenType.EOF);

        parser.Parser parser = new parser.Parser(tokens);
        Program program = parser.Parse();

      
        System.out.println("\n PARSING Done SUCCESSFULLY"); 
        TreePrinter.printTree(program);
    }
}
