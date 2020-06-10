package Frontend;

import Backend.BaseRegister;
import Backend.MachineRegister;
import Semantic.Type;

import java.util.HashSet;
import java.util.Map;

public class LoadInstruction extends IRInstruction {
    VirtualRegister lhs;
    BaseRegister rhs;//rhs is a pointer
    int offset;
    private int width;

    LoadInstruction(op o, VirtualRegister lhs, BaseRegister rhs, int addr, Type tp) {
        super(o);
        assert o == op.LOAD;
        this.lhs = lhs;
        this.rhs = rhs;
        this.offset = addr;
        this.width = tp.getWidth();
    }

    LoadInstruction(op o, VirtualRegister lhs, BaseRegister rhs, int addr, int tpWid) {
        super(o);
        assert o == op.LOAD;
        this.lhs = lhs;
        this.rhs = rhs;
        this.offset = addr;
        this.width = tpWid;
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
        String t2 = getUseReg(rhs);
        String t1 = getDefReg(lhs);
        if (width == 4) {
            lw(t1, offset + "("+t2+")");
        } else {
            lb(t1, offset + "("+t2+")");
        }
        checkDefReg(lhs);
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
        if (rhs instanceof VirtualRegister) {
            ((VirtualRegister) rhs).read_ex(this);
        }
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        def.add(lhs);
        lhs.addDef(this);
        if (rhs instanceof VirtualRegister) {
            use.add(rhs);
            rhs.addUse(this);
        }
    }

    @Override
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newLhs = getOrPut(givenCs, virtualMap, lhs);
        VirtualRegister newRhs = getOrPut(givenCs, virtualMap, ((VirtualRegister) rhs));
        return new LoadInstruction(op.LOAD, newLhs, newRhs, offset, width);
    }

    @Override
    public String getMessage() {
        return ("Load "  + lhs.getName() + ", " + rhs.getName() + " + " + offset + " (" + width + " byte)");
    }
}
