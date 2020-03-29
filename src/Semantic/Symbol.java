package Semantic;

import AST.ASTNode;
import Utils.Position;

public abstract class Symbol {
    private String name;
    private Type type;
    private ASTNode define;
    private Position pos;

    public Symbol(Type tp, String na, ASTNode def, Position pos) {
        this.type = tp;
        this.name = na;
        this.define = def;
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public ASTNode getDefine() {
        return define;
    }

    public abstract boolean isVariableSymbol();

    public abstract boolean isFunctionSymbol();
}
