package Semantic;

import AST.*;
import Utils.Position;
import Utils.SemanticError;

import java.util.ArrayList;
import java.util.List;

public class ClassAndFuncVisitor extends ASTVisitor {
    public ClassAndFuncVisitor(Scope global) {
        super(global);
    }

    @Override
    public void visit(ProgramNode node) {
        node.getDeclNodeList().forEach(x-> {
            if (x instanceof ClassDeclNode)
                x.accept(this);
        });
        node.getDeclNodeList().forEach(x-> {
            if (x instanceof FuncDeclNode)
                x.accept(this);
        });
        FunctionSymbol mainFunc = globalScope.findFunc("main", new Position(null));
        if (mainFunc.getType() != Scope.intType)
            throw new SemanticError("Not find int main()", mainFunc.getPos());
        if (mainFunc.getParameters().size() > 0)
            throw new SemanticError("int main() contains arguments!", mainFunc.getPos());
    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(ClassBodyNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {
        ClassType newClass = new ClassType(node.getIdentifier(), globalScope, node);
        node.setClassType(newClass);
        globalScope.addClass(newClass);
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
        Type ReturnType = Type.getReturnType(node.getTypeOrVoid());
        List<ParameterNode> list = node.getParameterList();
        FunctionSymbol newFunc = new FunctionSymbol(ReturnType, node.getIdentifier(), node, globalScope, list);
        globalScope.addFunction(newFunc);
        node.setFunctionSymbol(newFunc);
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
