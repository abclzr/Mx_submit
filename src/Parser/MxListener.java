// Generated from C:/Users/Dedicatus545/Desktop/Mx_star_Compiler/src/Parser\Mx.g4 by ANTLR 4.8
package Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link MxParser}.
 */
public interface MxListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void enterProgram(MxParser.ProgramContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#program}.
	 * @param ctx the parse tree
	 */
	void exitProgram(MxParser.ProgramContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#declaration}.
	 * @param ctx the parse tree
	 */
	void enterDeclaration(MxParser.DeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#declaration}.
	 * @param ctx the parse tree
	 */
	void exitDeclaration(MxParser.DeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassDeclaration(MxParser.ClassDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 */
	void enterClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classBody}.
	 * @param ctx the parse tree
	 */
	void exitClassBody(MxParser.ClassBodyContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterClassBodyDeclaration(MxParser.ClassBodyDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classBodyDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitClassBodyDeclaration(MxParser.ClassBodyDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterMethodDeclaration(MxParser.MethodDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#methodDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitMethodDeclaration(MxParser.MethodDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclaration(MxParser.VariableDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#variableDecorator}.
	 * @param ctx the parse tree
	 */
	void enterVariableDecorator(MxParser.VariableDecoratorContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#variableDecorator}.
	 * @param ctx the parse tree
	 */
	void exitVariableDecorator(MxParser.VariableDecoratorContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(MxParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(MxParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#typeOrVoid}.
	 * @param ctx the parse tree
	 */
	void enterTypeOrVoid(MxParser.TypeOrVoidContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#typeOrVoid}.
	 * @param ctx the parse tree
	 */
	void exitTypeOrVoid(MxParser.TypeOrVoidContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 */
	void enterClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#classType}.
	 * @param ctx the parse tree
	 */
	void exitClassType(MxParser.ClassTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void enterPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#primitiveType}.
	 * @param ctx the parse tree
	 */
	void exitPrimitiveType(MxParser.PrimitiveTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void enterFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#functionDeclaration}.
	 * @param ctx the parse tree
	 */
	void exitFunctionDeclaration(MxParser.FunctionDeclarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameters}.
	 * @param ctx the parse tree
	 */
	void enterParameters(MxParser.ParametersContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameters}.
	 * @param ctx the parse tree
	 */
	void exitParameters(MxParser.ParametersContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void enterParameterList(MxParser.ParameterListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameterList}.
	 * @param ctx the parse tree
	 */
	void exitParameterList(MxParser.ParameterListContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#parameter}.
	 * @param ctx the parse tree
	 */
	void enterParameter(MxParser.ParameterContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#parameter}.
	 * @param ctx the parse tree
	 */
	void exitParameter(MxParser.ParameterContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 */
	void enterBlock(MxParser.BlockContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#block}.
	 * @param ctx the parse tree
	 */
	void exitBlock(MxParser.BlockContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#blockStatement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStatement(MxParser.BlockStatementContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBlockStmt(MxParser.BlockStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blockStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBlockStmt(MxParser.BlockStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterIfStmt(MxParser.IfStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code ifStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitIfStmt(MxParser.IfStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterForStmt(MxParser.ForStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code forStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitForStmt(MxParser.ForStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterWhileStmt(MxParser.WhileStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code whileStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitWhileStmt(MxParser.WhileStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterReturnStmt(MxParser.ReturnStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code returnStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitReturnStmt(MxParser.ReturnStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterBreakStmt(MxParser.BreakStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code breakStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitBreakStmt(MxParser.BreakStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterContinueStmt(MxParser.ContinueStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code continueStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitContinueStmt(MxParser.ContinueStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code semiStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterSemiStmt(MxParser.SemiStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code semiStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitSemiStmt(MxParser.SemiStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterExprStmt(MxParser.ExprStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code exprStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitExprStmt(MxParser.ExprStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableDeclStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void enterVariableDeclStmt(MxParser.VariableDeclStmtContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableDeclStmt}
	 * labeled alternative in {@link MxParser#statement}.
	 * @param ctx the parse tree
	 */
	void exitVariableDeclStmt(MxParser.VariableDeclStmtContext ctx);
	/**
	 * Enter a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterNewExpr(MxParser.NewExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code newExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitNewExpr(MxParser.NewExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrefixExpr(MxParser.PrefixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code prefixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrefixExpr(MxParser.PrefixExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code methodCallExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMethodCallExpr(MxParser.MethodCallExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code methodCallExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMethodCallExpr(MxParser.MethodCallExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPrimaryExpr(MxParser.PrimaryExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code primaryExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPrimaryExpr(MxParser.PrimaryExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterArrayExpr(MxParser.ArrayExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitArrayExpr(MxParser.ArrayExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMemberExpr(MxParser.MemberExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code memberExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMemberExpr(MxParser.MemberExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code binaryOpExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterBinaryOpExpr(MxParser.BinaryOpExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code binaryOpExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitBinaryOpExpr(MxParser.BinaryOpExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code postfixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterPostfixExpr(MxParser.PostfixExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code postfixExpr}
	 * labeled alternative in {@link MxParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitPostfixExpr(MxParser.PostfixExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void enterExpressionList(MxParser.ExpressionListContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#expressionList}.
	 * @param ctx the parse tree
	 */
	void exitExpressionList(MxParser.ExpressionListContext ctx);
	/**
	 * Enter a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterErrorCreator(MxParser.ErrorCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code errorCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitErrorCreator(MxParser.ErrorCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterArrayCreator(MxParser.ArrayCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code arrayCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitArrayCreator(MxParser.ArrayCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code constructorCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void enterConstructorCreator(MxParser.ConstructorCreatorContext ctx);
	/**
	 * Exit a parse tree produced by the {@code constructorCreator}
	 * labeled alternative in {@link MxParser#creator}.
	 * @param ctx the parse tree
	 */
	void exitConstructorCreator(MxParser.ConstructorCreatorContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parenthesizedExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterParenthesizedExpr(MxParser.ParenthesizedExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parenthesizedExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitParenthesizedExpr(MxParser.ParenthesizedExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterThisExpr(MxParser.ThisExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code thisExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitThisExpr(MxParser.ThisExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterLiteralExpr(MxParser.LiteralExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code literalExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitLiteralExpr(MxParser.LiteralExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code nameExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void enterNameExpr(MxParser.NameExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code nameExpr}
	 * labeled alternative in {@link MxParser#primary}.
	 * @param ctx the parse tree
	 */
	void exitNameExpr(MxParser.NameExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(MxParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(MxParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link MxParser#forControl}.
	 * @param ctx the parse tree
	 */
	void enterForControl(MxParser.ForControlContext ctx);
	/**
	 * Exit a parse tree produced by {@link MxParser#forControl}.
	 * @param ctx the parse tree
	 */
	void exitForControl(MxParser.ForControlContext ctx);
}