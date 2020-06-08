package Frontend;

import Semantic.Type;

public class GStoreInstruction extends IRInstruction {
    private VirtualRegister value;
    private String gv;
    private int width;

    GStoreInstruction(IRInstruction.op o, String gv, VirtualRegister b, Type tp) {
        super(o);
        assert o == IRInstruction.op.GSTORE;
        this.gv = gv;
        this.value = b;
        width = tp.getWidth();
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        la("t6", gv);
        String v = regManager.askForReg(value, getId(), true);
        if (value.getWidth() == 4) {
            sw(v, "0(t6)");
            /*
            LW("t2", value.getAddrValue(), "sp");
            sw("t2", "0(t1)");
             */
        } else {
            sb(v, "0(t6)");
            /*
            LB("t2", value.getAddrValue(), "sp");
            sb("t2", "0(t1)");
             */
        }
    }

    @Override
    public void optimize() {
        value.read_ex(this);
    }

    @Override
    public String getMessage() {
        return ("GStore " + gv + ", " + value.getName() + " (" + width + " byte)");
    }
}
