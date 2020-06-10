package Frontend;

import Backend.BaseRegister;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BinaryInstruction extends IRInstruction {
    private VirtualRegister lhs, rhs1, rhs2;
    private int imm_rhs2;
    private int width;
    private String bop;
    private boolean is_imm;

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, VirtualRegister r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;//may be null!
        this.bop = bop;
        this.width = l.getWidth();
        this.rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = false;
    }

    BinaryInstruction(op o, VirtualRegister l, VirtualRegister r1, String bop, int r2) {
        super(o);
        this.lhs = l;
        this.rhs1 = r1;//may be null!
        this.bop = bop;
        this.width = l.getWidth();
        this.imm_rhs2 = r2;
        assert o == op.BINARY;
        this.is_imm = true;
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
        if (rhs1 != null) {
            if (is_imm) {
                String t1 = getUseReg(rhs1);
                String t3 = getDefReg(lhs);
                switch (bop) {
                    case "+":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            addi(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            add(t3, t1, "t5");
                        }
                        break;
                    case "<=":
                        li("t5", imm_rhs2);
                        slt(t3, "t5", t1);
                        xori(t3, t3, "1");
                        break;
                    case ">=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047) {
                            slti(t3, t1, String.valueOf(imm_rhs2));
                            xori(t3, t3, "1");
                        } else {
                            li("t5", imm_rhs2);
                            slt(t3, t1, "t5");
                            xori(t3, t3, "1");
                        }
                        break;
                    case "<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            slt(t3, t1, "t5");
                        }
                        break;
                    case ">":
                        li("t5", imm_rhs2);
                        slt(t3, "t5", t1);
                        break;
                    case "-":
                        li("t5", imm_rhs2);
                        sub(t3, t1, "t5");
                        break;
                    case "*":
                        if (imm_rhs2 == 1) {
                            mv(t3, t1);
                            break;
                        }
                        if (imm_rhs2 == 4) {
                            slli(t3, t1, "2");
                            break;
                        }
                        li("t5", imm_rhs2);
                        mul(t3, t1, "t5");
                        break;
                    case "/":
                        li("t5", imm_rhs2);
                        div(t3, t1, "t5");
                        break;
                    case "%":
                        li("t5", imm_rhs2);
                        rem(t3, t1, "t5");
                        break;
                    case "<<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slli(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            sll(t3, t1, "t5");
                        }
                        break;
                    case ">>":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            srai(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            sra(t3, t1, "t5");
                        }
                        break;
                    case "&":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            andi(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            and(t3, t1, "t5");
                        }
                        break;
                    case "^":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            xor(t3, t1, "t5");
                        }
                        break;
                    case "|":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            ori(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            or(t3, t1, "t5");
                        }
                        break;
                    case "==":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            xor(t3, t1, "t5");
                        }
                        seqz(t3, t3);
                        break;
                    case "!=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori(t3, t1, String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            xor(t3, t1, "t5");
                        }
                        snez(t3, t3);
                        break;
                }
                checkDefReg(lhs);
            } else {
                String t1 = getUseReg(rhs1);
                String t2 = getUseReg2(rhs2);
                String t3 = getDefReg(lhs);
                switch (bop) {
                    case "+":
                        add(t3, t1, t2);
                        break;
                    case "<=":
                        slt(t3, t2, t1);
                        xori(t3, t3, "1");
                        break;
                    case ">=":
                        slt(t3, t1, t2);
                        xori(t3, t3, "1");
                        break;
                    case "<":
                        slt(t3, t1, t2);
                        break;
                    case ">":
                        slt(t3, t2, t1);
                        break;
                    case "-":
                        sub(t3, t1, t2);
                        break;
                    case "*":
                        mul(t3, t1, t2);
                        break;
                    case "/":
                        div(t3, t1, t2);
                        break;
                    case "%":
                        rem(t3, t1, t2);
                        break;
                    case "<<":
                        sll(t3, t1, t2);
                        break;
                    case ">>":
                        sra(t3, t1, t2);
                        break;
                    case "&":
                        and(t3, t1, t2);
                        break;
                    case "^":
                        xor(t3, t1, t2);
                        break;
                    case "|":
                        or(t3, t1, t2);
                        break;
                    case "==":
                        xor(t3, t1, t2);
                        seqz(t3, t3);
                        break;
                    case "!=":
                        xor(t3, t1, t2);
                        snez(t3, t3);
                        break;
                }
                checkDefReg(lhs);
            }
        } else {
            if (is_imm) {
                String t3 = getDefReg(lhs);
                switch (bop) {
                    case "+":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            addi(t3, "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            add(t3, "x0", "t5");
                        }
                        break;
                    case "<=":
                        li("t5", imm_rhs2);
                        slt(t3, "t5", "x0");
                        xori(t3, t3, "1");
                        break;
                    case ">=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti(t3, "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            slt(t3, "x0", "t5");
                        }
                        xori(t3, t3, "1");
                        break;
                    case "<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti(t3, "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t5", imm_rhs2);
                            slt(t3, "x0", "t5");
                        }
                        break;
                    case ">":
                        li("t5", imm_rhs2);
                        slt(t3, "t5", "x0");
                        break;
                    case "-":
                        li("t5", imm_rhs2);
                        sub(t3, "x0", "t5");
                        break;
                    case "*":
                    case "&":
                    case "<<":
                    case ">>":
                        mv(t3, "x0");
                        break;
                    case "/":
                        li("t5", imm_rhs2);
                        div(t3, "x0", "t5");
                        break;
                    case "%":
                        li("t5", imm_rhs2);
                        rem(t3, "x0", "t5");
                        break;
                    case "^":
                    case "|":
                        li(t3, imm_rhs2);
                        break;
                    case "==":
                        li("t5", imm_rhs2);
                        seqz(t3, "t5");
                        break;
                    case "!=":
                        li("t5", imm_rhs2);
                        snez(t3, "t5");
                        break;
                }
                checkDefReg(lhs);
            } else {
                String t2 = getUseReg2(rhs2);
                String t3 = getDefReg(lhs);
                switch (bop) {
                    case "+":
                        add(t3, "x0", t2);
                        break;
                    case "<=":
                        slt(t3, t2, "x0");
                        xori(t3, t3, "1");
                        break;
                    case ">=":
                        slt(t3, "x0", t2);
                        xori(t3, t3, "1");
                        break;
                    case "<":
                        slt(t3, "x0", t2);
                        break;
                    case ">":
                        slt(t3, t2, "x0");
                        break;
                    case "-":
                        sub(t3, "x0", t2);
                        break;
                    case "*":
                        mul(t3, "x0", t2);
                        break;
                    case "/":
                        div(t3, "x0", t2);
                        break;
                    case "%":
                        rem(t3, "x0", t2);
                        break;
                    case "<<":
                        sll(t3, "x0", t2);
                        break;
                    case ">>":
                        sra(t3, "x0", t2);
                        break;
                    case "&":
                        and(t3, "x0", t2);
                        break;
                    case "^":
                        xor(t3, "x0", t2);
                        break;
                    case "|":
                        or(t3, "x0", t2);
                        break;
                    case "==":
                        xor(t3, "x0", t2);
                        seqz(t3, t3);
                        break;
                    case "!=":
                        xor(t3, "x0", t2);
                        snez(t3, t3);
                        break;
                }
                checkDefReg(lhs);
            }
        }
    }



    @Override
    public void optimize() {
        if (rhs1 != null) {
            rhs1.read_ex(this);
        }
        if (!is_imm) {
            rhs2.read_ex(this);
        }
        lhs.write_ex(this);
    }

    @Override
    public void collectUseAndDef() {
        use = new HashSet<>();
        def = new HashSet<>();
        if (rhs1 != null) {
            use.add(rhs1);
            rhs1.addUse(this);
        }
        if (!is_imm) {
            use.add(rhs2);
            rhs2.addUse(this);
        }
        def.add(lhs);
        lhs.addDef(this);
    }

    @Override
    public IRInstruction copyWrite(CodeSegment givenCs, Map<BasicBlock, BasicBlock> blockMap, Map<VirtualRegister, VirtualRegister> virtualMap) {
        VirtualRegister newLhs = getOrPut(givenCs, virtualMap, lhs);
        VirtualRegister newRhs1 = getOrPut(givenCs, virtualMap, rhs1);
        VirtualRegister newRhs2 = getOrPut(givenCs, virtualMap, rhs2);
        if (is_imm)
            return new BinaryInstruction(op.BINARY, newLhs, newRhs1, bop, imm_rhs2);
        else
            return new BinaryInstruction(op.BINARY, newLhs, newRhs1, bop, newRhs2);
    }

    @Override
    public String getMessage() {
        if (rhs1 != null) {
            if (is_imm)
                return lhs.getName() + " = " + rhs1.getName() + " " + bop + " " + imm_rhs2;
            else
                return lhs.getName() + " = " + rhs1.getName() + " " + bop + " " + rhs2.getName();
        } else {
            if (is_imm)
                return lhs.getName() + " = " + 0 + " " + bop + " " + imm_rhs2;
            else
                return lhs.getName() + " = " + 0 + " " + bop + " " + rhs2.getName();
        }
    }
}
