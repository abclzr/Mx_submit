package Semantic;

import Utils.Position;
import Utils.SemanticError;

public class ArrayType extends Type {
    private Type type;
    static private Scope arrayScope;
    private int dimension;

    public ArrayType(String str, Type tp, int di) {
        super(str);
        this.type = tp;
        this.dimension = di;
    }

    public static void setScope(Scope scope) {
        arrayScope = scope;
    }

    public static Scope getScope() {
        return arrayScope;
    }

    public Type getType() {
        return type;
    }

    public int getDimension() {
        return dimension;
    }

    public void dimensionPlusOne() {
        dimension++;
    }

    public Type getDimensionMinusOne() {
        if (dimension > 1)
            return new ArrayType(super.getTypeName(), this.type, this.dimension - 1);
        else
            return this.type;
    }

    @Override
    public int getAllocWidth() {
        return super.width;
    }

    @Override
    public void setWidth() {
        super.width = 4;
    }

    @Override
    public boolean isArrayType() {
        return true;
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
        return false;
    }

    @Override
    public boolean isNullType() {
        return false;
    }

    @Override
    public boolean isSameTypeOf(Type type) {
        if (type.isArrayType()) {
            return this.type == ((ArrayType) type).getType() && this.dimension == ((ArrayType) type).getDimension();
        } else {
            return false;
        }
    }
}
