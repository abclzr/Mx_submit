package Semantic;

import AST.ASTNode;
import AST.ClassDeclNode;
import Frontend.CodeSegment;
import Utils.Position;
import Utils.SemanticError;

public class ClassType extends Type {
    private Scope scope;
    private ASTNode define;
    private int allocWidth;
    private CodeSegment creator;

    public void setCreator(CodeSegment cr) {
        this.creator = cr;
    }

    public CodeSegment getCreator() {
        return this.creator;
    }

    public ClassType(String name, Scope fatherScope, ASTNode def) {
        super(name);
        this.scope = new Scope(fatherScope, this);
        this.define = def;
    }

    public Scope getScope() {
        return scope;
    }

    public ASTNode getDefine() {
        return define;
    }

    @Override
    public int getAllocWidth() {
        return allocWidth;
    }

    @Override
    public void setWidth() {
        super.width = 4;
        this.allocWidth = 0;
        ((ClassDeclNode) define).getVarDeclNodeList().forEach(x -> {
            Type type = Type.getType(x.getType());
            this.allocWidth += type.getWidth();
        });
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isStringType() {
        return false;
    }

    @Override
    public boolean isClassType() {
        return true;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public boolean isSameTypeOf(Type type) {
        return this == type;
    }
}
