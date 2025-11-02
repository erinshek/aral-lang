package project.aral.parser.ast;

public class BinaryExpression extends ASTNode {
    private ASTNode left;
    private String operator;
    private ASTNode right;

    public BinaryExpression(ASTNode left, String operator, ASTNode right, int line) {
        super(line);
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public ASTNode getLeft() {
        return left;
    }

    public String getOperator() {
        return operator;
    }

    public ASTNode getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "BinaryExpression{" + left + " " + operator + " " + right + "}";
    }
}