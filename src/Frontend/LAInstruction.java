package Frontend;

import Semantic.Type;

public class LAInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    String gv;
    private int width;

    LAInstruction(IRInstruction.op o, VirtualRegister lhs, String gv, Type tp) {
        super(o);
        assert o == op.GADD;
        this.lhs = lhs;
        this.gv = gv;
        this.width = tp.getWidth();
    }

    @Override
    public void codegen() {
        la("t1", gv);
        sw("t1", lhs.getAddrValue() + "(sp)");
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = " + gv + " (" + width + " byte)");
    }
}
