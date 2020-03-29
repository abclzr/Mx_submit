package AST;

import Utils.Position;

public class ClassTypeNode extends ASTNode {
    private String identifier;

    public ClassTypeNode(Position pos, String id) {
        super(pos);
        this.identifier = id;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
