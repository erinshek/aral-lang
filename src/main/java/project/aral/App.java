package project.aral;

import project.aral.interpreter.Interpreter;
import project.aral.lexer.Lexer;
import project.aral.lexer.Token;
import project.aral.parser.Parser;
import project.aral.parser.ast.ASTNode;

import java.util.List;

public class App {
    public static void main(String[] args) {
        String code = """ 
        ózgeriwshi san a = 10;
        
        qashan (a > 0) bolsa {
            shıǵar(a);
            a = a - 1;
        }
        """;

        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        List<ASTNode> ast = parser.parse();

        Interpreter interpreter = new Interpreter();
        interpreter.interpret(ast);
    }
}
