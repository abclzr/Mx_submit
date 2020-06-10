package Frontend;

import Backend.BaseRegister;
import Backend.MachineRegister;
import Backend.RegAllocator;
import Semantic.ClassType;
import Semantic.FunctionSymbol;
import Semantic.Scope;

import java.util.*;

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
    public int tmp;
    public List<String> calleeMachineList;
    public List<VirtualRegister> calleeVirtualList;
    private RegisterAllocator regManager;
    public List<BasicBlock> blocks;
    private Set<BaseRegister> allVirtual;
    private int VirtualNumber = 0;

    public int getVirtualNumber() {
        return VirtualNumber++;
    }

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
        //regManager.init(calleeMachineList);
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
        for (VirtualRegister v : calleeVirtualList) {
            String r = calleeMachineList.get(i++);
            IRInstruction.SW(r, v.getAddrValue(), "sp");
        }
        if (getRaPointer() != null)
            IRInstruction.sw("ra", getRaPointer().getAddrValue() + "(sp)");
        BasicBlock cs = headBlock;
        while (cs != null) {
            cs.codegen(regManager);
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

    public void optimize() {
        BasicBlock cs = headBlock;
        blocks = new ArrayList<>();
        tmp = 0;
        while (cs != null) {
            cs.optimize();
            blocks.add(cs);
            cs = cs.getPos();
        }
    }

    public Set<BaseRegister> getLiveOut(BasicBlock block) {
        return liveOut.get(block);
    }

    private Map<BasicBlock, Set<BaseRegister>> liveIn = new HashMap<>();
    private Map<BasicBlock, Set<BaseRegister>> liveOut = new HashMap<>();
    private Map<BasicBlock, Set<BaseRegister>> gen = new HashMap<>();
    private Map<BasicBlock, Set<BaseRegister>> kill = new HashMap<>();

    public void livenessAnalysis() {
        liveIn = new HashMap<>();
        liveOut = new HashMap<>();
        gen = new HashMap<>();
        kill = new HashMap<>();

        for (var block : blocks) {
            liveIn.put(block, new HashSet<>());
            liveOut.put(block, new HashSet<>());
            gen.put(block, new HashSet<>());
            kill.put(block, new HashSet<>());

            for (var inst : block.newInstList) {
                var t = new HashSet<>(inst.getUse());
                t.removeAll(kill.get(block));
                gen.get(block).addAll(t);
                kill.get(block).addAll(inst.getDef());
            }
        }

        boolean flag = true;
        while (flag) {
            flag = false;
            for (var block : blocks) {
                var newIn = new HashSet<>(liveOut.get(block));
                newIn.removeAll(kill.get(block));
                newIn.addAll(gen.get(block));
                flag |= !newIn.equals(liveIn.get(block));
                liveIn.put(block, newIn);

                var newOut = new HashSet<BaseRegister>();
                for (var succ : block.getPost()) {
                    newOut.addAll(liveIn.getOrDefault(succ, new HashSet<>()));
                }
                flag |= !newOut.equals(liveOut.get(block));
                liveOut.put(block, newOut);
            }
        }
    }

    private RegAllocator regAllocator;

    public void registerAllocate() {
        Set<BaseRegister> copy = new HashSet<>(allVirtual);
        regAllocator = new RegAllocator(this);
        Set<MachineRegister> a = new HashSet<>();
        copy.forEach(x -> {
            if (x.getColor() != null)
                a.add(x.getColor());
            else
                ((VirtualRegister) x).askForSpace();
        });
        calleeMachineList = new ArrayList<>();
        calleeVirtualList = new ArrayList<>();
        for (var s : a) {
            calleeMachineList.add(s.getName());
            calleeVirtualList.add(new VirtualRegister(this, Scope.intType).askForSpace());
        }
    }

    public void addVirtual(BaseRegister v) {
        if (allVirtual == null) allVirtual = new HashSet<>();
        allVirtual.add(v);
    }

    public Set<BaseRegister> getAllVregs() {
        return allVirtual;
    }
}
