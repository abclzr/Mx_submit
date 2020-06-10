package Frontend;

import Backend.BaseRegister;

import java.util.HashSet;
import java.util.Map;

public class CopyInstruction extends IRInstruction {
    private VirtualRegister lhs;
    private BaseRegister rhs;
    private int rhs_int;
    private type tp;

    public CopyInstruction(op o, VirtualRegister a, BaseRegister b) {
        super(o);
        assert o == op.COPY;
        this.lhs = a;
        this.rhs = b;
        this.tp = type.reg_to_reg;
    }

    public CopyInstruction(op o, VirtualRegister a, int b) {
        super(o);
        assert o == op.COPY;
        this.lhs = a;
        this.rhs_int = b;
        this.tp = type.val_to_reg;
    }

    public VirtualRegister getLhs() {
        return lhs;
    }

    public BaseRegister getRhs() {
        return rhs;
    }

    public int getRhs_int() {
        return rhs_int;
    }

    public boolean is_imm_assign() {
        return tp == type.val_to_reg;
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
        String t1, t2;
        switch (tp) {
            case reg_to_reg:
                t2 = getUseReg(rhs);
                t1 = getDefReg(lhs);
                if (!t1.equals(t2))
                    mv(t1, t2);
                checkDefReg(lhs);
                break;
            case val_to_reg:
                t1 = getDefReg(lhs);
                li(t1, rhs_int);
                checkDefReg(lhs);
                break;
        }
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
        if (tp == type.reg_to_reg && rhs instanceof VirtualRegister) {
            ((VirtualRegister) rhs).read_ex(this);
        }
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        def.add(lhs);
        lhs.addDef(this);
        if (tp == type.reg_to_reg && rhs instanceof VirtualRegister) {
            use.add(rhs);
            rhs.addUse(this);
        }
    }

    @Override
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newLhs = getOrPut(givenCs, virtualMap, lhs);
        VirtualRegister newRhs = getOrPut(givenCs, virtualMap, ((VirtualRegister) rhs));
        switch (tp) {
            case reg_to_reg: return new CopyInstruction(op.COPY, newLhs, newRhs);
            case val_to_reg: return new CopyInstruction(op.COPY, newLhs, rhs_int);
        }
        return null;
    }

    @Override
    public String getMessage() {
        switch (tp) {
            case reg_to_reg:
                return "Copy " + lhs.getName() + " " + rhs.getName();
            case val_to_reg:
                return "Copy " + lhs.getName() + " " + rhs_int;
        }
        return null;
    }

    public enum type {reg_to_reg, val_to_reg};
}
