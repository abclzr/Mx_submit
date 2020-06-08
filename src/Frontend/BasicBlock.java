package Frontend;

import AST.StatementNode;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
    private CodeSegment enclosureCodeSegment;
    private List<StatementNode> stmtList;
    private List<IRInstruction> instList;
    private BasicBlock pre;
    private BasicBlock pos;
    private List<BasicBlock> pred;
    private List<BasicBlock> post;
    private int id;
    private IRInstruction headIRInst;

    public BasicBlock getPos() {
        return pos;
    }

    public BasicBlock getPre() {
        return pre;
    }

    public BasicBlock(CodeSegment fa) {
        this.enclosureCodeSegment = fa;
        this.stmtList = new ArrayList<>();
        this.instList = new ArrayList<>();
        this.pred = new ArrayList<>();
        this.post = new ArrayList<>();
        this.pre = null;
        this.pos = null;
        this.id = fa.getBBNum();
    }

    public BasicBlock split() {
        BasicBlock nb = new BasicBlock(enclosureCodeSegment);
        nb.pos = this.pos;
        nb.pre = this;
        this.pos = nb;
        return nb;
    }

    public CodeSegment getEnclosureCodeSegment() {
        return enclosureCodeSegment;
    }

    public List<BasicBlock> getPost() {
        return post;
    }

    public List<BasicBlock> getPred() {
        return pred;
    }

    public List<IRInstruction> getInstList() {
        return instList;
    }

    public List<StatementNode> getStmtList() {
        return stmtList;
    }

    public void addStmtNode(StatementNode a) {
        stmtList.add(a);
    }

    public void addInst(IRInstruction a) {
        instList.add(a);
    }

    public void addPred(BasicBlock a) {
        pred.add(a);
        if (a != null) a.post.add(this);
    }

    public void addPost(BasicBlock a) {
        post.add(a);
        if (a != null) a.pred.add(this);
    }

    public String getName() {
        return enclosureCodeSegment.getFuncName() + ".label_" + this.id;
    }

    public void printall() {
        if (headIRInst != null) {
            printall2();
            return;
        }
        System.out.println("\t" + getName() + ":");
        instList.forEach(x -> {
            System.out.println("\t\t" + x.getMessage());
        });
    }

    public void printall2() {
        System.out.println("\t" + getName() + ":");
        IRInstruction x = headIRInst.getPostInst();
        while (x != null) {
            System.out.println("\t\t" + x.getMessage());
            x = x.getPostInst();
        }
    }

    public void makeIRList() {
        IRInstruction ir = new ReturnInstruction(IRInstruction.op.RETURN, null, null);
        headIRInst = ir;
        for (IRInstruction x : instList) {
            ir.setPostInst(x);
            x.setPreInst(ir);
            ir = x;
        }
    }

    public void codegen(RegisterAllocator regManager) {
        System.out.println(getName() + ":");
        IRInstruction x = headIRInst.getPostInst();
        int id = 0;
        while (x != null) {
            x.codegen(regManager);
            System.out.println("#" + x.getId() + ": " + x.getMessage());
            id = x.getId();
            x = x.getPostInst();
        }
        regManager.flush_all(id + 1);
    }

    public void optimize() {
        makeIRList();
        IRInstruction x = headIRInst.getPostInst();
        while (x != null) {
            x.setId(enclosureCodeSegment.tmp++);
            x.optimize();
            x = x.getPostInst();
        }
        x = headIRInst.getPostInst();
        while (x != null) {
            if (x instanceof CopyInstruction) {
                CopyInstruction c = (CopyInstruction) x;
                VirtualRegister lhs = c.getLhs();
                if (c.is_imm_assign()) {
                    int rhs = c.getRhs_int();
                    if (rhs < -2048 || rhs > 2047) {
                        x = x.getPostInst();
                        continue;
                    }
                    if (lhs.getRead_times() == 1 && lhs.getWrite_times() == 1) {
                        if (lhs.getLast_read() == c.getPostInst()) {
                            //c.getPostInst().replace_rhs_with(lhs, rhs);
                        }
                    }
                } else {
                    VirtualRegister rhs = c.getRhs();
                    if (rhs.getRead_times() == 1 && rhs.getWrite_times() == 1) {
                        if (rhs.getLast_write() == c.getPreInst()) {
                            c.getPreInst().replace_lhs_with(rhs, lhs);
                            c.getPreInst().setPostInst(c.getPostInst());
                            if (c.getPostInst() != null) c.getPostInst().setPreInst(c.getPreInst());
                        }
                    }
                }
            }
            x = x.getPostInst();
        }
    }
}
