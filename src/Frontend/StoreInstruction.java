package Frontend;

import Semantic.Type;

import java.util.HashSet;
import java.util.Map;

public class StoreInstruction extends IRInstruction {
    private VirtualRegister addr, value;
    int offset;
    private int width;
    private Type tp;

    StoreInstruction(op o, VirtualRegister a, int addr, VirtualRegister b, Type tp) {
        super(o);
        assert o == op.STORE;
        this.addr = a;
        this.offset = addr;
        this.value = b;//may be null
        this.tp = tp;
        width = tp.getWidth();
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        String a = getUseReg(addr);
        String v = null;
        if (value != null) v = getUseReg2(value);
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
        if (value != null) {
            value.read_ex(this);
        }
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        use.add(addr);
        addr.addUse(this);
        if (value != null) {
            use.add(value);
            value.addUse(this);
        }
    }

    @Override
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newAddr = getOrPut(givenCs, virtualMap, addr);
        VirtualRegister newValue = getOrPut(givenCs, virtualMap, value);
        return new StoreInstruction(op.STORE, newAddr, offset, newValue, tp);
    }

    @Override
    public String getMessage() {
        if (value != null)
            return ("Store "  + addr.getName() + " + " + offset + ", " + value.getName() + " (" + width + " byte)");
        else
            return ("Store "  + addr.getName() + " + " + offset + ", " + 0 + " (" + width + " byte)");
    }
}
