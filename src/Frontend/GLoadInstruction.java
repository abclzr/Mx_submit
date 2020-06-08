package Frontend;

import Semantic.Type;

public class GLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    String gv;
    private int width;

    GLoadInstruction(IRInstruction.op o, VirtualRegister lhs, String gv, Type tp) {
        super(o);
        assert o == IRInstruction.op.GLOAD;
        this.lhs = lhs;
        this.gv = gv;
        this.width = tp.getWidth();
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        if (lhs == a)
            lhs = b;
        else
            assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        la("t6", gv);
        String l = regManager.askForReg(lhs, getId(), false);
        if (lhs.getWidth() == 4) {
            lw(l, "0(t6)");
            /*
            lw("t2", "0(t1)");
            SW("t2", lhs.getAddrValue(), "sp");
             */
        } else {
            lb(l, "0(t6)");
            /*
            lb("t2", "0(t1)");
            SB("t2", lhs.getAddrValue(), "sp");
             */
        }
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
    }

    @Override
    public String getMessage() {
        return ("GLoad "  + lhs.getName() + ", " + gv + " (" + width + " byte)");
    }
}
