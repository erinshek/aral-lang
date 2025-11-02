package project.aral.parser.ast;

public class NumberLiteral extends ASTNode {
    private double value;

    public NumberLiteral(double value, int line) {
        super(line);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "NumberLiteral{" + value + "}";
    }
}