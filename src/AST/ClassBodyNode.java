package AST;

import Utils.Position;

import java.util.List;

/*
classDeclaration: CLASS Identifier classBody;
classBody: '{' classBodyDeclaration* '}';
classBodyDeclaration: variableDeclaration
                    | methodDeclaration;
 */

public class ClassBodyNode extends ASTNode {
    private List<VarDeclNode> varDeclNodeList;
    private List<MethodDeclNode> methodDeclNodeList;

    public ClassBodyNode(Position pos, List<VarDeclNode> v, List<MethodDeclNode> m) {
        super(pos);
        this.varDeclNodeList = v;
        this.methodDeclNodeList = m;
    }

    public List<VarDeclNode> getVarDeclNodeList() {
        return varDeclNodeList;
    }

    public List<MethodDeclNode> getMethodDeclNodeList() {
        return methodDeclNodeList;
    }

    @Override
    public void accept(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
