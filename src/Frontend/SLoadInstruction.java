package Frontend;

import Semantic.Type;

public class SLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    SLoadInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SLOAD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }
    SLoadInstruction(IRInstruction.op o, VirtualRegister lhs, int addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SLOAD;
        this.lhs = lhs;
        this.offset = new Address();
        this.offset.setAddr(addr);
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
        String t1 = regManager.askForReg(lhs, getId(), false);
        if (width == 4) {
            LW(t1, offset.getAddr(), "sp");
//            LW("t1", offset.getAddr(), "sp");
//            SW("t1", lhs.getAddrValue(), "sp");
        } else {
            LB(t1, offset.getAddr(), "sp");
//            LB("t1", offset.getAddr(), "sp");
//            SB("t1", lhs.getAddrValue(), "sp");
        }
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
    }

    @Override
    public String getMessage() {
        return ("Load "  + lhs.getName() + ", sp + " + offset.getAddr() + " (" + width + " byte)");
    }
}
