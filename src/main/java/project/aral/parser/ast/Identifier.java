package project.aral.parser.ast;

public class Identifier extends ASTNode {
    private String name;

    public Identifier(String name, int line) {
        super(line);
        this.name = name;
    }

    public String getValue() {
        return name;
    }

    @Override
    public String toString() {
        return "Identifier{\"" + name + "\"}";
    }
}