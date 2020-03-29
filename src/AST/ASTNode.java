package AST;

import Utils.Position;

public abstract class ASTNode {
    private Position pos;

    public ASTNode(Position pos) {this.pos = pos;}

    public abstract void accept(ASTVisitor visitor);

    public Position getPosition() {return pos;}
}
