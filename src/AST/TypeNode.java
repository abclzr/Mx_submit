package AST;

import Utils.Position;

public class TypeNode extends ASTNode {
    private String identifier;
    private boolean isPrimitive;
    private int dimension;

    public TypeNode(Position pos, String id, boolean isP, int d) {
        super(pos);
        this.identifier = id;
        this.isPrimitive = isP;
        this.dimension = d;
    }

    public String getIdentifier() {
        return identifier;
    }

    public boolean getIsPrimitive() {
        return isPrimitive;
    }

    public int getDimension() {
        return dimension;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
