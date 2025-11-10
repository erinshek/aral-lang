package project.aral;

import project.aral.lexer.Lexer;
import project.aral.lexer.Token;
import project.aral.parser.Parser;
import project.aral.interpreter.Interpreter;
import project.aral.parser.ast.ASTNode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.List;

public class App {
    private static final String VERSION = "0.1.0";

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String command = args[0];

        switch (command) {
            case "run":
                if (args.length < 2) {
                    System.err.println("Error: File name not specified");
                    System.err.println("Usage: aral run <file_name>.aral");
                    System.exit(1);
                }
                runFile(args[1]);
                break;

            case "--version":
            case "-v":
                System.out.println("Aral programming language v" + VERSION);
                break;

            case "--help":
            case "-h":
                printHelp();
                break;

            default:
                if (command.endsWith(".aral")) {
                    runFile(command);
                } else {
                    System.err.println("Unknown command: " + command);
                    printUsage();
                    System.exit(1);
                }
        }
    }

    private static void runFile(String fileName) {
        String code;
        try {
            code = Files.readString(Paths.get(fileName));
        } catch (IOException e) {
            System.err.println("Error: Could not read file: " + fileName);
            System.err.println("Reason: " + e.getMessage());
            System.exit(1);
            return;
        }

        try {
            Lexer lexer = new Lexer(code);
            List<Token> tokens = lexer.tokenize();

            Parser parser = new Parser(tokens);
            List<ASTNode> ast = parser.parse();

            Interpreter interpreter = new Interpreter();
            interpreter.interpret(ast);
        } catch (Exception e) {
            System.err.println("Program error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: aral <command> [parameters]");
        System.out.println();
        System.out.println("Commands:");
        System.out.println("  run <file>       Run a .aral file");
        System.out.println("  --version, -v    Show version");
        System.out.println("  --help, -h       Show help");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  aral run test.aral");
        System.out.println("  aral test.aral");
    }

    private static void printHelp() {
        System.out.println("Aral - A programming language in the Karakalpak language");
        System.out.println();
        printUsage();
        System.out.println();
        System.out.println("For more information: https://github.com/erinshek/aral-lang");
    }
}
