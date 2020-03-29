package AST;

import Semantic.Scope;
import Semantic.Type;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class CreatorNode extends ASTNode {
    private String id;
    private boolean isPrimitive;
    private List<ExpressionNode> expr;
    private int dimension;
    private Scope scope;
    private Type exprType;

    public void setExprType(Type exprType) {
        this.exprType = exprType;
    }

    public Type getExprType() {
        return exprType;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public CreatorNode(Position pos, String id, boolean isPrimitive, List<ExpressionNode> ex, int di) {
        super(pos);
        this.id = id;
        this.isPrimitive = isPrimitive;
        if (ex == null) this.expr = new ArrayList<>();
        else this.expr = ex;
        this.dimension = di;
    }

    public List<ExpressionNode> getExpr() {return expr;}
    public String getId() {return id;}
    public boolean getIsPrimitive() {return isPrimitive;}
    public int getDimension() {return dimension;}

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
