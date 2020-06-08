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
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        if (lhs == a)
            lhs = b;
        else
            assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        String l = regManager.askForReg(lhs, getId(), false);
        la(l, gv);
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = " + gv + " (" + width + " byte)");
    }
}
