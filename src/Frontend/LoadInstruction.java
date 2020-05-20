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
    public void codegen() {
        LW("t1", rhs.getAddrValue(), "sp");
        if (width == 4) {
            LW("t2", offset, "t1");
            SW("t2", lhs.getAddrValue(), "sp");
        } else {
            LB("t2", offset, "t1");
            SB("t2", lhs.getAddrValue(), "sp");
        }
    }

    @Override
    public String getMessage() {
        return ("Load "  + lhs.getName() + ", " + rhs.getName() + " + " + offset + " (" + width + " byte)");
    }
}
