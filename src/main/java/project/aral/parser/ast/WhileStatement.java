package project.aral.parser.ast;

import java.util.List;

public class WhileStatement extends ASTNode {
    private ASTNode condition;
    private List<ASTNode> body;

    public WhileStatement(ASTNode condition, List<ASTNode> body, int line) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "WhileStatement{condition=" + condition + ", body=" + body.size() + " statements}";
    }
}