package Semantic;

import AST.*;

public class ClassMemberVisitor extends ASTVisitor {
    public ClassMemberVisitor(Scope globalScope) {
        super(globalScope);
    }

    @Override
    public void visit(ProgramNode node) {
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof ClassDeclNode)
                x.accept(this);
        });
    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(ClassBodyNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {
        Scope currentScope = node.getClassType().getScope();
        node.getVarDeclNodeList().forEach(x -> {
            Type type = Type.getType(x.getType());
            x.getVarDecoratorList().forEach(y -> {
                y.setVariableSymbol(new VariableSymbol(type, y.getIdentifier(), y, y.getPosition()));
                currentScope.addVariable(y.getVariableSymbol());
            });
        });
        node.getMethodDeclNodeList().forEach(x -> {
            Type returnType;
            if (x.isConstructor()) {
                assert x.getTypOrVoid() == null && x.getIdentifier().equals(node.getIdentifier());
                returnType = null;
            } else
                returnType = Type.getReturnType(x.getTypOrVoid());
            if (x.getIdentifier().equals(node.getIdentifier()))
                assert returnType == null;
            x.setFuncSymbol(new FunctionSymbol(returnType, x.getIdentifier(), x, currentScope, x.getParameterList()));
            currentScope.addFunction(x.getFuncSymbol());
        });
    }

    @Override
    public void visit(ClassTypeNode node) {

    }

    @Override
    public void visit(CreatorNode node) {

    }

    @Override
    public void visit(ExpressionListNode node) {

    }

    @Override
    public void visit(ExpressionNode node) {

    }

    @Override
    public void visit(ForControlNode node) {

    }

    @Override
    public void visit(FuncDeclNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override
    public void visit(MethodDeclNode node) {

    }

    @Override
    public void visit(ParameterNode node) {

    }

    @Override
    public void visit(ParametersNode node) {

    }

    @Override
    public void visit(PrimitiveTypeNode node) {

    }

    @Override
    public void visit(StatementNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(TypeOrVoidNode node) {

    }

    @Override
    public void visit(VarDeclNode node) {

    }

    @Override
    public void visit(VarDecoratorNode node) {

    }
}
