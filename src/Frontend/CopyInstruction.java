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

    @Override
    public void codegen() {
        switch (tp) {
            case reg_to_reg:
                if (rhs.getWidth() == 4)
                    LW("t1", rhs.getAddrValue(), "sp");
                else
                    LB("t1", rhs.getAddrValue(), "sp");
                if (lhs.getWidth() == 4)
                    SW("t1", lhs.getAddrValue(), "sp");
                else
                    SB("t1", lhs.getAddrValue(), "sp");
                break;
            case val_to_reg:
                li("t1", rhs_int);
                if (lhs.getWidth() == 4)
                    SW("t1", lhs.getAddrValue(), "sp");
                else
                    SB("t1", lhs.getAddrValue(), "sp");
                break;
        }
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
