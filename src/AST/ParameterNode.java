package AST;

import Utils.Position;

/*
parameter: type Identifier;
 */

public class ParameterNode extends ASTNode {
    private TypeNode type;
    private String identifier;

    public ParameterNode(Position pos, TypeNode tp, String id) {
        super(pos);
        this.type = tp;
        this.identifier = id;
    }

    public TypeNode getType() {
        return type;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
