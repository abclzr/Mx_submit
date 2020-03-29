package AST;

import Utils.Position;

public class TypeOrVoidNode extends ASTNode {
    private TypeNode typeNode;
    private boolean isVoid;

    public String getTypeName() {
        if (isVoid) return "void";
        else return typeNode.getIdentifier();
    }

    public TypeOrVoidNode(Position pos, TypeNode _t, boolean isV) {
        super(pos);
        this.typeNode = _t;
        this.isVoid = isV;
    }

    public TypeNode getTypeNode() {
        return typeNode;
    }

    public boolean isVoid() {
        return isVoid;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
