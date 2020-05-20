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
    public void codegen() {
        la("t1", gv);
        if (value.getWidth() == 4) {
            lw("t2", value.getAddrValue() + "(sp)");
            sw("t2", "0(t1)");
        } else {
            lb("t2", value.getAddrValue() + "(sp)");
            sb("t2", "0(t1)");
        }
    }

    @Override
    public String getMessage() {
        return ("GStore " + gv + ", " + value.getName() + " (" + width + " byte)");
    }
}
