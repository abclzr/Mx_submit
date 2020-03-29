package AST;

import Utils.Position;
import java.util.List;

public class ProgramNode extends ASTNode {
    private List<ASTNode> declNodeList;

    ProgramNode(Position pos, List<ASTNode> l) {
        super(pos);
        this.declNodeList = l;
    }

    public List<ASTNode> getDeclNodeList() {
        return declNodeList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
