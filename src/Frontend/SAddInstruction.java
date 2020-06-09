package Frontend;

import Semantic.Type;

import java.util.HashSet;

public class SAddInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    Address offset;
    private int width;

    SAddInstruction(IRInstruction.op o, VirtualRegister lhs, Address addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SADD;
        this.lhs = lhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }
    SAddInstruction(IRInstruction.op o, VirtualRegister lhs, int addr, Type tp) {
        super(o);
        assert o == IRInstruction.op.SADD;
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
        String t1 = getDefReg(lhs);
        ADDI(t1, "sp", offset.getAddr());
        checkDefReg(lhs);
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        def.add(lhs);
        lhs.addDef(this);
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = sp + " + offset.getAddr() + " (" + width + " byte)");
    }
}
