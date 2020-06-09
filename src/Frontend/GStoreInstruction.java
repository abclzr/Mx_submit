package Frontend;

import Semantic.Type;

import java.util.HashSet;

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
        String v = getUseReg(value);
        if (value.getWidth() == 4) {
            sw(v, "0(t6)");
        } else {
            sb(v, "0(t6)");
        }
    }

    @Override
    public void optimize() {
        value.read_ex(this);
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        use.add(value);
        value.addUse(this);
    }

    @Override
    public String getMessage() {
        return ("GStore " + gv + ", " + value.getName() + " (" + width + " byte)");
    }
}
