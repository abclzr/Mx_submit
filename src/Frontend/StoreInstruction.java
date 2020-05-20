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
        LW("t1", addr.getAddrValue(), "sp");
        if (width == 4) {
            if(value != null) {
                LW("t2", value.getAddrValue(), "sp");
                SW("t2", offset, "t1");
            } else
                SW("x0", offset, "t1");
        } else {
            if (value != null) {
                LB("t2", value.getAddrValue(), "sp");
                SB("t2", offset, "t1");
            } else
                SB("x0", offset, "t1");
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
