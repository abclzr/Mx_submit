package AST;

import Semantic.Scope;
import Utils.Position;

import java.util.List;

/*
block: '{' blockStatement '}' ;
blockStatement : statement*;
 */

public class BlockNode extends ASTNode {
    private List<StatementNode> list;
    private Scope scope;

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public BlockNode(Position pos, List<StatementNode> li) {
        super(pos);
        list = li;
    }

    public List<StatementNode> getStatementList() {
        return list;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
