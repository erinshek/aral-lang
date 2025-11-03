package project.aral.parser.ast;

public class VariableDeclaration extends ASTNode {
    private String type;
    private String name;
    private ASTNode value;

    public VariableDeclaration(String type, String name, ASTNode value, int line) {
        super(line);
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public ASTNode getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "VariableDeclaration{type='" + type + "', name='" + name + "', value=" + value + "}";
    }
}