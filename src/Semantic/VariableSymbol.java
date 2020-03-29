package Semantic;

import AST.ASTNode;
import Utils.Position;

public class VariableSymbol extends Symbol {
    public VariableSymbol(Type tp, String na, ASTNode def, Position pos) {
        super(tp, na, def, pos);
    }

    @Override
    public boolean isVariableSymbol() {
        return true;
    }

    @Override
    public boolean isFunctionSymbol() {
        return false;
    }
}
