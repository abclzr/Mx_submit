package Semantic;

import Utils.Position;
import Utils.SemanticError;

public class PrimitiveType extends Type {
    private Scope scope;

    public PrimitiveType(String str) {
        super(str);
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public boolean isArrayType() {
        return false;
    }

    @Override
    public boolean isPrimitiveType() {
        return true;
    }

    @Override
    public boolean isStringType() {
        return super.getTypeName().equals("string");
    }

    @Override
    public boolean isClassType() {
        return false;
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
