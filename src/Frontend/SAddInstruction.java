package Frontend;

import Semantic.Type;

import java.util.HashSet;
import java.util.Map;

public class SAddInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    VirtualRegister rhsForOffset;
    Address offset;
    private int width;
    private Type tp;

    SAddInstruction(IRInstruction.op o, VirtualRegister lhs, VirtualRegister rhsForOffset, Type tp) {
        super(o);
        assert o == IRInstruction.op.SADD;
        this.lhs = lhs;
        this.rhsForOffset = rhsForOffset;
        this.offset = rhsForOffset.getAddr();
        this.tp = tp;
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
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newLhs = getOrPut(givenCs, virtualMap, lhs);
        VirtualRegister newRhs = getOrPut(givenCs, virtualMap, rhsForOffset);
        return new SAddInstruction(op.SADD, newLhs, newRhs, tp);
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = sp + " + offset.getAddr() + " (" + width + " byte)");
    }
}
