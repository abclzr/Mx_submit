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
        lw("t1", rhs.getAddrValue() + "(sp)");
        if (width == 4) {
            lw("t2", offset + "(t1)");
            sw("t2", lhs.getAddrValue() + "(sp)");
        } else {
            lb("t2", offset + "(t1)");
            sb("t2", lhs.getAddrValue() + "(sp)");
        }
    }

    @Override
    public String getMessage() {
        return ("Load "  + lhs.getName() + ", " + rhs.getName() + " + " + offset + " (" + width + " byte)");
    }
}
