package Frontend;

import Semantic.ClassType;
import Semantic.FunctionSymbol;

import java.util.ArrayList;
import java.util.List;

public class CodeSegment {
    private FunctionSymbol functionSymbol;
    private int stackStorage;
    private VirtualRegister thisPointer;
    private VirtualRegister raPointer;
    private BasicBlock headBlock;
    private BasicBlock tailBlock;
    private ClassType classType;
    private int vsNum;
    private VirtualRegister constructorReturnValue;
    private String funcName;
    private List<VirtualRegister> params;

    public List<VirtualRegister> getParams() {
        return params;
    }

    public void addParam(VirtualRegister a) {
        params.add(a);
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setRaPointer(VirtualRegister raPointer) {
        this.raPointer = raPointer;
    }

    public VirtualRegister getRaPointer() {
        return raPointer;
    }

    public int getStackStorage() {
        return stackStorage;
    }

    public void setClassType(ClassType classType) {
        this.classType = classType;
    }

    public ClassType getClassType() {
        return classType;
    }

    public void setThisPointer(VirtualRegister thisPointer) {
        this.thisPointer = thisPointer;
    }

    public VirtualRegister getThisPointer() {
        return thisPointer;
    }

    CodeSegment(FunctionSymbol inFunc) {
        this.functionSymbol = inFunc;
        if (inFunc != null) inFunc.setCodeSegment(this);
        stackStorage = 0;
        thisPointer = null;
        vsNum = 0;
        this.headBlock = new BasicBlock(this);
        this.tailBlock = this.headBlock;
        if (inFunc != null) this.funcName = inFunc.getName();
        params = new ArrayList<>();
    }

    public FunctionSymbol getFunctionSymbol() {
        return functionSymbol;
    }

    public int Allocate(int width) {
        stackStorage += width;
        return stackStorage - width;
    }

    public int getBBNum() {
        return vsNum++;
    }

    public BasicBlock getHeadBlock() {
        return headBlock;
    }

    public void setTailBlock(BasicBlock tailBlock) {
        this.tailBlock = tailBlock;
    }

    public BasicBlock getTailBlock() {
        return tailBlock;
    }

    public void printall() {
        System.out.println(this.funcName + ":");
        BasicBlock cs = headBlock;
        while (cs != null) {
            cs.printall();
            cs = cs.getPos();
        }
        System.out.println("");
    }

    public void codegen() {
        System.out.println("\t.globl\t" + this.funcName + "\t\t\t\t\t # -- Begin function " + this.funcName + "\n");
        System.out.println("\t.p2align\t2\n");
        System.out.println("\t.type\t" + this.funcName + ",@function\n");
        System.out.println(this.funcName + ":");
        IRInstruction.ADDI("sp", "sp", -getStackStorage());
        int i = 0;
        for (VirtualRegister param : params) {
            if (param.getWidth() == 4)
                IRInstruction.sw("a" + i, param.getAddrValue() + "(sp)");
            else
                IRInstruction.sb("a" + i, param.getAddrValue() + "(sp)");
            i++;
            if (i > 7) break;
        }
        if (getRaPointer() != null)
            IRInstruction.sw("ra", getRaPointer().getAddrValue() + "(sp)");
        BasicBlock cs = headBlock;
        while (cs != null) {
            cs.codegen();
            cs = cs.getPos();
        }
        System.out.println("\t\t\t\t\t\t # -- End function\n");
    }

    public void setConstructorReturnValue(VirtualRegister th) {
        constructorReturnValue = th;
    }

    public VirtualRegister getConstructorReturnValue() {
        return constructorReturnValue;
    }
}
