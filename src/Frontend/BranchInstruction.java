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
    public void codegen() {
        lw("t1", r1.getAddrValue() + "(sp)");
        lw("t2", r2.getAddrValue() + "(sp)");
        switch (bop) {
            case "<":
                bgt("t2", "t1", toBB.getName());
                break;
            case ">":
                bgt("t1", "t2", toBB.getName());
                break;
            case "<=":
                ble("t1", "t2", toBB.getName());
                break;
            case ">=":
                ble("t2", "t1", toBB.getName());
                break;
        }
    }

    @Override
    public String getMessage() {
        return "if (" + r1.getName() + " " + bop + " " + r2.getName() + ") go to " + toBB.getName();
    }
}
