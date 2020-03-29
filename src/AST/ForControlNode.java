package AST;

import Semantic.Scope;
import Utils.Position;

/*
forControl
    : initial=expression? ';' condition=expression? ';' update=expression?
    ;
 */

public class ForControlNode extends ASTNode {
    private ExpressionNode ex1, ex2, ex3;
    Scope scope;

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public ForControlNode(Position pos, ExpressionNode e1, ExpressionNode e2, ExpressionNode e3) {
        super(pos);
        this.ex1 = e1;
        this.ex2 = e2;
        this.ex3 = e3;
    }

    public ExpressionNode getInitialExpr() {
        return ex1;
    }

    public ExpressionNode getConditionExpr() {
        return ex2;
    }

    public ExpressionNode getUpdateExpr() {
        return ex3;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
