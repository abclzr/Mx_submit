
import AST.ASTNode;
import AST.ASTBuilder;
import AST.ProgramNode;
import Parser.MxErrorListener;
import Parser.MxParser;
import Parser.MxLexer;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import Semantic.ClassAndFuncVisitor;
import Semantic.ClassMemberVisitor;
import Semantic.Scope;
import Semantic.SemanticCheckVisitor;
import Utils.AccessError;
import Utils.SyntaxError;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.BailErrorStrategy;

import static java.lang.System.exit;

public class Main {
    public static ASTNode BuildAST(InputStream in) throws Exception {
        MxLexer lexer = new MxLexer(CharStreams.fromStream(in));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new MxErrorListener());
        MxParser parser = new MxParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(new MxErrorListener());
        ParseTree root = parser.program();
        ASTBuilder a = new ASTBuilder();
        return a.visit(root);
    }

    public static void main(String... args) throws Exception {
        try {
            InputStream in = new FileInputStream("test.txt");
            ASTNode root = BuildAST(in);

            Scope globalScope = new Scope(null);
            Scope.globalScope = globalScope;
            globalScope.initGlobalScope();
            new ClassAndFuncVisitor(globalScope).visit((ProgramNode) root);
            new ClassMemberVisitor(globalScope).visit((ProgramNode) root);
            new SemanticCheckVisitor(globalScope).visit((ProgramNode) root);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            //throw new RuntimeException();
            exit(1);
        }
    }
}