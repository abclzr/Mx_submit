package AST;

import Semantic.FunctionSymbol;
import Utils.Position;

import java.util.List;

/*
functionDeclaration: typeOrVoid Identifier parameters block;
 */

public class FuncDeclNode extends ASTNode {
    private TypeOrVoidNode typeOrVoid;
    private String identifier;
    private List<ParameterNode> parameterList;
    private BlockNode block;
    private FunctionSymbol functionSymbol;

    FuncDeclNode(Position pos, TypeOrVoidNode tp, String id, List<ParameterNode> li, BlockNode bl) {
        super(pos);
        this.typeOrVoid = tp;
        this.identifier= id;
        this.parameterList = li;
        this.block = bl;
    }

    public void setFunctionSymbol(FunctionSymbol functionSymbol) {
        this.functionSymbol = functionSymbol;
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public TypeOrVoidNode getTypeOrVoid() {
        return typeOrVoid;
    }

    public String getReturnTypeName() {
        return typeOrVoid.getTypeName();
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

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
