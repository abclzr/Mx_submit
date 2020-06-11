
import AST.ASTNode;
import AST.ASTBuilder;
import AST.ProgramNode;
import Frontend.IRBuilder;
import Parser.MxErrorListener;
import Parser.MxParser;
import Parser.MxLexer;

import java.io.*;

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
import static java.lang.System.out;

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
//clang-9 --target=riscv32 -march=rv32ima test.s -c
//export PATH="/usr/local/opt/bin:$PATH"
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

            PrintStream ps;

            //ps = new PrintStream(new FileOutputStream("test.ir"));
            //System.setOut(ps);

            IRBuilder irBuilder = new IRBuilder(globalScope);
            irBuilder.visit((ProgramNode) root);
            //irBuilder.printall();

            //ps = new PrintStream(new FileOutputStream("test.iro"));
            //System.setOut(out);

            irBuilder.globalAnalysis();

            if (IRBuilder.inlineEnable)
                irBuilder.inlineAnalysis();
            //irBuilder.printall();
//            long start = System.currentTimeMillis();
            irBuilder.optimize();

            irBuilder.registerAllocate();
//            long end = System.currentTimeMillis();
//            System.err.println(end-start + "ms");
            ps = new PrintStream(new FileOutputStream("output.s"));
            System.setOut(ps);

            irBuilder.codegen();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            //throw new RuntimeException();
            exit(1);
        }
    }
}