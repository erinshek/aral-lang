package project.aral.parser.ast;

public class AssignmentStatement extends ASTNode {
    private String name;
    private ASTNode value;

    public AssignmentStatement(String name, ASTNode value, int line) {
        super(line);
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "AssignmentStatement{name='" + name + "', value=" + value + "}";
    }
}