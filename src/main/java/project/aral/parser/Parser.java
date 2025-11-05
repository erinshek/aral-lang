package project.aral.parser;

import project.aral.lexer.Token;
import project.aral.parser.ast.*;
import project.aral.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private List<Token> tokens;
    private int position;
    private Token currentToken;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
        this.currentToken = tokens.get(0);
    }

    private void advance() {
        position++;
        if (position < tokens.size()) {
            currentToken = tokens.get(position);
        }
    }

    private void expect(TokenType type) {
        if (currentToken.getType() != type) {
            throw new RuntimeException("Expected " + type + " but got " + 
                currentToken.getType() + " at line " + currentToken.getLine());
        }
        advance();
    }

    public List<ASTNode> parse() {
        List<ASTNode> statements = new ArrayList<>();
        
        while (currentToken.getType() != TokenType.EOF) {
            statements.add(parseStatement());
        }
        
        return statements;
    }

    private ASTNode parseStatement() {
        // KEYWORD bo'lsa - VariableDeclaration yoki PrintStatement
        if (currentToken.getType() == TokenType.KEYWORD) {
            String keyword = currentToken.getValue();
            
            if (keyword.equals("ózgeriwshi")) {
                return parseVariableDeclaration();
            } else if (keyword.equals("shıǵar")) {
                return parsePrintStatement();
            } else if (keyword.equals("eger")) {
                return parseIfStatement();
            } else if (keyword.equals("qashan")) {
                return parseWhileStatement();
            }
        }

        if (currentToken.getType() == TokenType.IDENTIFIER) {
            return parseAssignmentStatement();
        }
        
        throw new RuntimeException("Unexpected token: " + currentToken.getValue() + 
            " at line " + currentToken.getLine());
    }

    private ASTNode parseVariableDeclaration() {
        int line = currentToken.getLine();
        expect(TokenType.KEYWORD);

        if (currentToken.getType() != TokenType.KEYWORD) {
            throw new RuntimeException("Expected type at line " + line);
        }
        String type = currentToken.getValue();
        advance();

        if (currentToken.getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected identifier at line " + line);
        }
        String name = currentToken.getValue();
        advance();

        expect(TokenType.EQUAL);

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON);
        
        return new VariableDeclaration(type, name, value, line);
    }

    private ASTNode parsePrintStatement() {
        int line = currentToken.getLine();
        expect(TokenType.KEYWORD);
        expect(TokenType.LEFT_PAREN);
        
        ASTNode expression = parseExpression();
        
        expect(TokenType.RIGHT_PAREN);
        expect(TokenType.SEMICOLON);
        
        return new PrintStatement(expression, line);
    }

    private ASTNode parseIfStatement() {
        int line = currentToken.getLine();
        expect(TokenType.KEYWORD); // "eger"
        expect(TokenType.LEFT_PAREN); // (

        ASTNode condition = parseExpression();

        expect(TokenType.RIGHT_PAREN);

        if (currentToken.getType() != TokenType.KEYWORD ||
                !currentToken.getValue().equals("bolsa")) {
            throw new RuntimeException("Expected 'bolsa' after condition at line " + line);
        }
        advance(); // "bolsa"

        expect(TokenType.LEFT_BRACE); // {

        List<ASTNode> thenBody = parseBlock();

        expect(TokenType.RIGHT_BRACE); // }

        List<IfStatement> elifParts = new ArrayList<>();
        while (currentToken.getType() == TokenType.KEYWORD &&
                currentToken.getValue().equals("ol")) {
            advance(); // "ol"

            if (currentToken.getType() != TokenType.KEYWORD ||
                    !currentToken.getValue().equals("bolmasa")) {
                throw new RuntimeException("Expected 'bolmasa' after 'ol' at line " + line);
            }
            advance(); // "bolmasa"

            expect(TokenType.LEFT_PAREN);
            ASTNode elifCondition = parseExpression();
            expect(TokenType.RIGHT_PAREN);
            expect(TokenType.LEFT_BRACE);
            List<ASTNode> elifBody = parseBlock();
            expect(TokenType.RIGHT_BRACE);

            elifParts.add(new IfStatement(elifCondition, elifBody, null, null, line));
        }

        List<ASTNode> elseBody = null;
        if (currentToken.getType() == TokenType.KEYWORD &&
                currentToken.getValue().equals("dım")) {
            advance(); // "dım"

            if (currentToken.getType() != TokenType.KEYWORD ||
                    !currentToken.getValue().equals("bolmasa")) {
                throw new RuntimeException("Expected 'bolmasa' after 'dım' at line " + line);
            }
            advance(); // "bolmasa"

            expect(TokenType.LEFT_BRACE);
            elseBody = parseBlock();
            expect(TokenType.RIGHT_BRACE);
        }

        return new IfStatement(condition, thenBody,
                elifParts.isEmpty() ? null : elifParts,
                elseBody, line);
    }

    private ASTNode parseWhileStatement() {
        int line = currentToken.getLine();
        expect(TokenType.KEYWORD); // "qashan"
        expect(TokenType.LEFT_PAREN); // (

        ASTNode condition = parseExpression();

        expect(TokenType.RIGHT_PAREN); // )

        if (currentToken.getType() != TokenType.KEYWORD ||
                !currentToken.getValue().equals("bolsa")) {
            throw new RuntimeException("Expected 'bolsa' after condition at line " + line);
        }
        advance(); // "bolsa"

        expect(TokenType.LEFT_BRACE); // {

        List<ASTNode> body = parseBlock();

        expect(TokenType.RIGHT_BRACE); // }

        return new WhileStatement(condition, body, line);
    }

    private ASTNode parseAssignmentStatement() {
        int line = currentToken.getLine();

        if (currentToken.getType() != TokenType.IDENTIFIER) {
            throw new RuntimeException("Expected identifier at line " + line);
        }
        String name = currentToken.getValue();
        advance();

        expect(TokenType.EQUAL); // =

        ASTNode value = parseExpression();

        expect(TokenType.SEMICOLON); // ;

        return new AssignmentStatement(name, value, line);
    }

    private ASTNode parseExpression() {
        return parseLogicalOr();
    }

    private ASTNode parseLogicalOr() {
        ASTNode left = parseLogicalAnd();

        while (currentToken.getType() == TokenType.KEYWORD &&
                currentToken.getValue().equals("yamasa")) {
            String operator = "yamasa";
            int line = currentToken.getLine();
            advance();
            ASTNode right = parseLogicalAnd();
            left = new BinaryExpression(left, operator, right, line);
        }
        return left;
    }

    private ASTNode parseLogicalAnd() {
        ASTNode left = parseComparison();

        while (currentToken.getType() == TokenType.KEYWORD &&
                currentToken.getValue().equals("hám")) {
            String operator = "hám";
            int line = currentToken.getLine();
            advance();
            ASTNode right = parseComparison();
            left = new BinaryExpression(left, operator, right, line);
        }
        return left;
    }

    private ASTNode parseComparison() {
        ASTNode left = parseAdditive();

        while (currentToken.getType() == TokenType.GREATER ||
                currentToken.getType() == TokenType.LESS ||
                currentToken.getType() == TokenType.GREATER_OR_EQUAL ||
                currentToken.getType() == TokenType.LESS_OR_EQUAL ||
                currentToken.getType() == TokenType.EQUAL_EQUAL ||
                currentToken.getType() == TokenType.NOT_EQUAL) {
            String operator = currentToken.getValue();
            int line = currentToken.getLine();
            advance();
            ASTNode right = parseAdditive();
            left = new BinaryExpression(left, operator, right, line);
        }

        return left;
    }

    private ASTNode parseAdditive() {
        ASTNode left = parseMultiplicative();
        
        while (currentToken.getType() == TokenType.PLUS || 
               currentToken.getType() == TokenType.MINUS) {
            String operator = currentToken.getValue();
            int line = currentToken.getLine();
            advance();
            ASTNode right = parseMultiplicative();
            left = new BinaryExpression(left, operator, right, line);
        }
        
        return left;
    }

    private ASTNode parseMultiplicative() {
        ASTNode left = parsePrimary();
        
        while (currentToken.getType() == TokenType.MULTIPLY || 
               currentToken.getType() == TokenType.DIVIDE) {
            String operator = currentToken.getValue();
            int line = currentToken.getLine();
            advance();
            ASTNode right = parsePrimary();
            left = new BinaryExpression(left, operator, right, line);
        }
        
        return left;
    }

    private ASTNode parsePrimary() {
        Token token = currentToken;

        if (token.getType() == TokenType.NUMBER) {
            advance();
            return new NumberLiteral(Double.parseDouble(token.getValue()), token.getLine());
        }

        if (token.getType() == TokenType.STRING) {
            advance();
            return new StringLiteral(token.getValue(), token.getLine());
        }

        if (token.getType() == TokenType.IDENTIFIER) {
            advance();
            return new Identifier(token.getValue(), token.getLine());
        }

        if (token.getType() == TokenType.LEFT_PAREN) {
            advance();
            ASTNode expr = parseExpression();
            expect(TokenType.RIGHT_PAREN);
            return expr;
        }
        
        throw new RuntimeException("Unexpected token in expression: " + token.getValue() + 
            " at line " + token.getLine());
    }

    private List<ASTNode> parseBlock() {
        List<ASTNode> statements = new ArrayList<>();

        while (currentToken.getType() != TokenType.RIGHT_BRACE &&
                currentToken.getType() != TokenType.EOF) {
            statements.add(parseStatement());
        }

        return statements;
    }
}