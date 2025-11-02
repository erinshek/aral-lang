package project.aral.parser.ast;

public class PrintStatement extends ASTNode {
    private ASTNode expression;

    public PrintStatement(ASTNode expression, int line) {
        super(line);
        this.expression = expression;
    }

    public ASTNode getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return "PrintStatement{" + expression + "}";
    }
}