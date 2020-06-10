package Frontend;

import AST.*;
import Backend.BaseRegister;
import Backend.MachineRegister;
import Semantic.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class IRBuilder extends ASTVisitor {
    private CodeSegment globalVarSegment;
    private ConstantPoolTable constantPoolTable;
    private List<CodeSegment> segmentList;
    private CodeSegment currentSegment;
    private BasicBlock currentBlock;
    private List<CopyInstruction> constPoolWriteBack;
    private CodeSegment stringAdd;
    private CodeSegment stringCmp;
    private List<VirtualRegister> globalVarList;
    private List<CodeSegment> defaultConstructorList;
    //things to write back:
    //const pool pointer
    //global variable address
    //function pointer

    private List<Type> typeList;

    public IRBuilder(Scope globalScope) {
        super(globalScope);
        segmentList = new ArrayList<>();
        constPoolWriteBack = new ArrayList<>();
        defaultConstructorList = new ArrayList<>();
        MachineRegister.init();
    }

    public void printall() {
        segmentList.forEach(x -> {
            x.printall();
        });
        System.out.println("\t.section\t.sdata,\"aw\",@progbits\n");
        globalVarList.forEach(x -> {
            System.out.println("\t.globl\t" + x.getGlobalVarName() + "\t#@" + x.getGlobalVarName());
            if (x.getWidth() == 4) {
                System.out.println("\t.p2align\t2");
                System.out.println(x.getGlobalVarName() + ":");
                System.out.println("\t.word\t" + 0);
            } else {
                System.out.println(x.getGlobalVarName() + ":");
                System.out.println("\t.byte\t" + 0);
            }
        });
        constantPoolTable.printall();
    }

    @Override
    public void visit(ProgramNode node) {
        CodeSegment lengthSegment = new CodeSegment(Scope.lengthSymbol);
        lengthSegment.setFuncName("string_length");
        CodeSegment substringSegment = new CodeSegment(Scope.substringSymbol);
        substringSegment.setFuncName("string_substring");
        CodeSegment parseIntSegment = new CodeSegment(Scope.parseIntSymbol);
        parseIntSegment.setFuncName("string_parseInt");
        CodeSegment ordSegment = new CodeSegment(Scope.ordSymbol);
        ordSegment.setFuncName("string_ord");
        CodeSegment printSegment = new CodeSegment(Scope.printSymbol);
        CodeSegment printlnSegment = new CodeSegment(Scope.printlnSymbol);
        CodeSegment printIntSegment = new CodeSegment(Scope.printIntSymbol);
        CodeSegment printlnIntSegment = new CodeSegment(Scope.printlnIntSymbol);
        CodeSegment getStringSegment = new CodeSegment(Scope.getStringSymbol);
        CodeSegment getIntSegment = new CodeSegment(Scope.getIntSymbol);
        CodeSegment toStringSegment = new CodeSegment(Scope.toStringSymbol);
        CodeSegment sizeSegment = new CodeSegment(Scope.sizeSymbol);
        sizeSegment.setFuncName("_array_size");

        constantPoolTable = new ConstantPoolTable();
        //get Class size
        Map<String, Type> typeMap = globalScope.getTypeMap();
        typeMap.forEach((name, type) -> {
            type.setWidth();
        });

        FunctionSymbol stringAddSymbol = new FunctionSymbol(Scope.stringType, "string_add", null, null, globalScope, null);
        FunctionSymbol stringCmpSymbol = new FunctionSymbol(Scope.intType, "strcmp", null, null, globalScope, null);
        stringAdd = new CodeSegment(stringAddSymbol);
        stringCmp = new CodeSegment(stringCmpSymbol);

        //set class member's offset and
        //create all code segments and its parameters' virtual registers for all class methods
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof ClassDeclNode) {
                //set class member's offset
                AtomicInteger offset = new AtomicInteger();
                ((ClassDeclNode) x).getVarDeclNodeList().forEach(y -> {
                    Type type = Type.getType(y.getType());
                    y.getVarDecoratorList().forEach(z -> {
                        z.getVariableSymbol().setOffsetInClass(offset.get());
                        offset.addAndGet(type.getWidth());
                    });
                });
                //create all code segments and its parameters' virtual registers for all class methods
                ((ClassDeclNode) x).getMethodDeclNodeList().forEach(y -> {
                    CodeSegment cs = new CodeSegment(y.getFuncSymbol());
                    cs.setFuncName(((ClassDeclNode) x).getIdentifier() + "." + cs.getFuncName());
                    cs.setClassType(((ClassDeclNode) x).getClassType());
                    cs.setRaPointer(new VirtualRegister(cs, Scope.intType).askForSpace());
                    segmentList.add(cs);
                    VirtualRegister vr;
                    //constructor has no parameters
                    if (!y.isConstructor()) {
                        vr = new VirtualRegister(cs, ((ClassDeclNode) x).getClassType()).askForSpace();
                        cs.setThisPointer(vr);
                        cs.addParam(vr);
                        y.getParameterList().forEach(z -> {
                            VariableSymbol var = y.getFuncSymbol().getScope().findVarInScope(z.getIdentifier(), null);
                            VirtualRegister vv = new VirtualRegister(cs, Type.getType(z.getType())).askForSpace();
                            var.setVirtualRegister(vv);
                            cs.addParam(vv);
                        });
                    } else {
                        vr = new VirtualRegister(cs, ((ClassDeclNode) x).getClassType()).askForSpace();
                        cs.setThisPointer(vr);
                        ((ClassDeclNode) x).getClassType().setCreator(cs);
                    }
                });
                if (((ClassDeclNode) x).getClassType().getCreator() == null) {
                    CodeSegment cs = new CodeSegment(null);
                    cs.setClassType(((ClassDeclNode) x).getClassType());
                    cs.setFuncName(((ClassDeclNode) x).getIdentifier());
                    ((ClassDeclNode) x).getClassType().setCreator(cs);
                    defaultConstructorList.add(cs);
                }
            }
        });

        //create all code segments for all functions and prepare its parameters' virtual registers
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof FuncDeclNode) {
                FunctionSymbol func = ((FuncDeclNode) x).getFunctionSymbol();
                CodeSegment cs = new CodeSegment(func);
                cs.setRaPointer(new VirtualRegister(cs, Scope.intType).askForSpace());
                segmentList.add(cs);
                if (((FuncDeclNode) x).getParameterList() != null)
                    ((FuncDeclNode) x).getParameterList().forEach(y -> {
                        VirtualRegister vr = new VirtualRegister(cs, Type.getType(y.getType())).askForSpace();
                        VariableSymbol var = func.getScope().findVarInScope(y.getIdentifier(), null);
                        var.setVirtualRegister(vr);
                        cs.addParam(vr);
                    });
            }
        });

        //scan global variables
        globalVarSegment = new CodeSegment(null);
        FunctionSymbol mainSymbol = globalScope.findFuncInScope("main", null);
        CodeSegment mainSegment = mainSymbol.getCodeSegment();
//      currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, Scope.intType, mainSegment, new ArrayList<>()));
        currentSegment = mainSegment;
        currentBlock = mainSegment.getHeadBlock();
        globalVarList = new ArrayList<>();
        node.getDeclNodeList().forEach(x -> {
            if (x instanceof VarDeclNode) {
                Type type = Type.getType(((VarDeclNode) x).getType());
                ((VarDeclNode) x).getVarDecoratorList().forEach(y -> {
                    if (y.getExpr() == null) {
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type).askForSpace();
                        nv.setGlobalVarName(y.getIdentifier());
                        globalVarList.add(nv);
                        y.getVariableSymbol().setVirtualRegister(nv);
                    } else {
                        ComputExprValue(y.getExpr());
                        VirtualRegister nv = new VirtualRegister(globalVarSegment, type).askForSpace();
                        nv.setGlobalVarName(y.getIdentifier());
                        globalVarList.add(nv);
                        y.getVariableSymbol().setVirtualRegister(nv);
                        currentBlock.addInst(new GStoreInstruction(IRInstruction.op.GSTORE, nv.getGlobalVarName(), y.getExpr().getVirtualRegister(), type));
                    }
                });
            }
        });
        mainSegment.setTailBlock(currentBlock);

        defaultConstructorList.forEach(cs -> {
            currentSegment = cs;
            currentBlock = cs.getTailBlock();
            VirtualRegister ra = new VirtualRegister(currentSegment, Scope.intType).askForSpace();
            cs.setRaPointer(ra);
            VirtualRegister th = new VirtualRegister(currentSegment, cs.getClassType()).askForSpace();
            currentBlock.addInst(new MallocInstruction(IRInstruction.op.MALLOC, th, cs.getClassType().getAllocWidth()));
            cs.setConstructorReturnValue(th);
            AtomicInteger wid = new AtomicInteger();
            ((ClassDeclNode) cs.getClassType().getDefine()).getVarDeclNodeList().forEach(x -> {
                Type type = Type.getType(x.getType());
                currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, th, wid.get(), null, type));
                wid.addAndGet(type.getWidth());
            });
            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, th, cs));
        });

        segmentList.forEach(cs -> {
            if (cs.getFunctionSymbol() != null) {
                if (cs.getFunctionSymbol().getType() == null) {
                    currentSegment = cs;
                    currentBlock = cs.getTailBlock();
                    currentBlock.addInst(new MallocInstruction(IRInstruction.op.MALLOC, cs.getThisPointer(), cs.getClassType().getAllocWidth()));
                    currentSegment.setConstructorReturnValue(cs.getThisPointer());
                    CollectStmt(cs.getFunctionSymbol().getBlockContext().getStatementList(), null, null);
                    currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, cs.getThisPointer(), currentSegment));
                } else {
                    currentSegment = cs;
                    currentBlock = cs.getTailBlock();
                    int i = 0;
                    for (var p : cs.getParams()) {
                        if (i <= 7)
                            currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, p, (BaseRegister) MachineRegister.get("a"+i)));
                        else
                            currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, p, (BaseRegister) MachineRegister.get("sp"), p.getAddrValue(), p.getWidth()));
                        i++;
                    }
                    CollectStmt(cs.getFunctionSymbol().getBlockContext().getStatementList(), null, null);
                    currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, null, currentSegment));
                }
            }
        });

        defaultConstructorList.forEach(x -> {segmentList.add(x);});
    }

    void CollectStmt(StatementNode blockNode, BasicBlock continueBlock, BasicBlock breakBlock) {
        List<StatementNode> blockNodeList = new ArrayList<>();
        blockNodeList.add(blockNode);
        CollectStmt(blockNodeList, continueBlock, breakBlock);
    }

    void CollectStmt(List<StatementNode> blockNodeList, BasicBlock continueBlock, BasicBlock breakBlock) {
        if(blockNodeList == null) return;
        for (StatementNode node : blockNodeList) {
            if (node == null) continue;
            switch (node.getStmtType()) {
                    case BLOCK:
                        CollectStmt(node.getBlockStmtList(), continueBlock, breakBlock);
                        break;
                    case IF:
                        ComputExprValue(node.getIfExpr());
                        VirtualRegister ifExpr = node.getIfExpr().getVirtualRegister();
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, ifExpr, false, bb2));
                        currentBlock.addPost(bb2);
                        currentBlock = bb1;
                        CollectStmt(node.getIfStmt1(), continueBlock, breakBlock);
                        if (node.getIfStmt2() == null) {
                            currentBlock = bb2;
                        } else {
                            BasicBlock bb3 = bb2.split();
                            currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb3));
                            currentBlock.addPost(bb3);
                            currentBlock = bb2;
                            CollectStmt(node.getIfStmt2(), continueBlock, breakBlock);
                            currentBlock = bb3;
                        }
                        break;
                    case FOR:
                        BasicBlock forInit = currentBlock;
                        BasicBlock forCond = currentBlock.split();
                        BasicBlock forBody = forCond.split();
                        BasicBlock forUpdate = forBody.split();
                        BasicBlock forAfter = forUpdate.split();
                        currentBlock = forInit;
                        ComputExprValue(node.getForControl().getInitialExpr());
                        currentBlock = forCond;
                        ComputExprValue(node.getForControl().getConditionExpr());
                        if (node.getForControl().getConditionExpr() != null) {
                            VirtualRegister fC = node.getForControl().getConditionExpr().getVirtualRegister();
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, fC, false, forAfter));
                            currentBlock.addPost(forAfter);
                        }
                        currentBlock = forBody;
                        if (node.getForStatement() != null)
                            CollectStmt(node.getForStatement(), forUpdate, forAfter);
                        currentBlock = forUpdate;
                        ComputExprValue(node.getForControl().getUpdateExpr());
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, forCond));
                        currentBlock.addPost(forCond);
                        currentBlock = forAfter;
                        break;
                    case WHILE:
                        BasicBlock whileCond = currentBlock.split();
                        BasicBlock whileBody = whileCond.split();
                        BasicBlock whileAfter = whileBody.split();
                        currentBlock = whileCond;
                        ComputExprValue(node.getWhileExpr());
                        VirtualRegister wC = node.getWhileExpr().getVirtualRegister();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, wC, false, whileAfter));
                        currentBlock.addPost(whileAfter);
                        currentBlock = whileBody;
                        CollectStmt(node.getWhileStmt(), whileCond, whileAfter);
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, whileCond));
                        currentBlock.addPost(whileCond);
                        currentBlock = whileAfter;
                        break;
                    case RETURN:
                        if (node.getReturnExpr() != null) {
                            ComputExprValue(node.getReturnExpr());
                            VirtualRegister returnValue = node.getReturnExpr().getVirtualRegister();
                            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, returnValue, currentSegment));
                        } else if (currentSegment.getFunctionSymbol().getType() != null){
                            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, null, currentSegment));
                        } else {
                            currentBlock.addInst(new ReturnInstruction(IRInstruction.op.RETURN, currentSegment.getConstructorReturnValue(), currentSegment));
                        }
                        break;
                    case BREAK:
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, breakBlock));
                        currentBlock.addPost(breakBlock);
                        break;
                    case CONTINUE:
                        currentBlock.addInst(new JumpInstruction(IRInstruction.op.JUMP, continueBlock));
                        currentBlock.addPost(continueBlock);
                        break;
                    case EXPR:
                        ComputExprValue(node.getExpr());
                        break;
                    case VARDECL:
                        Type type = Type.getType(node.getVarDecl().getType());
                        node.getVarDecl().getVarDecoratorList().forEach(v -> {
                            VirtualRegister vn = new VirtualRegister(currentSegment, type);
                            v.getVariableSymbol().setVirtualRegister(vn);
                            if (v.getExpr() != null) {
                                ComputExprValue(v.getExpr());
                                VirtualRegister vv = v.getExpr().getVirtualRegister();
                                currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, vv));
                            } else
                                currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                        });
                        break;
            }
        }
    }

    public VirtualRegister NewArray(List<VirtualRegister> list, int dimension, int tmp, int listSize, Type tp) {
        if (tmp > listSize) {
            VirtualRegister rt = new VirtualRegister(currentSegment, tp);
            if (tp.isClassType()) {
                CodeSegment creator = ((ClassType) tp).getCreator();
                currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, rt, Scope.intType, creator, new ArrayList<>()));
            } else {
                currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, rt, 0));
            }
            return rt;
        }

        Type ctxType =  ((ArrayType) tp).getDimensionMinusOne();
        VirtualRegister len = list.get(tmp - 1);
        VirtualRegister malloc_size = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister malloc_size_p4 = new VirtualRegister(currentSegment, Scope.intType);
        VirtualRegister jump_size = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, jump_size, ctxType.getWidth()));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, malloc_size, len, "*", jump_size));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, malloc_size_p4, malloc_size, "+", 4));
        VirtualRegister rt = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new MallocInstruction(IRInstruction.op.MALLOC, rt, malloc_size_p4));
        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, rt, 0, len, Scope.intType));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, rt, rt, "+", 4));
        VirtualRegister pointer = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, pointer, rt));
        VirtualRegister endPointer = new VirtualRegister(currentSegment, Scope.intType);
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, endPointer, pointer, "+", malloc_size));
        currentBlock = currentBlock.split();
        BasicBlock loopBlock = currentBlock;
        VirtualRegister ctx = NewArray(list, dimension, tmp + 1, listSize, ctxType);
        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, pointer, 0, ctx, ctxType));
        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, pointer, pointer, "+", jump_size));
        currentBlock.addInst(new BranchInstruction(IRInstruction.op.BRANCH, pointer, "<", endPointer, loopBlock));
        currentBlock.addPost(loopBlock);
        currentBlock = currentBlock.split();
        return rt;
    }

    public void ComputExprValue(ExpressionNode node) {
        VirtualRegister vn = null;
        if (node == null) return;
        switch (node.getType()) {
            case THIS:
                node.setVirtualRegister(currentSegment.getThisPointer());
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        String s = constantPoolTable.allocate(node.getLiteralNode().getStr());
                        vn = new VirtualRegister(currentSegment, Scope.stringType);
                        currentBlock.addInst(new LAInstruction(IRInstruction.op.GADD, vn, s, Scope.intType));
                        node.setVirtualRegister(vn);
                        break;
                    case INT:
                        int num = node.getLiteralNode().getNum();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, num));
                        node.setVirtualRegister(vn);
                        break;
                    case BOOL:
                        boolean b = node.getLiteralNode().getStr().equals("true");
                        vn = new VirtualRegister(currentSegment, Scope.boolType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, b ? 1 : 0));
                        node.setVirtualRegister(vn);
                        break;
                    case NULL:
                        vn = new VirtualRegister(currentSegment, Scope.nullType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                        node.setVirtualRegister(vn);
                        break;
                }
                break;
            case IDENTIFIER:
                if (!node.isFunction()) {
                    assert node.isLeftValue();
                    VariableSymbol var = node.getScope().findVar(node.getIdentifier(), node.getPosition());
                    VirtualRegister varReg = var.getVirtualRegister();
                    Address offset;
                    if (varReg != null) {
                        if (varReg.getInCodeSegment() == globalVarSegment) {
                            vn = new VirtualRegister(currentSegment, var.getType());
                            currentBlock.addInst(new GLoadInstruction(IRInstruction.op.GLOAD, vn, varReg.getGlobalVarName(), var.getType()));
                            node.setVirtualRegister(vn);
                        } else {
                            node.setVirtualRegister(varReg);
                        }
                    } else {
                        offset = var.getAddrInClass();
                        VirtualRegister thi = currentSegment.getThisPointer();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, thi, offset.getAddr(), var.getType()));
                        node.setVirtualRegister(vn);
                    }
                } else {
                    FunctionSymbol func = node.getScope().findFunc(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(func.getCodeSegment());
                }
                break;
            case MEMBER:
                ComputExprValue(node.getMemberExpr());
                VirtualRegister c = node.getMemberExpr().getVirtualRegister();
                Type t = node.getMemberExpr().getExprType();
                Scope classScope;
                if (t instanceof ClassType) classScope = ((ClassType) t).getScope();
                else if (t instanceof ArrayType) classScope = ArrayType.getScope();
                else classScope = ((PrimitiveType) t).getScope();

                if (node.isFunction()) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(fs.getCodeSegment());
                    node.setVirtualRegister(c);
                } else {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    vn = new VirtualRegister(currentSegment, vs.getType());
                    currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, c, vs.getOffsetInClass(), vs.getType()));
                    node.setVirtualRegister(vn);
                }
                break;
            case ARRAY:
                ComputExprValue(node.getArrayExprBefore());
                ComputExprValue(node.getArrayExprAfter());
                VirtualRegister bef = node.getArrayExprBefore().getVirtualRegister();
                VirtualRegister aft = node.getArrayExprAfter().getVirtualRegister();
                int a = node.getExprType().getWidth();
                VirtualRegister off = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off, aft, "*", a));
                VirtualRegister off2 = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off2, bef, "+", off));
                vn = new VirtualRegister(currentSegment, node.getExprType());
                currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, off2, 0, node.getExprType()));
                node.setVirtualRegister(vn);
                break;
            case CALL:
                FunctionSymbol func = node.getCallExpr().getFuncSymbol();
                List<VirtualRegister> list = new ArrayList<>();
                if (func.getScope().getFatherScope() != globalScope) {
                    if(node.getCallExpr().getMemberExpr() == null)
                        list.add(currentSegment.getThisPointer());
                    else {
                        ComputExprValue(node.getCallExpr());
                        list.add(node.getCallExpr().getVirtualRegister());
                    }
                }
                for (ExpressionNode ex : node.getCallExprList()) {
                    ComputExprValue(ex);
                    list.add(ex.getVirtualRegister());
                }
                if (node.getExprType() != Scope.voidType) {
                    vn = new VirtualRegister(currentSegment, node.getExprType());
                    currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, node.getExprType(), func.getCodeSegment(), list));
                    node.setVirtualRegister(vn);
                } else {
                    currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, node.getExprType(), func.getCodeSegment(), list));
                    node.setVirtualRegister(null);
                }
                break;
            case NEW:
                CreatorNode cr = node.getCreator();
                List<VirtualRegister> lis = new ArrayList<>();
                cr.getExpr().forEach(x -> {
                    ComputExprValue(x);
                    lis.add(x.getVirtualRegister());
                });
                if (cr.getExprType().isArrayType()) {
                    vn = NewArray(lis, cr.getDimension(), 1, lis.size(), cr.getExprType());
                } else {
                    //assert class type
                    CodeSegment creator = ((ClassType) cr.getExprType()).getCreator();
                    vn = new VirtualRegister(currentSegment, Scope.intType);
                    currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, Scope.intType, creator, new ArrayList<>()));
                }
                node.setVirtualRegister(vn);
                break;
            case POST:
                if (node.getPostExpr().getType() == ExpressionNode.Type.IDENTIFIER) {
                    VariableSymbol var = node.getPostExpr().getScope().findVar(node.getPostExpr().getIdentifier(), node.getPosition());
                    VirtualRegister varReg = var.getVirtualRegister();
                    if (varReg != null && varReg.getInCodeSegment() != globalVarSegment) {
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, varReg));
                        if(node.getOp().equals("++"))
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, varReg, varReg, "+", 1));
                        else
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, varReg, varReg, "-", 1));
                        node.setVirtualRegister(vn);
                        break;
                    }
                }
                ComputExprAddr(node.getPostExpr());
                VirtualRegister addr = node.getPostExpr().getVirtualRegister();
                vn = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, vn, addr, 0, Scope.intType));
                VirtualRegister res = new VirtualRegister(currentSegment, Scope.intType);
                switch (node.getOp()) {
                    case "++":
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, res, vn, "+", 1));
                        break;
                    case "--":
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, res, vn, "-", 1));
                        break;
                }
                currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, addr, 0, res, Scope.intType));
                node.setVirtualRegister(vn);
                break;
            case PRE:
                switch (node.getOp()) {
                    case "++":
                    case "--":
                        if (node.getPreExpr().getType() == ExpressionNode.Type.IDENTIFIER) {
                            VariableSymbol var = node.getPreExpr().getScope().findVar(node.getPreExpr().getIdentifier(), node.getPosition());
                            VirtualRegister varReg = var.getVirtualRegister();
                            if (varReg != null && varReg.getInCodeSegment() != globalVarSegment) {
                                if(node.getOp().equals("++"))
                                    currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, varReg, varReg, "+", 1));
                                else
                                    currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, varReg, varReg, "-", 1));
                                node.setVirtualRegister(varReg);
                                break;
                            }
                        }
                        ComputExprAddr(node.getPreExpr());
                        VirtualRegister address = node.getPreExpr().getVirtualRegister();
                        VirtualRegister num = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, num, address, 0, Scope.intType));
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        if (node.getOp().equals("++"))
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "+", 1));
                        else
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "-", 1));
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, address, 0, vn, Scope.intType));
                        node.setVirtualRegister(vn);
                        break;
                    case "+":
                    case "-":
                    case "~":
                        ComputExprValue(node.getPreExpr());
                        VirtualRegister num2 = node.getPreExpr().getVirtualRegister();
                        if (node.getOp().equals("+"))
                            node.setVirtualRegister(num2);
                        else if (node.getOp().equals("-")) {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, null, "-", num2));
                            node.setVirtualRegister(vn);
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num2, "^", -1));
                            node.setVirtualRegister(vn);
                        }
                        break;
                    case "!":
                        ComputExprValue(node.getPreExpr());
                        VirtualRegister bool_res = node.getPreExpr().getVirtualRegister();
                        vn = new VirtualRegister(currentSegment, Scope.boolType);
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        BasicBlock bb3 = bb2.split();
                        currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, bool_res, true, bb2));
                        currentBlock.addPost(bb2);
                        bb1.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                        bb1.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb3));
                        bb1.addPost(bb3);
                        bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));//NOTICE: not SSA!!
                        currentBlock = bb3;
                        node.setVirtualRegister(vn);
                        break;
                }
                break;
            case BINARY:
                VirtualRegister r1 = null, r2 = null;
                if (!node.getOp().equals("&&") && !node.getOp().equals("||")) {
                    if (node.getOp().equals("=")) {
                        if (node.getBinaryExpr1().getType() == ExpressionNode.Type.IDENTIFIER) {
                            VariableSymbol var = node.getBinaryExpr1().getScope().findVar(node.getBinaryExpr1().getIdentifier(), node.getPosition());
                            VirtualRegister varReg = var.getVirtualRegister();
                            if (varReg != null && varReg.getInCodeSegment() != globalVarSegment) {
                                ComputExprValue(node.getBinaryExpr2());
                                r2 = node.getBinaryExpr2().getVirtualRegister();
                                currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, varReg, r2));
                                node.setVirtualRegister(varReg);
                                break;
                            }
                        }
                        ComputExprAddr(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    } else {
                        ComputExprValue(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    }
                }
                switch (node.getOp()) {
                    case "+":
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, "+", r2));
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.intType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, vn, Scope.intType, stringAdd, li));
                        }
                        break;
                    case "<=": case ">=": case "<": case ">":
                        if (Scope.intType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        } else {
                            VirtualRegister result = new VirtualRegister(currentSegment, Scope.intType);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, result, Scope.intType, stringCmp, li));
                            if (node.getOp().equals("<=")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "<=", 0));
                            } else if (node.getOp().equals(">=")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, ">=", 0));
                            } else if (node.getOp().equals("<")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "<", 0));
                            } else {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, ">", 0));
                            }
                        }
                        break;
                    case "-": case "*": case "/": case "%": case "<<": case ">>":  case "&": case "^": case "|":
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        break;
                    case "&&": case "||":
                        ComputExprValue(node.getBinaryExpr1());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        BasicBlock bb1 = currentBlock.split();
                        BasicBlock bb2 = bb1.split();
                        BasicBlock bb3 = bb2.split();
                        BasicBlock bb4 = bb3.split();
                        if (node.getOp().equals("&&")) {
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r1, false, bb3));
                            currentBlock.addPost(bb3);
                            currentBlock = bb1;
                            ComputExprValue(node.getBinaryExpr2());
                            r2 = node.getBinaryExpr2().getVirtualRegister();
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r2, false, bb3));
                            currentBlock.addPost(bb3);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                            bb2.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb4));
                            bb2.addPost(bb4);
                            bb3.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                            currentBlock = bb4;//not SSA!
                        } else {
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r1, true, bb3));
                            currentBlock.addPost(bb3);
                            currentBlock = bb1;
                            ComputExprValue(node.getBinaryExpr2());
                            r2 = node.getBinaryExpr2().getVirtualRegister();
                            currentBlock.addInst(new CjumpInstruction(IRInstruction.op.CJUMP, r2, true, bb3));
                            currentBlock.addPost(bb3);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            bb2.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 0));
                            bb2.addInst(new JumpInstruction(IRInstruction.op.JUMP, bb4));
                            bb2.addPost(bb4);
                            bb3.addInst(new CopyInstruction(IRInstruction.op.COPY, vn, 1));
                            currentBlock = bb4;//not SSA!
                        }
                        break;
                    case "==": case "!=":
                        if (Scope.stringType.isSameTypeOf(node.getBinaryExpr1().getExprType())) {
                            VirtualRegister result = new VirtualRegister(currentSegment, Scope.intType);
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            List<VirtualRegister> li = new ArrayList<>();
                            li.add(r1); li.add(r2);
                            currentBlock.addInst(new CallInstruction(IRInstruction.op.CALL, result, Scope.intType, stringCmp, li));
                            if (node.getOp().equals("==")) {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "==", 0));
                            } else {
                                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, result, "!=", 0));
                            }
                        } else {
                            vn = new VirtualRegister(currentSegment, Scope.boolType);
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, r1, node.getOp(), r2));
                        }
                        break;
                    case "=":
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, r1, 0, r2, node.getBinaryExpr1().getExprType()));
                        vn = r2;
                        break;
                }
                node.setVirtualRegister(vn);
                break;
        }
    }

    public void ComputExprAddr(ExpressionNode node) {
        VirtualRegister vn = null;
        switch (node.getType()) {
            case THIS:
                break;
            case LITERAL:
                switch (node.getLiteralNode().getLiteralType()) {
                    case STRING:
                        break;
                    case INT:
                        break;
                    case BOOL:
                        break;
                    case NULL:
                        break;
                }
                break;
            case IDENTIFIER:
                if (!node.isFunction()) {
                    assert node.isLeftValue();
                    VariableSymbol var = node.getScope().findVar(node.getIdentifier(), node.getPosition());
                    VirtualRegister varReg = var.getVirtualRegister();
                    Address offset;
                    if (varReg != null) {
                        offset = varReg.getAddr();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        if (varReg.getInCodeSegment() == globalVarSegment) {
                            currentBlock.addInst(new LAInstruction(IRInstruction.op.GADD, vn, varReg.getGlobalVarName(), var.getType()));
                            node.setVirtualRegister(vn);
                        } else {
                            currentBlock.addInst(new SAddInstruction(IRInstruction.op.SADD, vn, offset, var.getType()));
                            node.setVirtualRegister(vn);
                        }
                    } else {
                        offset = var.getAddrInClass();
                        VirtualRegister th = currentSegment.getThisPointer();
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, th, "+", offset.getAddr()));
                        node.setVirtualRegister(vn);
                    }
                } else {
                    FunctionSymbol func = node.getScope().findFunc(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(func.getCodeSegment());
                }
                break;
            case MEMBER:
                ComputExprValue(node.getMemberExpr());
                VirtualRegister c = node.getMemberExpr().getVirtualRegister();
                Type t = node.getMemberExpr().getExprType();
                Scope classScope;
                if (t instanceof ClassType) classScope = ((ClassType) t).getScope();
                else if (t instanceof ArrayType) classScope = ArrayType.getScope();
                else classScope = ((PrimitiveType) t).getScope();

                if (node.isFunction()) {
                    FunctionSymbol fs = classScope.findFuncInScope(node.getIdentifier(), node.getPosition());
                    node.setFuncPointer(fs.getCodeSegment());
                    node.setVirtualRegister(c);
                } else {
                    VariableSymbol vs = classScope.findVarInScope(node.getIdentifier(), node.getPosition());
                    vn = new VirtualRegister(currentSegment, Scope.intType);
                    VirtualRegister offset = new VirtualRegister(currentSegment, Scope.intType);
                    currentBlock.addInst(new CopyInstruction(IRInstruction.op.COPY, offset, vs.getOffsetInClass()));
                    currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, c, "+", offset));
                    node.setVirtualRegister(vn);
                }
                break;
            case ARRAY:
                ComputExprValue(node.getArrayExprBefore());
                ComputExprValue(node.getArrayExprAfter());
                VirtualRegister bef = node.getArrayExprBefore().getVirtualRegister();
                VirtualRegister aft = node.getArrayExprAfter().getVirtualRegister();
                int a = node.getExprType().getWidth();
                VirtualRegister off = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off, aft, "*", a));
                VirtualRegister off2 = new VirtualRegister(currentSegment, Scope.intType);
                currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, off2, bef, "+", off));
                vn = off2;
                node.setVirtualRegister(vn);
                break;
            case CALL:
                break;
            case NEW:
                break;
            case POST:
                break;
            case PRE:
                switch (node.getOp()) {
                    case "++":
                    case "--":
                        ComputExprAddr(node.getPreExpr());
                        VirtualRegister address = node.getPreExpr().getVirtualRegister();
                        VirtualRegister num = new VirtualRegister(currentSegment, Scope.intType);
                        currentBlock.addInst(new LoadInstruction(IRInstruction.op.LOAD, num, address, 0, Scope.intType));
                        vn = new VirtualRegister(currentSegment, Scope.intType);
                        if (node.getOp().equals("++"))
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "+", 1));
                        else
                            currentBlock.addInst(new BinaryInstruction(IRInstruction.op.BINARY, vn, num, "-", 1));
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, address, 0, vn, Scope.intType));
                        node.setVirtualRegister(address);
                        break;
                    case "+":
                    case "-":
                    case "~":
                        break;
                    case "!":
                        break;
                }
                break;
            case BINARY:
                VirtualRegister r1 = null, r2 = null;
                if (!node.getOp().equals("&&") && !node.getOp().equals("||")) {
                    if (node.getOp().equals("=")) {
                        ComputExprAddr(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    } else {
                        ComputExprValue(node.getBinaryExpr1());
                        ComputExprValue(node.getBinaryExpr2());
                        r1 = node.getBinaryExpr1().getVirtualRegister();
                        r2 = node.getBinaryExpr2().getVirtualRegister();
                    }
                }
                switch (node.getOp()) {
                    case "+":
                        break;
                    case "<=": case ">=": case "<": case ">":
                        break;
                    case "-": case "*": case "/": case "%": case "<<": case ">>":  case "&": case "^": case "|":
                        break;
                    case "&&": case "||":
                        break;
                    case "==": case "!=":
                        break;
                    case "=":
                        currentBlock.addInst(new StoreInstruction(IRInstruction.op.STORE, r1, 0, r2, node.getBinaryExpr1().getExprType()));
                        vn = r1;
                        break;
                }
                node.setVirtualRegister(vn);
                break;
        }
    }

    @Override
    public void visit(BlockNode node) {

    }

    @Override
    public void visit(ClassBodyNode node) {

    }

    @Override
    public void visit(ClassDeclNode node) {

    }

    @Override
    public void visit(ClassTypeNode node) {

    }

    @Override
    public void visit(CreatorNode node) {

    }

    @Override
    public void visit(ExpressionListNode node) {

    }

    @Override
    public void visit(ExpressionNode node) {

    }

    @Override
    public void visit(ForControlNode node) {

    }

    @Override
    public void visit(FuncDeclNode node) {

    }

    @Override
    public void visit(LiteralNode node) {

    }

    @Override//Remember to set this-pointer
    public void visit(MethodDeclNode node) {

    }

    @Override
    public void visit(ParameterNode node) {

    }

    @Override
    public void visit(ParametersNode node) {

    }

    @Override
    public void visit(PrimitiveTypeNode node) {

    }

    @Override
    public void visit(StatementNode node) {

    }

    @Override
    public void visit(TypeNode node) {

    }

    @Override
    public void visit(TypeOrVoidNode node) {

    }

    @Override
    public void visit(VarDeclNode node) {

    }

    @Override
    public void visit(VarDecoratorNode node) {

    }

    public void optimize() {
        segmentList.forEach(x -> {
            x.optimize();
        });
    }

    public void livenessAnalysis() {
        segmentList.forEach(x -> {
            x.livenessAnalysis();
        });
    }

    public void codegen() {
        System.out.println("\t.text\n");
        segmentList.forEach(x -> {
            x.codegen();
        });
        System.out.println("\t.section\t.sdata,\"aw\",@progbits\n");
        globalVarList.forEach(x -> {
            System.out.println("\t.globl\t" + x.getGlobalVarName() + "\t#@" + x.getGlobalVarName());
            if (x.getWidth() == 4) {
                System.out.println("\t.p2align\t2");
                System.out.println(x.getGlobalVarName() + ":");
                System.out.println("\t.word\t" + 0);
            } else {
                System.out.println(x.getGlobalVarName() + ":");
                System.out.println("\t.byte\t" + 0);
            }
        });
        constantPoolTable.printall();
    }

    public void registerAllocate() {
        segmentList.forEach(x -> {
            x.registerAllocate();
        });
    }
}
