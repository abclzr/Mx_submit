package Frontend;

import java.util.HashSet;

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
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        if (returnValue != null) {
            String rv = getUseReg(returnValue);
            mv("a0", rv);
        } else
            mv("a0", "x0");
        if (enclosureSegment.getRaPointer() != null)
            LW("ra", enclosureSegment.getRaPointer().getAddrValue(), "sp");
        int i = 0;
        for (VirtualRegister v : enclosureSegment.calleeVirtualList) {
            String r = enclosureSegment.calleeRegList.get(i++);
            IRInstruction.LW(r, v.getAddrValue(), "sp");
        }
        ADDI("sp", "sp", enclosureSegment.getStackStorage());
        ret();
    }

    @Override
    public void optimize() {
        if (returnValue != null) {
            returnValue.read_ex(this);
        }
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        if (returnValue != null) {
            use.add(returnValue);
            returnValue.addUse(this);
        }
    }

    @Override
    public String getMessage() {
        if (returnValue == null)
            return "Return";
        else
            return ("Return " + returnValue.getName());
    }
}
