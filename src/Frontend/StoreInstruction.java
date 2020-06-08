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
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        String a = regManager.askForReg(addr, getId(), true);
        String v = null;
        if (value != null) v = regManager.askForReg(value, getId(), true);
        if (width == 4) {
            if(value != null) {
                SW(v, offset, a);
            } else
                SW("x0", offset, a);
        } else {
            if (value != null) {
                SB(v, offset, a);
            } else
                SB("x0", offset, a);
        }
    }

    @Override
    public void optimize() {
        addr.read_ex(this);
        if (value != null)
            value.read_ex(this);
    }

    @Override
    public String getMessage() {
        if (value != null)
            return ("Store "  + addr.getName() + " + " + offset + ", " + value.getName() + " (" + width + " byte)");
        else
            return ("Store "  + addr.getName() + " + " + offset + ", " + 0 + " (" + width + " byte)");
    }
}
