package Frontend;

import Semantic.Type;

public class LoadInstruction extends IRInstruction {
    VirtualRegister lhs, rhs;//rhs is a pointer
    int offset;
    private int width;

    LoadInstruction(op o, VirtualRegister lhs, VirtualRegister rhs, int addr, Type tp) {
        super(o);
        assert o == op.LOAD;
        this.lhs = lhs;
        this.rhs = rhs;
        this.offset = addr;
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
        String t2 = regManager.askForReg(rhs, getId(), true);
        String t1 = regManager.askForReg(lhs, getId(), false);
        //LW("t1", rhs.getAddrValue(), "sp");
        if (width == 4) {
            lw(t1, offset + "("+t2+")");
            //LW("t2", offset, "t1");
            //SW("t2", lhs.getAddrValue(), "sp");
        } else {
            lb(t1, offset + "("+t2+")");
            //LB("t2", offset, "t1");
            //SB("t2", lhs.getAddrValue(), "sp");
        }
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
        rhs.read_ex(this);
    }

    @Override
    public String getMessage() {
        return ("Load "  + lhs.getName() + ", " + rhs.getName() + " + " + offset + " (" + width + " byte)");
    }
}
