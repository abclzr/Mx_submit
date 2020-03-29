package AST;

import Utils.Position;

import java.util.List;

public class ParametersNode extends ASTNode {
    private List<ParameterNode> list;

    public ParametersNode(Position pos, List<ParameterNode> par) {
        super(pos);
        this.list = par;
    }

    public List<ParameterNode> getParameterList() {
        return list;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
