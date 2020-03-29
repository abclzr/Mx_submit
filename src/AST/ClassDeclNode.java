package AST;

import Semantic.ClassType;
import Utils.Position;

import java.util.List;

/*
classDeclaration: CLASS Identifier classBody;
classBody: '{' classBodyDeclaration* '}';
classBodyDeclaration: variableDeclaration
                    | methodDeclaration;
 */

public class ClassDeclNode extends ASTNode {
    private String identifier;
    private List<VarDeclNode> varDeclNodeList;
    private List<MethodDeclNode> methodDeclNodeList;
    private ClassType classType;

    public void setClassType(ClassType cT) {
        this.classType = cT;
    }

    public ClassType getClassType() {
        return classType;
    }

    public ClassDeclNode(Position pos, String id, List<VarDeclNode> v, List<MethodDeclNode> m) {
        super(pos);
        this.identifier = id;
        this.varDeclNodeList = v;
        this.methodDeclNodeList = m;
    }

    public String getIdentifier() {
        return identifier;
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
