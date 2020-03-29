package AST;

import Semantic.Scope;
import Utils.Position;

import java.beans.Expression;
import java.util.List;

/*
statement
    : block                                             #blockStmt
    | IF '('expression')' statement (ELSE statement)?   #ifStmt
    | FOR '(' forControl ')' statement                  #forStmt
    | WHILE '('expression')' statement                  #whileStmt
    | RETURN expression? ';'                            #returnStmt
    | BREAK ';'                                         #breakStmt
    | CONTINUE ';'                                      #continueStmt
    | ';'                                               #semiStmt
    | expression ';'                                    #exprStmt
    | variableDeclaration                               #variableDeclStmt
    ;
 */

public class StatementNode extends ASTNode {
    private ExpressionNode expr;
    private Type type;
    private StatementNode st1, st2;
    private ForControlNode forControl;
    private List<StatementNode> list;
    private VarDeclNode varDecl;
    private Scope scope;

    public Type getStmtType() {
        return type;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Scope getScope() {
        return scope;
    }

    public StatementNode(Position pos, Type tp, List<StatementNode> li) {
        super(pos);
        this.type = tp;
        assert tp == Type.BLOCK;
        this.list = li;
    }

    public List<StatementNode> getBlockStmtList() {
        return list;
    }

    public StatementNode(Position pos, Type tp, ExpressionNode e, StatementNode s1, StatementNode s2) {
        super(pos);
        this.type = tp;
        assert tp == Type.IF;
        this.expr = e;
        this.st1 = s1;
        this.st2 = s2;
    }

    public ExpressionNode getIfExpr() {
        return expr;
    }

    public StatementNode getIfStmt1() {
        return st1;
    }

    public StatementNode getIfStmt2() {
        return st2;
    }

    public StatementNode(Position pos, Type tp, ForControlNode forC, StatementNode st) {
        super(pos);
        this.type = tp;
        assert tp == Type.FOR;
        this.forControl = forC;
        this.st1 = st;
    }

    public ForControlNode getForControl() {
        return forControl;
    }

    public StatementNode getForStatement() {
        return st1;
    }

    public StatementNode(Position pos, Type tp, ExpressionNode ex, StatementNode st) {
        super(pos);
        this.type = tp;
        assert tp == Type.WHILE;
        this.expr = ex;
        this.st1 = st;
    }

    public ExpressionNode getWhileExpr() {
        return expr;
    }

    public StatementNode getWhileStmt() {
        return st1;
    }

    public StatementNode(Position pos, Type tp, ExpressionNode ex) {
        super(pos);
        this.type = tp;
        assert tp == Type.RETURN || tp == Type.EXPR;
        this.expr = ex;
    }

    public StatementNode(Position pos, Type tp, boolean isVoidReturn) {
        super(pos);
        this.type = tp;
        assert tp == Type.RETURN;
        this.expr = null;
    }

    public ExpressionNode getReturnExpr() {
        return expr;
    }

    public ExpressionNode getExpr() {
        return expr;
    }

    public StatementNode(Position pos, Type tp) {
        super(pos);
        this.type = tp;
        assert tp == Type.BREAK || tp == Type.CONTINUE;
    }

    public StatementNode(Position pos, Type tp, VarDeclNode v) {
        super(pos);
        this.type = tp;
        assert tp == Type.VARDECL;
        this.varDecl = v;
    }

    public VarDeclNode getVarDecl() {
        return varDecl;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }

    public enum Type {BLOCK, IF, FOR, WHILE, RETURN, BREAK, CONTINUE, EXPR, VARDECL}
}
