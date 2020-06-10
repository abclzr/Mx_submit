package Frontend;

import Semantic.Type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class CallInstruction extends IRInstruction {
    private boolean has_return_value;
    VirtualRegister lhs;
    Type tp;
    int width;
    CodeSegment callee;
    List<VirtualRegister> params;
    CallInstruction(op o, VirtualRegister lhs, Type tp, CodeSegment c, List<VirtualRegister> p) {
        super(o);
        assert o == op.CALL;
        this.lhs = lhs;
        this.tp = tp;
        this.width = tp.getWidth();
        this.callee = c;
        this.params = p;
        this.has_return_value = true;
    }

    CallInstruction(op o, Type tp, CodeSegment c, List<VirtualRegister> p) {
        super(o);
        assert o == op.CALL;
        this.lhs = null;
        this.tp = tp;
        this.width = tp.getWidth();
        this.callee = c;
        this.params = p;
        this.has_return_value = false;
    }

    public VirtualRegister getLhs() {
        return lhs;
    }

    @Override
    public void replace_lhs_with(VirtualRegister a, VirtualRegister b) {
        if (lhs == a)
            lhs = b;
        else
            assert false;
    }

    public CodeSegment getCallee() {
        return callee;
    }

    @Override
    public void codegen(RegisterAllocator regManager) {
        int i = 0;
        for (VirtualRegister p : params) {
            if (i <= 7) {
                String pp = getUseReg(p);
                mv("a" + i, pp);
            } else {
                int offset = -callee.getStackStorage() + callee.getParams().get(i).getAddrValue();
                String pp = getUseReg(p);
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
            String t1 = getDefReg(lhs);
            mv(t1, "a0");
            checkDefReg(lhs);
        }
    }

    @Override
    public void optimize() {
        if (has_return_value) {
            lhs.write_ex(this);
        }
        for (VirtualRegister x : params) {
            x.read_ex(this);
        }
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        if (has_return_value) {
            def.add(lhs);
            lhs.addDef(this);
        }
        for (VirtualRegister x : params) {
            use.add(x);
            x.addUse(this);
        }
    }

    @Override
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newLhs = getOrPut(givenCs, virtualMap, lhs);
        List<VirtualRegister> newParams = new ArrayList<>();
        params.forEach(p -> {newParams.add(getOrPut(givenCs, virtualMap, p));});
        if (newLhs == null)
            return new CallInstruction(op.CALL, tp, callee, newParams);
        else
            return new CallInstruction(op.CALL, newLhs, tp, callee, newParams);
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
