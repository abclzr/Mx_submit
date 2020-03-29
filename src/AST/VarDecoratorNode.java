package AST;

import Semantic.Scope;
import Semantic.VariableSymbol;
import Utils.Position;

/*
variableDecorator: Identifier ( '=' expression)?;
 */

public class VarDecoratorNode extends ASTNode {
    private String id;
    private ExpressionNode expr;
    private VariableSymbol variableSymbol;
    private Scope scope;

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public void setVariableSymbol(VariableSymbol variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public VariableSymbol getVariableSymbol() {
        return variableSymbol;
    }

    public VarDecoratorNode(Position pos, String i, ExpressionNode e) {
        super(pos);
        this.id = i;
        this.expr = e;
    }

    public String getIdentifier() {
        return id;
    }

    public boolean hasExpr() {
        return expr != null;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
