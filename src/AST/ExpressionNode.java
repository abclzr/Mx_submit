package AST;

import Semantic.FunctionSymbol;
import Semantic.Scope;
import Semantic.Type;
import Utils.Position;

import java.util.List;

/*
expression
    : primary                                    #primaryExpr
    | expression op='.' Identifier               #memberExpr
    | expression '[' expression ']'              #arrayExpr
    | expression '(' expressionList? ')'         #methodCallExpr
    | NEW creator                                #newExpr
    | expression postfix=('++' | '--')           #postfixExpr
    | prefix=('+'|'-'|'++'|'--') expression      #prefixExpr
    | prefix=('~'|'!') expression                #prefixExpr
    | expression op=('*'|'/'|'%') expression     #binaryOpExpr
    | expression op=('+'|'-') expression         #binaryOpExpr
    | expression op=('<<' | '>>>' | '>>') expression #binaryOpExpr
    | expression op=('<=' | '>=' | '>' | '<') expression #binaryOpExpr
    | expression op=('==' | '!=') expression     #binaryOpExpr
    | expression op='&' expression               #binaryOpExpr
    | expression op='^' expression               #binaryOpExpr
    | expression op='|' expression               #binaryOpExpr
    | expression op='&&' expression              #binaryOpExpr
    | expression op='||' expression              #binaryOpExpr
    | <assoc=right> expression op='=' expression #binaryOpExpr
    ;

primary
    : '(' expression ')' #parenthesizedExpr
    | THIS               #thisExpr
    | literal            #literalExpr
    | Identifier         #nameExpr
    ;
 */

public class ExpressionNode extends ASTNode {
    private String id, op;
    private Type type;
    private LiteralNode literal;
    private ExpressionNode expr1, expr2;
    private List<ExpressionNode> exprList;
    private CreatorNode creator;
    private Scope scope;
    private Semantic.Type exprType;
    private boolean isFunction;
    private boolean isLeftValue;
    private boolean isConstInt;
    private int constInt;
    private Semantic.FunctionSymbol funcSymbol;//only used for isFunction == true

    public void setIsConstInt(boolean i) {
        isConstInt = i;
    }

    public void setConstInt(int i) {
        isConstInt = true;
        constInt = i;
    }

    public boolean isConstInt() {
        return isConstInt;
    }

    public int getConstInt() {
        return constInt;
    }

    public void setIsLeftValue(boolean i) {
        this.isLeftValue = i;
    }

    public boolean isLeftValue() {
        return isLeftValue;
    }

    public void setFunction(Semantic.FunctionSymbol fs) {
        isFunction = true;
        funcSymbol = fs;
    }

    public FunctionSymbol getFuncSymbol() {
        return funcSymbol;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setExprType(Semantic.Type exprType) {
        this.exprType = exprType;
    }

    public Semantic.Type getExprType() {
        return exprType;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public ExpressionNode(Position pos, Type tp, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.IDENTIFIER;
        this.id = id;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public String getIdentifier() {
        return id;
    }

    public String getOp() {
        return op;
    }

    public Type getType() {
        return type;
    }

    public LiteralNode getLiteralNode() {
        return literal;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op, ExpressionNode e2) {
        super(pos);
        this.type = tp;
        assert tp == Type.BINARY;
        this.expr1 = e1;
        this.op = op;
        this.expr2 = e2;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getBinaryExpr1() {
        return expr1;
    }

    public ExpressionNode getBinaryExpr2() {
        return expr2;
    }

    public ExpressionNode(Position pos, Type tp, String op, ExpressionNode e1) {
        super(pos);
        this.type = tp;
        assert tp == Type.PRE;
        this.op = op;
        this.expr1 = e1;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getPreExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode e1, String op) {
        super(pos);
        this.type = tp;
        assert tp == Type.POST;
        this.expr1 = e1;
        this.op = op;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getPostExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, LiteralNode li) {
        super(pos);
        this.type = tp;
        assert tp == Type.LITERAL;
        this.literal = li;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode(Position pos, Type tp) {
        super(pos);
        this.type = tp;
        assert tp == Type.THIS;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex, String op, String id) {
        super(pos);
        this.type = tp;
        assert tp == Type.MEMBER && op.equals(".");
        this.expr1 = ex;
        this.op = op;
        this.id = id;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getMemberExpr() {
        return expr1;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, ExpressionNode ex2) {
        super(pos);
        this.type = tp;
        assert tp == Type.ARRAY;
        this.expr1 = ex1;
        this.expr2 = ex2;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getArrayExprBefore() {
        return expr1;
    }

    public ExpressionNode getArrayExprAfter() {
        return expr2;
    }

    public ExpressionNode(Position pos, Type tp, ExpressionNode ex1, List<ExpressionNode> list) {
        super(pos);
        this.type = tp;
        assert tp == Type.CALL;
        this.expr1 = ex1;
        this.exprList = list;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public ExpressionNode getCallExpr() {
        return expr1;
    }

    public List<ExpressionNode> getCallExprList() {
        return exprList;
    }

    public ExpressionNode(Position pos, Type tp, CreatorNode cr) {
        super(pos);
        this.type = tp;
        assert tp == Type.NEW;
        this.creator = cr;
        this.isFunction = false;
        this.isLeftValue = false;
        this.isConstInt = false;
    }

    public CreatorNode getCreator() {
        return creator;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public enum Type {THIS, LITERAL, IDENTIFIER, MEMBER, ARRAY, CALL, NEW, POST, PRE, BINARY,}
}
