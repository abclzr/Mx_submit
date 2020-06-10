package Frontend;

import Semantic.Type;

import java.util.HashSet;
import java.util.Map;

public class LAInstruction extends IRInstruction {
    VirtualRegister lhs;//rhs is a pointer
    String gv;
    private Type tp;
    private int width;

    LAInstruction(IRInstruction.op o, VirtualRegister lhs, String gv, Type tp) {
        super(o);
        assert o == op.GADD;
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
        String l = getDefReg(lhs);
        la(l, gv);
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
        return new LAInstruction(op.GADD, newLhs, gv, tp);
    }

    @Override
    public String getMessage() {
        return ("Op "  + lhs.getName() + " = " + gv + " (" + width + " byte)");
    }
}
