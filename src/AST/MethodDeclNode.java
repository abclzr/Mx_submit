package AST;

import Semantic.FunctionSymbol;
import Utils.Position;

import java.util.ArrayList;
import java.util.List;

/*
methodDeclaration: (typeOrVoid)? Identifier parameters block;
 */

public class MethodDeclNode extends ASTNode {
    private TypeOrVoidNode typeOrVoid;
    private String identifier;
    private List<ParameterNode> parameterList;
    private BlockNode block;
    private boolean isConstructor;
    private FunctionSymbol funcSymbol;

    public void setFuncSymbol(FunctionSymbol func) {
        funcSymbol = func;
    }

    public FunctionSymbol getFuncSymbol() {
        return funcSymbol;
    }

    MethodDeclNode(Position pos, TypeOrVoidNode tp, String id, List<ParameterNode> li, BlockNode bl, boolean is) {
        super(pos);
        this.typeOrVoid = tp;
        this.identifier= id;
        if (li == null) this.parameterList = new ArrayList<>();
        else this.parameterList = li;
        this.block = bl;
        this.isConstructor = is;
    }

    public TypeOrVoidNode getTypOrVoid() {
        return typeOrVoid;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<ParameterNode> getParameterList() {
        return parameterList;
    }

    public BlockNode getBlock() {
        return block;
    }

    public boolean isConstructor() {
        return isConstructor;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
