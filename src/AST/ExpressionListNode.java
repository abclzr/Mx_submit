package AST;

import Utils.Position;

import java.util.List;

public class ExpressionListNode extends ASTNode {
    private List<ExpressionNode> list;

    public ExpressionListNode(Position pos, List<ExpressionNode> list) {
        super(pos);
        this.list = list;
    }

    public List<ExpressionNode> getList() {
        return list;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
