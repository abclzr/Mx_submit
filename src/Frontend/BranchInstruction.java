package Frontend;

import Backend.BaseRegister;

import java.util.HashSet;
import java.util.Set;

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
        String t1 = getUseReg(r1);
        String t2 = getUseReg2(r2);
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
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        use.add(r1);
        r1.addUse(this);
        use.add(r2);
        r2.addUse(this);
    }

    @Override
    public String getMessage() {
        return "if (" + r1.getName() + " " + bop + " " + r2.getName() + ") go to " + toBB.getName();
    }
}
