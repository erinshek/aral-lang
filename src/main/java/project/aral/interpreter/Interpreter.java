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
        } else if (node instanceof IfStatement) {
            executeIfStatement((IfStatement) node);
        } else if (node instanceof WhileStatement) {
            executeWhileStatement((WhileStatement) node);
        } else if (node instanceof AssignmentStatement) {
            executeAssignmentStatement((AssignmentStatement) node);
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

    private void executeIfStatement(IfStatement node) {
        Object conditionValue = evaluate(node.getCondition());

        boolean condition = isTrue(conditionValue);

        if (condition) {
            for (ASTNode statement : node.getThenBody()) {
                execute(statement);
            }
        } else {
            boolean elifExecuted = false;
            if (node.getElifParts() != null) {
                for (IfStatement elifPart : node.getElifParts()) {
                    Object elifCondition = evaluate(elifPart.getCondition());
                    if (isTrue(elifCondition)) {
                        for (ASTNode statement : elifPart.getThenBody()) {
                            execute(statement);
                        }
                        elifExecuted = true;
                        break;
                    }
                }
            }

            if (!elifExecuted && node.getElseBody() != null) {
                for (ASTNode statement : node.getElseBody()) {
                    execute(statement);
                }
            }
        }
    }

    private void executeWhileStatement(WhileStatement node) {
        while (true) {
            Object conditionValue = evaluate(node.getCondition());

            if (!isTrue(conditionValue)) {
                break;
            }

            for (ASTNode statement : node.getBody()) {
                execute(statement);
            }
        }
    }

    private void executeAssignmentStatement(AssignmentStatement node) {
        String name = node.getName();

        if (!variables.containsKey(name)) {
            throw new RuntimeException("Undefined variable: " + name + " at line " + node.getLine());
        }

        Object value = evaluate(node.getValue());
        variables.put(name, value);
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
        String operator = node.getOperator();

        if (operator.equals("hám")) {
            Object leftValue = evaluate(node.getLeft());
            if (!isTrue(leftValue)) {
                return false;
            }
            Object rightValue = evaluate(node.getRight());
            return isTrue(rightValue);
        }

        if (operator.equals("yamasa")) {
            Object leftValue = evaluate(node.getLeft());
            if (isTrue(leftValue)) {
                return true;
            }
            Object rightValue = evaluate(node.getRight());
            return isTrue(rightValue);
        }

        Object leftValue = evaluate(node.getLeft());
        Object rightValue = evaluate(node.getRight());

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

    private boolean isTrue(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Double) {
            return  (Double) value != 0.0;
        }
        if (value instanceof String) {
            return !((String) value).isEmpty();
        }
        return false;
    }
}