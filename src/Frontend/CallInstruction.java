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
    public void codegen() {
        int i = 0;
        for (VirtualRegister p : params) {
            if (i <= 7) {
                if (p.getWidth() == 4)
                    lw("a" + i, p.getAddrValue() + "(sp)");
                else
                    lb("a" + i, p.getAddrValue() + "(sp)");
            } else {
                int offset = -callee.getStackStorage() + callee.getParams().get(i).getAddrValue();
                if (p.getWidth() == 4) {
                    lw("t1", p.getAddrValue() + "(sp)");
                    sw("t1", offset + "(sp)");
                } else {
                    lb("t1", p.getAddrValue() + "(sp)");
                    sb("t1", offset + "(sp)");
                }
            }
            i++;
        }
        call(callee.getFuncName());
        if (has_return_value) {
            if (width == 4)
                sw("a0", lhs.getAddrValue() + "(sp)");
            else
                sb("a0", lhs.getAddrValue() + "(sp)");
        }
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
