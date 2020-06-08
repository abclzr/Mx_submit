package Frontend;

import Semantic.Type;

import java.util.List;

public class CallInstruction extends IRInstruction {
    private boolean has_return_value;
    VirtualRegister lhs;
    int width;
    CodeSegment callee;
    List<VirtualRegister> params;
    CallInstruction(op o, VirtualRegister lhs, Type tp, CodeSegment c, List<VirtualRegister> p) {
        super(o);
        assert o == op.CALL;
        this.lhs = lhs;
        this.width = tp.getWidth();
        this.callee = c;
        this.params = p;
        this.has_return_value = true;
    }

    CallInstruction(op o, Type tp, CodeSegment c, List<VirtualRegister> p) {
        super(o);
        assert o == op.CALL;
        this.lhs = lhs;
        this.width = tp.getWidth();
        this.callee = c;
        this.params = p;
        this.has_return_value = false;
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
        int i = 0;
        for (VirtualRegister p : params) {
            if (i <= 7) {
                String pp = regManager.askForReg(p, getId(), true);
                mv("a" + i, pp);
                /*
                if (p.getWidth() == 4)
                    LW("a" + i, p.getAddrValue(), "sp");
                else
                    LB("a" + i, p.getAddrValue(), "sp");
                 */
            } else {
                int offset = -callee.getStackStorage() + callee.getParams().get(i).getAddrValue();
                String pp = regManager.askForReg(p, getId(), true);
                if (p.getWidth() == 4) {
                    SW(pp, offset, "sp");
                } else {
                    SB(pp, offset, "sp");
                }
            }
            i++;
        }
        call(callee.getFuncName());
        if (has_return_value) {
            String t1 = regManager.askForReg(lhs, getId(), false);
            mv(t1, "a0");
            /*
            if (width == 4)
                SW("a0", lhs.getAddrValue(), "sp");
            else
                SB("a0", lhs.getAddrValue(), "sp");
             */
        }
    }

    @Override
    public void optimize() {
        if (has_return_value) lhs.write_ex(this);
        for (VirtualRegister x : params)
            x.read_ex(this);
    }

    @Override
    public String getMessage() {
        String ret = null;
        if (has_return_value)
            ret = lhs.getName() + " = call (" + width + "B) " + callee.getFuncName() + "(";
        else
            ret = "call (" + width + "B) " + callee.getFuncName() + "(";
        for (VirtualRegister x : params) {
            ret = ret.concat(x.getName() + ", ");
        }
        ret = ret.concat(")");
        return ret;
    }
}
