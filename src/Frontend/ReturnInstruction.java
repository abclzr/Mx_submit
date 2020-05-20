package Frontend;

public class ReturnInstruction extends IRInstruction {
    private VirtualRegister returnValue;
    private CodeSegment enclosureSegment;
    ReturnInstruction(op o, VirtualRegister returnValue, CodeSegment enclosureSegment) {
        super(o);
        assert o == op.RETURN;
        this.returnValue = returnValue;//May be Null!
        this.enclosureSegment = enclosureSegment;
    }

    @Override
    public void codegen() {
        if (enclosureSegment.getRaPointer() != null)
            lw("ra", enclosureSegment.getRaPointer().getAddrValue() + "(sp)");
        if (returnValue != null)
            if (returnValue.getWidth() == 4)
                lw("a0", returnValue.getAddrValue() + "(sp)");
            else
                lb("a0", returnValue.getAddrValue() + "(sp)");
        else
            mv("a0", "x0");
        addi("sp", "sp", String.valueOf(enclosureSegment.getStackStorage()));
        ret();
    }

    @Override
    public String getMessage() {
        if (returnValue == null)
            return "Return";
        else
            return ("Return " + returnValue.getName());
    }
}
