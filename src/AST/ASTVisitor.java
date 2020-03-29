package AST;

import Semantic.ArrayType;
import Semantic.ClassType;
import Semantic.Scope;
import Semantic.Type;

public abstract class ASTVisitor {
    public Scope globalScope;
    public ASTVisitor(Scope globalScope) {this.globalScope = globalScope;}
    public abstract void visit(ProgramNode node);
    public abstract void visit(BlockNode node);
    public abstract void visit(ClassBodyNode node);
    public abstract void visit(ClassDeclNode node);
    public abstract void visit(ClassTypeNode node);
    public abstract void visit(CreatorNode node);
    public abstract void visit(ExpressionListNode node);
    public abstract void visit(ExpressionNode node);
    public abstract void visit(ForControlNode node);
    public abstract void visit(FuncDeclNode node);
    public abstract void visit(LiteralNode node);
    public abstract void visit(MethodDeclNode node);
    public abstract void visit(ParameterNode node);
    public abstract void visit(ParametersNode node);
    public abstract void visit(PrimitiveTypeNode node);
    public abstract void visit(StatementNode node);
    public abstract void visit(TypeNode node);
    public abstract void visit(TypeOrVoidNode node);
    public abstract void visit(VarDeclNode node);
    public abstract void visit(VarDecoratorNode node);
}
