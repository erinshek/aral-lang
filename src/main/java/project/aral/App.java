package project.aral;

import project.aral.lexer.Lexer;
import project.aral.lexer.Token;

import java.util.List;

public class App {
    public static void main(String[] args) {
        String code = """
                ózgeriwshi san a = 5;
                ózgeriwshi san b = 3;
                """;
        Lexer lexer = new Lexer(code);
        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
