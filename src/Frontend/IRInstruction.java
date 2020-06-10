package Frontend;

import Backend.BaseRegister;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class IRInstruction {
    public op IRop;
    private IRInstruction preInst;
    private IRInstruction postInst;
    private int id;
    private boolean needToRemoveInInline = false;

    public void setNeedToRemoveInInline(boolean needToRemoveInInline) {
        this.needToRemoveInInline = needToRemoveInInline;
    }

    public boolean isNeedToRemoveInInline() {
        return needToRemoveInInline;
    }

    protected Set<BaseRegister> use;
    protected Set<BaseRegister> def;

    public String getUseReg(BaseRegister r) {
        if (r.color == null) {
            VirtualRegister v = ((VirtualRegister) r);
            if (v.getWidth() == 4)
                LW("t1", v.getAddrValue(), "sp");
            else
                LB("t1", v.getAddrValue(), "sp");
            return "t1";
        } else
            return r.color.getName();
    }
    public String getUseReg2(BaseRegister r) {
        if (r.color == null) {
            VirtualRegister v = ((VirtualRegister) r);
            if (v.getWidth() == 4)
                LW("t2", v.getAddrValue(), "sp");
            else
                LB("t2", v.getAddrValue(), "sp");
            return "t2";
        } else
            return r.color.getName();
    }
    public String getDefReg(BaseRegister r) {
        if (r.color == null) {
            return "t3";
        } else
            return r.color.getName();
    }
    public void checkDefReg(BaseRegister r) {
        if (r.color == null) {
            VirtualRegister v = (VirtualRegister) r;
            if (v.getWidth() == 4)
                SW("t3", v.getAddrValue(), "sp");
            else
                SB("t3", v.getAddrValue(), "sp");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPreInst(IRInstruction p) {
        this.preInst = p;
    }

    public IRInstruction getPreInst() {
        return this.preInst;
    }

    public void setPostInst(IRInstruction p) {
        this.postInst = p;
    }

    public IRInstruction getPostInst() {
        return this.postInst;
    }

    IRInstruction(op o) {
        this.IRop = o;
        this.preInst = null;
        this.postInst = null;
    }

    public abstract void replace_lhs_with(VirtualRegister a, VirtualRegister b);

    public abstract void codegen(RegisterAllocator regManager);

    public abstract void optimize();

    public abstract void collectUseAndDef();

    public Set<BaseRegister> getUse() {
        return use;
    }

    public Set<BaseRegister> getDef() {
        return def;
    }

    public VirtualRegister getOrPut(CodeSegment givenCs, Map<VirtualRegister, VirtualRegister> virtualMap, VirtualRegister v) {
        if (v == null) return null;
        if(virtualMap.containsKey(v))
            return virtualMap.get(v);
        else {
            VirtualRegister nv = new VirtualRegister(givenCs, v.getType());
            if (v.hasAskedForSpace) nv.askForSpace();
            virtualMap.put(v, nv);
            return nv;
        }
    }

    public abstract IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap);

    public enum op {RETURN, MALLOC, BINARY, UNARY, COPY, JUMP, CJUMP, BRANCH, PARAM, CALL, ARRAY, LOAD, STORE, SLOAD, SSTORE, GLOAD, GSTORE, SADD, GADD};

    public abstract String getMessage();

    public static void SW(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            sw(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            sw(rd, "0(t5)");
        }
    }
    public static void SH(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            sh(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            sh(rd, "0(t5)");
        }
    }
    public static void SB(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            sb(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            sb(rd, "0(t5)");
        }
    }

    public static void LW(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            lw(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            lw(rd, "0(t5)");
        }
    }
    public static void LH(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            lh(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            lh(rd, "0(t5)");
        }
    }
    public static void LB(String rd, int offset, String rs) {
        if (-2048 <= offset && offset <= 2047)
            lb(rd, offset + "(" + rs + ")");
        else {
            li("t5", offset);
            add("t5", "t5", rs);
            lb(rd, "0(t5)");
        }
    }
    public static void la(String rd, String symbol) {
        System.out.println("\tla " + rd + ", " + symbol);
    }
    public static void lb(String rd, String symbol) {
        System.out.println("\tlb " + rd + ", " + symbol);
    }
    public static void lh(String rd, String symbol) {
        System.out.println("\tlh " + rd + ", " + symbol);
    }
    public static void lw(String rd, String symbol) {
        System.out.println("\tlw " + rd + ", " + symbol);
    }
    public static void sb(String rd, String symbol) {
        System.out.println("\tsb " + rd + ", " + symbol);
    }
    public static void sh(String rd, String symbol) {
        System.out.println("\tsh " + rd + ", " + symbol);
    }
    public static void sw(String rd, String symbol) {
        System.out.println("\tsw " + rd + ", " + symbol);
    }
    public static void sb(String rd, String symbol, String rt) {
        System.out.println("\tsb " + rd + ", " + symbol + ", " + rt);
    }
    public static void sh(String rd, String symbol, String rt) {
        System.out.println("\tsh " + rd + ", " + symbol + ", " + rt);
    }
    public static void sw(String rd, String symbol, String rt) {
        System.out.println("\tsw " + rd + ", " + symbol + ", " + rt);
    }
    public static void nop() {
        System.out.println("\tnop");
    }
    public static void li(String rd, int imm) {
        System.out.println("\tli " + rd + ", " + imm);
    }
    public static void mv(String rd, String rs) {
        System.out.println("\tmv " + rd + ", " + rs);
    }
    public static void not(String rd, String rs) {
        System.out.println("\tnot " + rd + ", " + rs);
    }
    public static void neg(String rd, String rs) {
        System.out.println("\tneg " + rd + ", " + rs);
    }
    public static void seqz(String rd, String rs) {
        System.out.println("\tseqz " + rd + ", " + rs);
    }
    public static void snez(String rd, String rs) {
        System.out.println("\tsnez " + rd + ", " + rs);
    }
    public static void sltz(String rd, String rs) {
        System.out.println("\tsltz " + rd + ", " + rs);
    }
    public static void sgtz(String rd, String rs) {
        System.out.println("\tsgtz " + rd + ", " + rs);
    }
    public static void beqz(String rs, String offset) {
        System.out.println("\tbeqz " + rs + ", " + offset);
    }
    public static void bnez(String rs, String offset) {
        System.out.println("\tbnez " + rs + ", " + offset);
    }
    public static void blez(String rs, String offset) {
        System.out.println("\tblez " + rs + ", " + offset);
    }
    public static void bgez(String rs, String offset) {
        System.out.println("\tbgez " + rs + ", " + offset);
    }
    public static void bltz(String rs, String offset) {
        System.out.println("\tbltz " + rs + ", " + offset);
    }
    public static void btz(String rs, String offset) {
        System.out.println("\tbtz " + rs + ", " + offset);
    }
    public static void bgt(String rs, String rt, String offset) {
        System.out.println("\tbgt " + rs + ", " + rt + ", " + offset);
    }
    public static void ble(String rs, String rt, String offset) {
        System.out.println("\tble " + rs + ", " + rt + ", " + offset);
    }
    public static void bgtu(String rs, String rt, String offset) {
        System.out.println("\tbgtu " + rs + ", " + rt + ", " + offset);
    }
    public static void bleu(String rs, String rt, String offset) {
        System.out.println("\tbleu " + rs + ", " + rt + ", " + offset);
    }
    public static void j(String offset) {
        System.out.println("\tj " + offset);
    }
    public static void jr(String rs) {
        System.out.println("\tjr " + rs);
    }
    public static void ret() {
        System.out.println("\tret");
    }
    public static void call(String offset) {
        System.out.println("\tcall " + offset);
    }

//                    case "+":
    public static void add(String rd, String r1, String r2) {
        System.out.println("\tadd " + rd + ", " + r1 + ", " + r2);
    }
    public static void ADDI(String rd, String r1, int r2) {
        if (-2048 <= r2 && r2 <= 2047)
            addi(rd, r1, String.valueOf(r2));
        else {
            li("t5", r2);
            add(rd, r1, "t5");
        }
    }
    public static void addi(String rd, String r1, String r2) {
        System.out.println("\taddi " + rd + ", " + r1 + ", " + r2);
    }
//                    case "<=":
//                    case ">=":
//                    case "<":
//                    case ">":
    public static void slt(String rd, String r1, String r2) {
        System.out.println("\tslt " + rd + ", " + r1 + ", " + r2);
    }
    public static void slti(String rd, String r1, String r2) {
        System.out.println("\tslti " + rd + ", " + r1 + ", " + r2);
    }
//                    case "-":
    public static void sub(String rd, String r1, String r2) {
        System.out.println("\tsub " + rd + ", " + r1 + ", " + r2);
    }
//                    case "*":
    public static void mul(String rd, String r1, String r2) {
        System.out.println("\tmul " + rd + ", " + r1 + ", " + r2);
    }
//                    case "/":
    public static void div(String rd, String r1, String r2) {
        System.out.println("\tdiv " + rd + ", " + r1 + ", " + r2);
    }
//                    case "%":
    public static void rem(String rd, String r1, String r2) {
        System.out.println("\trem " + rd + ", " + r1 + ", " + r2);
    }
//                    case "<<":
    public static void sll(String rd, String r1, String r2) {
        System.out.println("\tsll " + rd + ", " + r1 + ", " + r2);
    }
    public static void slli(String rd, String r1, String r2) {
        System.out.println("\tslli " + rd + ", " + r1 + ", " + r2);
    }
//                    case ">>":
    public static void sra(String rd, String r1, String r2) {
        System.out.println("\tsra " + rd + ", " + r1 + ", " + r2);
    }
    public static void srai(String rd, String r1, String r2) {
        System.out.println("\tsrai " + rd + ", " + r1 + ", " + r2);
    }
//                    case "&":
    public static void and(String rd, String r1, String r2) {
        System.out.println("\tand " + rd + ", " + r1 + ", " + r2);
    }
    public static void andi(String rd, String r1, String r2) {
        System.out.println("\tandi " + rd + ", " + r1 + ", " + r2);
    }
//                    case "^":
    public static void xor(String rd, String r1, String r2) {
        System.out.println("\txor " + rd + ", " + r1 + ", " + r2);
    }
    public static void xori(String rd, String r1, String r2) {
        System.out.println("\txori " + rd + ", " + r1 + ", " + r2);
    }
//                    case "|":
    public static void or(String rd, String r1, String r2) {
        System.out.println("\tor " + rd + ", " + r1 + ", " + r2);
    }
    public static void ori(String rd, String r1, String r2) {
        System.out.println("\tori " + rd + ", " + r1 + ", " + r2);
    }
}