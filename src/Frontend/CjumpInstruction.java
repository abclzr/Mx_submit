package Frontend;

import java.util.HashSet;

public class CjumpInstruction extends IRInstruction {
    private VirtualRegister c;
    private boolean jump_when_true;
    private BasicBlock des;

    CjumpInstruction(op o, VirtualRegister c, boolean jwt, BasicBlock des) {
        super(o);
        assert o == op.CJUMP;
        this.c = c;
        this.des = des;
        this.jump_when_true = jwt;
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        String t1 = getUseReg(c);
        if (jump_when_true)
            bnez(t1, des.getName());
        else
            beqz(t1, des.getName());
    }

    @Override
    public void optimize() {
        c.read_ex(this);
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        use.add(c);
        c.addUse(this);
    }

    @Override
    public String getMessage() {
        if (jump_when_true)
            return "if (" + c.getName() + " == true) go to " + des.getName();
        else
            return "if (" + c.getName() + " == false) go to " + des.getName();
    }
}
