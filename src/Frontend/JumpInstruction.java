package Frontend;

import java.util.HashSet;

public class JumpInstruction extends IRInstruction {
    BasicBlock des;
    JumpInstruction(op o, BasicBlock des) {
        super(o);
        assert o == op.JUMP;
        this.des = des;
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        assert false;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        j(des.getName());
    }

    @Override
    public void optimize() {
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
    }

    @Override
    public String getMessage() {
        if (des == null) return "Jump wrong!";
        return ("go to " + des.getName());
    }
}
