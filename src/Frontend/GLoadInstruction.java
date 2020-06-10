package Frontend;

import Semantic.Type;

import java.util.HashSet;
import java.util.Map;

public class GLoadInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    String gv;
    private int width;
    private Type tp;

    GLoadInstruction(IRInstruction.op o, VirtualRegister lhs, String gv, Type tp) {
        super(o);
        assert o == IRInstruction.op.GLOAD;
        this.lhs = lhs;
        this.gv = gv;
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
        la("t6", gv);
        String l = getDefReg(lhs);
        if (lhs.getWidth() == 4) {
            lw(l, "0(t6)");
        } else {
            lb(l, "0(t6)");
        }
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
        return new GLoadInstruction(op.GLOAD, newLhs, gv, tp);
    }

    @Override
    public String getMessage() {
        return ("GLoad "  + lhs.getName() + ", " + gv + " (" + width + " byte)");
    }
}
