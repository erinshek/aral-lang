package project.aral.interpreter;

import project.aral.parser.ast.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private Map<String, Object> variables;

    public Interpreter() {
        this.variables = new HashMap<>();
    }

    public void interpret(List<ASTNode> statements) {
        for (ASTNode statement : statements) {
            execute(statement);
        }
    }

    private void execute(ASTNode node) {
        if (node instanceof VariableDeclaration) {
            executeVariableDeclaration((VariableDeclaration) node);
        } else if (node instanceof PrintStatement) {
            executePrintStatement((PrintStatement) node);
        } else {
            throw new RuntimeException("Unknown statement type: " + node.getClass().getName());
        }
    }

    private void executeVariableDeclaration(VariableDeclaration node) {
        String name = node.getName();
        Object value = evaluate(node.getValue());
        variables.put(name, value);
    }

    private void executePrintStatement(PrintStatement node) {
        Object value = evaluate(node.getExpression());
        System.out.println(value);
    }

    private Object evaluate(ASTNode node) {
        if (node instanceof NumberLiteral) {
            return ((NumberLiteral) node).getValue();
        } else if (node instanceof StringLiteral) {
            return ((StringLiteral) node).getValue();
        } else if (node instanceof Identifier) {
            String name = ((Identifier) node).getValue();
            if (!variables.containsKey(name)) {
                throw new RuntimeException("Undefined variable: " + name);
            }
            return variables.get(name);
        } else if (node instanceof BinaryExpression) {
            return evaluateBinaryExpression((BinaryExpression) node);
        }
        
        throw new RuntimeException("Unknown expression type: " + node.getClass().getName());
    }

    private Object evaluateBinaryExpression(BinaryExpression node) {
        Object leftValue = evaluate(node.getLeft());
        Object rightValue = evaluate(node.getRight());
        String operator = node.getOperator();

        if (leftValue instanceof Double && rightValue instanceof Double) {
            double left = (Double) leftValue;
            double right = (Double) rightValue;

            switch (operator) {
                case "+": return left + right;
                case "-": return left - right;
                case "*": return left * right;
                case "/": return left / right;
                case ">": return left > right;
                case "<": return left < right;
                case ">=": return left >= right;
                case "<=": return left <= right;
                case "==": return left == right;
                case "!=": return left != right;
                default:
                    throw new RuntimeException("Unknown operator: " + operator);
            }
        }

        throw new RuntimeException("Type error in binary expression");
    }
}