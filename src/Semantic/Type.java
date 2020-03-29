package Semantic;

import AST.TypeNode;
import AST.TypeOrVoidNode;
import Utils.Position;

public abstract class Type {
    private String typeName;

    public Type(String str) {
        this.typeName = str;
    }

    public static Type getReturnType(TypeOrVoidNode node) {
        if (node.isVoid()) return Scope.voidType;
        else {
            String str = node.getTypeName();
            Type type = Scope.globalScope.findType(str, node.getPosition());
            int d = node.getTypeNode().getDimension();
            if (d > 0) return new ArrayType(str, type, d);
            else return type;
        }
    }

    public static Type getType(TypeNode node) {
        String str = node.getIdentifier();
        Type type = Scope.globalScope.findType(str, node.getPosition());
        int d = node.getDimension();
        if (d > 0) return new ArrayType(str, type, d);
        else return type;
    }

    public String getTypeName() {
        return typeName;
    }

    public abstract boolean isArrayType();

    public abstract boolean isPrimitiveType();

    public abstract boolean isStringType();

    public abstract boolean isClassType();

    public abstract boolean isNullType();

    public abstract boolean isSameTypeOf(Type type);

    //TODO : type size
}
