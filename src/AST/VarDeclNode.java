package AST;

import Semantic.Scope;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

/*
variableDeclaration: type variableDecorator(',' variableDecorator)* ';';
 */

public class VarDeclNode extends ASTNode {
    private TypeNode type;
    private List<VarDecoratorNode> list;
    private Scope scope;

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return this.scope;
    }

    VarDeclNode(Position pos, TypeNode tp, List<VarDecoratorNode> li) {
        super(pos);
        this.type = tp;
        if (li != null) this.list = li;
        else this.list = new ArrayList<>();
    }

    public TypeNode getType() {
        return type;
    }

    public List<VarDecoratorNode> getVarDecoratorList() {
        return list;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
