package project.aral.parser.ast;

public abstract class ASTNode {
    protected int line;

    public ASTNode(int line) {
        this.line = line;
    }

    public int getLine() {
        return line;
    }
}