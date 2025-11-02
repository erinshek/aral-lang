package project.aral.parser.ast;

public class StringLiteral extends ASTNode {
    private String value;

    public StringLiteral(String value, int line) {
        super(line);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "StringLiteral{\"" + value + "\"}";
    }
}