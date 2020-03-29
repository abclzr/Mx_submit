package AST;

import Parser.MxBaseVisitor;
import Parser.MxParser;
import Utils.Position;
import Utils.SyntaxError;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;


import java.util.ArrayList;
import java.util.List;

public class ASTBuilder extends MxBaseVisitor<ASTNode> {
    @Override
    public ASTNode visitProgram(MxParser.ProgramContext ctx) {
        List<ASTNode> list = new ArrayList<>();
        for (ParserRuleContext decl : ctx.declaration()) {
            list.add(visit(decl));
        }
        return new ProgramNode(new Position(ctx.getStart()), list);
    }//done

    @Override
    public ASTNode visitDeclaration(MxParser.DeclarationContext ctx) {
        if (ctx.classDeclaration() != null) return visit(ctx.classDeclaration());
        if (ctx.functionDeclaration() != null) return visit(ctx.functionDeclaration());
        if (ctx.variableDeclaration() != null) return visit(ctx.variableDeclaration());
        return null;
    }//done

    @Override
    public ASTNode visitClassDeclaration(MxParser.ClassDeclarationContext ctx) {
        String id = ctx.Identifier().getText();
        ClassBodyNode classBody = (ClassBodyNode) visit(ctx.classBody());
        return new ClassDeclNode(new Position(ctx.getStart()), id,
                classBody.getVarDeclNodeList(), classBody.getMethodDeclNodeList());
    }//done

    @Override
    public ASTNode visitClassBody(MxParser.ClassBodyContext ctx) {
        List<VarDeclNode> v = new ArrayList<>();
        List<MethodDeclNode> m = new ArrayList<>();
        for (ParserRuleContext c : ctx.classBodyDeclaration()) {
            ASTNode s = visit(c);
            if (s instanceof VarDeclNode) {
                v.add((VarDeclNode) s);
            }
            if (s instanceof MethodDeclNode) {
                m.add((MethodDeclNode) s);
            }
        }
        return new ClassBodyNode(new Position(ctx.getStart()), v, m);
    }//done

    @Override
    public ASTNode visitClassBodyDeclaration(MxParser.ClassBodyDeclarationContext ctx) {
        if (ctx.variableDeclaration() != null)
            return visit(ctx.variableDeclaration());
        if (ctx.methodDeclaration() != null)
            return visit(ctx.methodDeclaration());
        return null;
    }//done

    @Override
    public ASTNode visitMethodDeclaration(MxParser.MethodDeclarationContext ctx) {
        TypeOrVoidNode tpov;
        boolean isCon;
        if (ctx.typeOrVoid() != null) {
            tpov = (TypeOrVoidNode) visit(ctx.typeOrVoid());
            isCon = false;
        } else {
            tpov = null;
            isCon = true;
        }
        String id = ctx.Identifier().getText();
        ParametersNode pl = (ParametersNode) visit(ctx.parameters());
        BlockNode bl = (BlockNode) visit(ctx.block());
        if (pl == null) return new MethodDeclNode(new Position(ctx.getStart()), tpov, id, null, bl, isCon);
        else return new MethodDeclNode(new Position(ctx.getStart()), tpov, id, pl.getParameterList(), bl, isCon);
    }//done

    @Override
    public ASTNode visitVariableDeclaration(MxParser.VariableDeclarationContext ctx) {
        List<VarDecoratorNode> list = new ArrayList<>();
        for (ParserRuleContext d : ctx.variableDecorator()) {
            list.add((VarDecoratorNode) visit(d));
        }
        return new VarDeclNode(new Position(ctx.getStart()), (TypeNode) visit(ctx.type()), list);
    }//done

    @Override
    public ASTNode visitVariableDecorator(MxParser.VariableDecoratorContext ctx) {
        if (ctx.expression() != null)
            return new VarDecoratorNode(new Position(ctx.getStart()), ctx.Identifier().getText(),
                (ExpressionNode) visit(ctx.expression()));
        else
            return new VarDecoratorNode(new Position(ctx.getStart()), ctx.Identifier().getText(), null);
    }//done

    @Override
    public ASTNode visitType(MxParser.TypeContext ctx) {
        String s = ctx.getText();
        int d = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == '[')
                ++d;
        }
        if (ctx.classType() != null) {
            ClassTypeNode c = (ClassTypeNode) visit(ctx.classType());
            return new TypeNode(new Position(ctx.getStart()), c.getIdentifier(), false, d);
        }
        if (ctx.primitiveType() != null) {
            PrimitiveTypeNode p = (PrimitiveTypeNode) visit(ctx.primitiveType());
            return new TypeNode(new Position(ctx.getStart()), p.getIdentifier(), true, d);
        }
        return null;
    }//done

    @Override
    public ASTNode visitTypeOrVoid(MxParser.TypeOrVoidContext ctx) {
        if (ctx.type() != null)
            return new TypeOrVoidNode(new Position(ctx.getStart()), (TypeNode) visit(ctx.type()), false);
        else
            return new TypeOrVoidNode(new Position(ctx.getStart()), null, true);
    }//done

    @Override
    public ASTNode visitClassType(MxParser.ClassTypeContext ctx) {
        return new ClassTypeNode(new Position(ctx.getStart()), ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitPrimitiveType(MxParser.PrimitiveTypeContext ctx) {
        return new PrimitiveTypeNode(new Position(ctx.getStart()), ctx.getText());
    }//done

    @Override
    public ASTNode visitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx) {
        TypeOrVoidNode tpov = (TypeOrVoidNode) visit(ctx.typeOrVoid());
        String id = ctx.Identifier().getText();
        ParametersNode pl;
        if (ctx.parameters() != null) pl = (ParametersNode) visit(ctx.parameters());
        else pl = null;
        BlockNode bl = (BlockNode) visit(ctx.block());
        if (pl != null) return new FuncDeclNode(new Position(ctx.getStart()), tpov, id, pl.getParameterList(), bl);
        else return new FuncDeclNode(new Position(ctx.getStart()), tpov, id, null, bl);
    }//done

    @Override
    public ASTNode visitParameters(MxParser.ParametersContext ctx) {
        if (ctx.parameterList() != null)
            return visit(ctx.parameterList());
        else
            return null;
    }//done

    @Override
    public ASTNode visitParameterList(MxParser.ParameterListContext ctx) {
        List<ParameterNode> list = new ArrayList<>();
        for (ParserRuleContext c : ctx.parameter()) {
            list.add((ParameterNode) visit(c));
        }
        return new ParametersNode(new Position(ctx.getStart()), list);
    }//done

    @Override
    public ASTNode visitParameter(MxParser.ParameterContext ctx) {
        return new ParameterNode(new Position(ctx.getStart()),
                (TypeNode) visit(ctx.type()), ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitBlock(MxParser.BlockContext ctx) {
        return visit(ctx.blockStatement());
    }//done

    @Override
    public ASTNode visitBlockStatement(MxParser.BlockStatementContext ctx) {
        List<StatementNode> list = new ArrayList<>();
        for (ParserRuleContext c : ctx.statement()) {
            ASTNode ac = visit(c);
            if (ac != null) list.add((StatementNode) ac);
        }
        return new BlockNode(new Position(ctx.getStart()), list);
    }//done

    @Override
    public ASTNode visitBlockStmt(MxParser.BlockStmtContext ctx) {
        BlockNode block = (BlockNode) visit(ctx.block());
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.BLOCK, block.getStatementList());
    }//done

    @Override
    public ASTNode visitIfStmt(MxParser.IfStmtContext ctx) {
        ExpressionNode expr = (ExpressionNode) visit(ctx.expression());
        StatementNode st1, st2;
        st1 = (StatementNode) visit(ctx.statement(0));
        if (ctx.statement().size() > 1)
            st2 = (StatementNode) visit(ctx.statement(1));
        else
            st2 = null;
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.IF, expr, st1, st2);
    }//done

    @Override
    public ASTNode visitForStmt(MxParser.ForStmtContext ctx) {
        ForControlNode forCon = (ForControlNode) visit(ctx.forControl());
        StatementNode st = (StatementNode) visit(ctx.statement());
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.FOR, forCon, st);
    }//done

    @Override
    public ASTNode visitWhileStmt(MxParser.WhileStmtContext ctx) {
        ExpressionNode expr = (ExpressionNode) visit(ctx.expression());
        StatementNode st = (StatementNode) visit(ctx.statement());
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.WHILE, expr, st);
    }//done

    @Override
    public ASTNode visitReturnStmt(MxParser.ReturnStmtContext ctx) {
        if (ctx.expression() != null) {
            ExpressionNode expr = (ExpressionNode) visit(ctx.expression());
            return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.RETURN, expr);
        } else {
            return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.RETURN, true);
        }
    }//done

    @Override
    public ASTNode visitBreakStmt(MxParser.BreakStmtContext ctx) {
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.BREAK);
    }//done

    @Override
    public ASTNode visitContinueStmt(MxParser.ContinueStmtContext ctx) {
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.CONTINUE);
    }//done

    @Override
    public ASTNode visitSemiStmt(MxParser.SemiStmtContext ctx) {
        return null;
    }//done

    @Override
    public ASTNode visitExprStmt(MxParser.ExprStmtContext ctx) {
        ExpressionNode expr = (ExpressionNode) visit(ctx.expression());
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.EXPR, expr);
    }//done

    @Override
    public ASTNode visitVariableDeclStmt(MxParser.VariableDeclStmtContext ctx) {
        VarDeclNode varDecl = (VarDeclNode) visit(ctx.variableDeclaration());
        return new StatementNode(new Position(ctx.getStart()), StatementNode.Type.VARDECL, varDecl);
    }//done

    @Override
    public ASTNode visitNewExpr(MxParser.NewExprContext ctx) {
        CreatorNode c = (CreatorNode) visit(ctx.creator());
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.NEW, c);
    }//done

    @Override
    public ASTNode visitPrefixExpr(MxParser.PrefixExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.PRE,
                ctx.prefix.getText(), (ExpressionNode) visit(ctx.expression()));
    }//done

    @Override
    public ASTNode visitMethodCallExpr(MxParser.MethodCallExprContext ctx) {
        if (ctx.expressionList() != null)
            return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.CALL,
                (ExpressionNode) visit(ctx.expression()),
                ((ExpressionListNode) visit(ctx.expressionList())).getList());
        else
            return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.CALL,
                    (ExpressionNode) visit(ctx.expression()),
                    new ArrayList<>());
    }//done

    @Override
    public ASTNode visitPrimaryExpr(MxParser.PrimaryExprContext ctx) {
        return visit(ctx.primary());
    }//done

    @Override
    public ASTNode visitArrayExpr(MxParser.ArrayExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.ARRAY,
                (ExpressionNode) visit(ctx.expression(0)),
                (ExpressionNode) visit(ctx.expression(1)));
    }//done

    @Override
    public ASTNode visitMemberExpr(MxParser.MemberExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.MEMBER,
                  (ExpressionNode) visit(ctx.expression()), ctx.op.getText(), ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitBinaryOpExpr(MxParser.BinaryOpExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.BINARY,
                (ExpressionNode) visit(ctx.expression(0)), ctx.op.getText(),
                (ExpressionNode) visit(ctx.expression(1)));
    }//done

    @Override
    public ASTNode visitPostfixExpr(MxParser.PostfixExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.POST,
                (ExpressionNode) visit(ctx.expression()), ctx.postfix.getText());
    }//done

    @Override
    public ASTNode visitExpressionList(MxParser.ExpressionListContext ctx) {
        List<ExpressionNode> list = new ArrayList<>();
        for (ParserRuleContext expr : ctx.expression()) {
            list.add((ExpressionNode) visit(expr));
        }
        return new ExpressionListNode(new Position(ctx.getStart()), list);
    }//done

    @Override
    public ASTNode visitErrorCreator(MxParser.ErrorCreatorContext ctx) {
        throw new SyntaxError("Invalid new operation", new Position(ctx.getStart()));
    }//done

    @Override
    public ASTNode visitArrayCreator(MxParser.ArrayCreatorContext ctx) {
        List<ExpressionNode> e = new ArrayList<>();
        for (ParserRuleContext expr : ctx.expression())
            e.add((ExpressionNode) visit(expr));
        int d = 0; String str = ctx.getText();
        for (int i = 0; i < str.length(); ++i)
            if (str.charAt(i) == '[') ++d;
        if (ctx.classType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.classType().getText(), false, e, d);
        if (ctx.primitiveType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.primitiveType().getText(), true, e, d);
        return null;
    }//done

    @Override
    public ASTNode visitConstructorCreator(MxParser.ConstructorCreatorContext ctx) {
        if (ctx.classType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.classType().getText(), false, null, 0);
        if (ctx.primitiveType() != null)
            return new CreatorNode(new Position(ctx.getStart()), ctx.primitiveType().getText(), true, null, 0);
        return null;
    }//done

    @Override
    public ASTNode visitParenthesizedExpr(MxParser.ParenthesizedExprContext ctx) {
        return visit(ctx.expression());
    }//done

    @Override
    public ASTNode visitThisExpr(MxParser.ThisExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.THIS);
    }//done

    @Override
    public ASTNode visitLiteralExpr(MxParser.LiteralExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.LITERAL,
                (LiteralNode) visit(ctx.literal()));
    }//done

    @Override
    public ASTNode visitNameExpr(MxParser.NameExprContext ctx) {
        return new ExpressionNode(new Position(ctx.getStart()), ExpressionNode.Type.IDENTIFIER,
                ctx.Identifier().getText());
    }//done

    @Override
    public ASTNode visitLiteral(MxParser.LiteralContext ctx) {
        if (ctx.IntegerLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.INT,
                    null,
                    Integer.valueOf(ctx.IntegerLiteral().getText()));
        if (ctx.StringLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.STRING,
                    ctx.StringLiteral().getText(),
                    0);
        if (ctx.BooleanLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.BOOL,
                    ctx.BooleanLiteral().getText(),
                    0);
        if (ctx.NullLiteral() != null)
            return new LiteralNode(new Position(ctx.getStart()),
                    LiteralNode.literalType.NULL,
                    ctx.NullLiteral().getText(),
                    0);
        return null;
    }//done

    @Override
    public ASTNode visitForControl(MxParser.ForControlContext ctx) {
        ExpressionNode ex1;
        if (ctx.initial != null) ex1 = (ExpressionNode) visit(ctx.initial);
        else ex1 = null;
        ExpressionNode ex2;
        if (ctx.condition != null) ex2 = (ExpressionNode) visit(ctx.condition);
        else ex2 = null;
        ExpressionNode ex3;
        if (ctx.update != null) ex3 = (ExpressionNode) visit(ctx.update);
        else ex3 = null;
        return new ForControlNode(new Position(ctx.getStart()), ex1, ex2, ex3);
    }//done
}
