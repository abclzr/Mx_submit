package Semantic;

import AST.ASTNode;
import Utils.Position;
import Utils.SemanticError;

public class ClassType extends Type {
    private Scope scope;
    private ASTNode define;

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
