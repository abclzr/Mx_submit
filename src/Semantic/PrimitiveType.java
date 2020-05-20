package Semantic;

import Utils.Position;
import Utils.SemanticError;
import Utils.SyntaxError;

public class PrimitiveType extends Type {
    private Scope scope;

    @Override
    public void setWidth() {
        if (isStringType()) super.width = 4;
        else if (super.getTypeName().equals("bool")) super.width = 1;
        else if (super.getTypeName().equals("int")) super.width = 4;
        else if (super.getTypeName().equals("void")) super.width = 0;
        else if (super.getTypeName().equals("null")) super.width = 4;
        else throw new SyntaxError("Primitive type not found!", null);
    }

    @Override
    public int getAllocWidth() {
        return super.width;
    }

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
