package Semantic;

import AST.ASTNode;
import AST.ParameterNode;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

public class FunctionSymbol extends Symbol {
    private Scope scope;
    private List<Type> list;

    public FunctionSymbol(Type tp, String na, ASTNode def, Position pos, Scope fatherScope, List<Type> li) {
        super(tp, na, def, pos);
        this.scope = new Scope(fatherScope, tp, this);
        if (li != null) this.list = li;
        else this.list = new ArrayList<>();
    }

    public FunctionSymbol(Type tp, String na, ASTNode def, Scope fatherScope, List<ParameterNode> list) {
        super(tp, na, def, def.getPosition());
        this.scope = new Scope(fatherScope, tp, this);
        this.list = new ArrayList<>();
        if (list != null) {
            for (ParameterNode x : list) {
                Type xType = Type.getType(x.getType());
                this.list.add(xType);
                this.scope.addVariable(new VariableSymbol(xType, x.getIdentifier(), x, x.getPosition()));
            }
        }
    }

    public List<Type> getParameters() {
        return list;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public boolean isVariableSymbol() {
        return false;
    }

    @Override
    public boolean isFunctionSymbol() {
        return true;
    }
}
