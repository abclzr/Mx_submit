package Frontend;

public class BranchInstruction extends IRInstruction {
    VirtualRegister r1, r2;
    String bop;
    BasicBlock toBB;

    BranchInstruction(op o, VirtualRegister r1, String bop, VirtualRegister r2, BasicBlock toBB) {
        super(o);
        assert o == op.BRANCH;
        this.r1 = r1;
        this.bop = bop;
        this.r2 = r2;
        this.toBB = toBB;
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        String t1 =regManager.askForReg(r1, getId(), true);
        String t2 =regManager.askForReg(r2, getId(), true);
        /*
        LW("t1", r1.getAddrValue(), "sp");
        LW("t2", r2.getAddrValue(), "sp");
         */
        regManager.flush_all(getId());
        switch (bop) {
            case "<":
                bgt(t2, t1, toBB.getName());
                break;
            case ">":
                bgt(t1, t2, toBB.getName());
                break;
            case "<=":
                ble(t1, t2, toBB.getName());
                break;
            case ">=":
                ble(t2, t1, toBB.getName());
                break;
        }
    }

    @Override
    public void optimize() {
        r1.read_ex(this);
        r2.read_ex(this);
    }

    @Override
    public String getMessage() {
        return "if (" + r1.getName() + " " + bop + " " + r2.getName() + ") go to " + toBB.getName();
    }
}
