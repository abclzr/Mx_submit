package Frontend;

import Semantic.Type;

public class StoreInstruction extends IRInstruction {
    private VirtualRegister addr, value;
    int offset;
    private int width;

    StoreInstruction(op o, VirtualRegister a, int addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == op.STORE;
        this.addr = a;
        this.offset = addr;
        this.value = b;//may be null
        width = tp.getWidth();
    }

    @Override
    public void codegen() {
        lw("t1", addr.getAddrValue() + "(sp)");
        if (width == 4) {
            if(value != null) {
                lw("t2", value.getAddrValue() + "(sp)");
                sw("t2", offset + "(t1)");
            } else
                sw("x0", offset + "(t1)");
        } else {
            if (value != null) {
                lb("t2", value.getAddrValue() + "(sp)");
                sb("t2", offset + "(t1)");
            } else
                sb("x0", offset + "(t1)");
        }
    }

    @Override
    public String getMessage() {
        if (value != null)
            return ("Store "  + addr.getName() + " + " + offset + ", " + value.getName() + " (" + width + " byte)");
        else
            return ("Store "  + addr.getName() + " + " + offset + ", " + 0 + " (" + width + " byte)");
    }
}
