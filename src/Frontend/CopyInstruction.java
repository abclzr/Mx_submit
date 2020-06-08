package Frontend;

public class CopyInstruction extends IRInstruction {
    private VirtualRegister lhs, rhs;
    private int rhs_int;
    private type tp;

    public CopyInstruction(op o, VirtualRegister a, VirtualRegister b) {
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

    public VirtualRegister getRhs() {
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
                t2 = regManager.askForReg(rhs, getId(), true);
                t1 = regManager.askForReg(lhs, getId(), false);
                mv(t1, t2);
                /*
                if (rhs.getWidth() == 4)
                    LW("t1", rhs.getAddrValue(), "sp");
                else
                    LB("t1", rhs.getAddrValue(), "sp");
                if (lhs.getWidth() == 4)
                    SW("t1", lhs.getAddrValue(), "sp");
                else
                    SB("t1", lhs.getAddrValue(), "sp");
                 */
                break;
            case val_to_reg:
                t1 = regManager.askForReg(lhs, getId(), false);
                li(t1, rhs_int);
                /*
                li("t1", rhs_int);
                if (lhs.getWidth() == 4)
                    SW("t1", lhs.getAddrValue(), "sp");
                else
                    SB("t1", lhs.getAddrValue(), "sp");
                 */
                break;
        }
    }

    @Override
    public void optimize() {
        lhs.write_ex(this);
        if (tp == type.reg_to_reg)
            rhs.read_ex(this);
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
