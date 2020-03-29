package AST;

import Utils.Position;

public class LiteralNode extends ASTNode {
    private literalType type;
    private String str;
    private int num;

    public literalType getLiteralType() {
        return type;
    }

    public String getStr() {
        return str;
    }

    public int getNum() {
        return num;
    }

    LiteralNode(Position pos, literalType tp, String s, Integer a) {
        super(pos);
        type = tp;
        str = s;
        num = a;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public enum literalType {STRING, INT, BOOL, NULL};
}
