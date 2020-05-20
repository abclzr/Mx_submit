package Frontend;

public class JumpInstruction extends IRInstruction {
    BasicBlock des;
    JumpInstruction(op o, BasicBlock des) {
        super(o);
        assert o == op.JUMP;
        this.des = des;
    }

    @Override
    public void codegen() {
        j(des.getName());
    }

    @Override
    public String getMessage() {
        if (des == null) return "Jump wrong!";
        return ("go to " + des.getName());
    }
}
