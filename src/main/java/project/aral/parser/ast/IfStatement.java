package project.aral.parser.ast;

import java.util.List;

public class IfStatement extends ASTNode {
    private ASTNode condition;
    private List<ASTNode> thenBody;
    private List<IfStatement> elifParts;
    private List<ASTNode> elseBody;

    public IfStatement(ASTNode condition, List<ASTNode> thenBody, 
                       List<IfStatement> elifParts, List<ASTNode> elseBody, int line) {
        super(line);
        this.condition = condition;
        this.thenBody = thenBody;
        this.elifParts = elifParts;
        this.elseBody = elseBody;
    }

    public ASTNode getCondition() {
        return condition;
    }

    public List<ASTNode> getThenBody() {
        return thenBody;
    }

    public List<IfStatement> getElifParts() {
        return elifParts;
    }

    public List<ASTNode> getElseBody() {
        return elseBody;
    }

    @Override
    public String toString() {
        return "IfStatement{condition=" + condition + ", then=" + thenBody.size() + 
               " statements, elif=" + (elifParts != null ? elifParts.size() : 0) + 
               ", else=" + (elseBody != null ? elseBody.size() : 0) + "}";
    }
}