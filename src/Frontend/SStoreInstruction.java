package Frontend;

import Semantic.Type;

public class SStoreInstruction extends IRInstruction {
    private VirtualRegister value;
    private Address offset;
    private int width;

    SStoreInstruction(IRInstruction.op o, Address addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == IRInstruction.op.SSTORE;
        this.offset = addr;
        this.value = b;//maybe zero!
        width = tp.getWidth();
    }

    @Override
    public void codegen() {
        if (value != null) {
            if (width == 4) {
                LW("t1", value.getAddrValue(), "sp");
                SW("t1", offset.getAddr(), "sp");
            } else {
                LB("t1", value.getAddrValue(), "sp");
                SB("t1", offset.getAddr(), "sp");
            }
        } else {
            if (width == 4) {
                SW("x0", offset.getAddr(), "sp");
            } else {
                SB("x0", offset.getAddr(), "sp");
            }
        }
    }

    @Override
    public String getMessage() {
        if (value == null)
            return ("Store sp + " + offset.getAddr() + ", 0 (" + width + " byte)");
        else
            return ("Store sp + " + offset.getAddr() + ", " + value.getName() + " (" + width + " byte)");
    }
}
