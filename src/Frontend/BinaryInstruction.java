package Frontend;

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
    public void codegen() {
        if (rhs1 != null) {
            if (is_imm) {
                if (rhs1.getWidth() == 4) {
                    LW("t1", rhs1.getAddrValue(), "sp");
                } else {
                    LB("t1", rhs1.getAddrValue(), "sp");
                }
                switch (bop) {
                    case "+":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            addi("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            add("t3", "t1", "t2");
                        }
                        break;
                    case "<=":
                        li("t2", imm_rhs2);
                        slt("t3", "t2", "t1");
                        xori("t3", "t3", "1");
                        break;
                    case ">=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047) {
                            slti("t3", "t1", String.valueOf(imm_rhs2));
                            xori("t3", "t3", "1");
                        } else {
                            li("t2", imm_rhs2);
                            slt("t3", "t1", "t2");
                            xori("t3", "t3", "1");
                        }
                        break;
                    case "<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            slt("t3", "t1", "t2");
                        }
                        break;
                    case ">":
                        li("t2", imm_rhs2);
                        slt("t3", "t2", "t1");
                        break;
                    case "-":
                        li("t2", imm_rhs2);
                        sub("t3", "t1", "t2");
                        break;
                    case "*":
                        li("t2", imm_rhs2);
                        mul("t3", "t1", "t2");
                        break;
                    case "/":
                        li("t2", imm_rhs2);
                        div("t3", "t1", "t2");
                        break;
                    case "%":
                        li("t2", imm_rhs2);
                        rem("t3", "t1", "t2");
                        break;
                    case "<<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slli("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            sll("t3", "t1", "t2");
                        }
                        break;
                    case ">>":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            srai("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            sra("t3", "t1", "t2");
                        }
                        break;
                    case "&":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            andi("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            and("t3", "t1", "t2");
                        }
                        break;
                    case "^":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            xor("t3", "t1", "t2");
                        }
                        break;
                    case "|":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            ori("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            or("t3", "t1", "t2");
                        }
                        break;
                    case "==":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            xor("t3", "t1", "t2");
                        }
                        seqz("t3", "t3");
                        break;
                    case "!=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            xori("t3", "t1", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            xor("t3", "t1", "t2");
                        }
                        snez("t3", "t3");
                        break;
                }
                if (width == 4) {
                    SW("t3", lhs.getAddrValue(), "sp");
                } else {
                    SB("t3", lhs.getAddrValue(), "sp");
                }
            } else {
                if (rhs1.getWidth() == 4) {
                    LW("t1", rhs1.getAddrValue(), "sp");
                    LW("t2", rhs2.getAddrValue(), "sp");
                } else {
                    LB("t1", rhs1.getAddrValue(), "sp");
                    LB("t2", rhs2.getAddrValue(), "sp");
                }
                switch (bop) {
                    case "+":
                        add("t3", "t1", "t2");
                        break;
                    case "<=":
                        slt("t3", "t2", "t1");
                        xori("t3", "t3", "1");
                        break;
                    case ">=":
                        slt("t3", "t1", "t2");
                        xori("t3", "t3", "1");
                        break;
                    case "<":
                        slt("t3", "t1", "t2");
                        break;
                    case ">":
                        slt("t3", "t2", "t1");
                        break;
                    case "-":
                        sub("t3", "t1", "t2");
                        break;
                    case "*":
                        mul("t3", "t1", "t2");
                        break;
                    case "/":
                        div("t3", "t1", "t2");
                        break;
                    case "%":
                        rem("t3", "t1", "t2");
                        break;
                    case "<<":
                        sll("t3", "t1", "t2");
                        break;
                    case ">>":
                        sra("t3", "t1", "t2");
                        break;
                    case "&":
                        and("t3", "t1", "t2");
                        break;
                    case "^":
                        xor("t3", "t1", "t2");
                        break;
                    case "|":
                        or("t3", "t1", "t2");
                        break;
                    case "==":
                        xor("t3", "t1", "t2");
                        seqz("t3", "t3");
                        break;
                    case "!=":
                        xor("t3", "t1", "t2");
                        snez("t3", "t3");
                        break;
                }
                if (width == 4) {
                    SW("t3", lhs.getAddrValue(), "sp");
                } else {
                    SB("t3", lhs.getAddrValue(), "sp");
                }
            }
        } else {
            if (is_imm) {
                switch (bop) {
                    case "+":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            addi("t3", "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            add("t3", "x0", "t2");
                        }
                        break;
                    case "<=":
                        li("t2", imm_rhs2);
                        slt("t3", "t2", "x0");
                        xori("t3", "t3", "1");
                        break;
                    case ">=":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti("t3", "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            slt("t3", "x0", "t2");
                        }
                        xori("t3", "t3", "1");
                        break;
                    case "<":
                        if (-2048 <= imm_rhs2 && imm_rhs2 <= 2047)
                            slti("t3", "x0", String.valueOf(imm_rhs2));
                        else {
                            li("t2", imm_rhs2);
                            slt("t3", "x0", "t2");
                        }
                        break;
                    case ">":
                        li("t2", imm_rhs2);
                        slt("t3", "t2", "x0");
                        break;
                    case "-":
                        li("t2", imm_rhs2);
                        sub("t3", "x0", "t2");
                        break;
                    case "*":
                    case "&":
                    case "<<":
                    case ">>":
                        mv("t3", "x0");
                        break;
                    case "/":
                        li("t2", imm_rhs2);
                        div("t3", "x0", "t2");
                        break;
                    case "%":
                        li("t2", imm_rhs2);
                        rem("t3", "x0", "t2");
                        break;
                    case "^":
                    case "|":
                        li("t3", imm_rhs2);
                        break;
                    case "==":
                        li("t2", imm_rhs2);
                        seqz("t3", "t2");
                        break;
                    case "!=":
                        li("t2", imm_rhs2);
                        snez("t3", "t2");
                        break;
                }
                if (width == 4) {
                    SW("t3", lhs.getAddrValue(), "sp");
                } else {
                    SB("t3", lhs.getAddrValue(), "sp");
                }
            } else {
                if (rhs2.getWidth() == 4) {
                    LW("t2", rhs2.getAddrValue(), "sp");
                } else {
                    LB("t2", rhs2.getAddrValue(), "sp");
                }
                switch (bop) {
                    case "+":
                        add("t3", "x0", "t2");
                        break;
                    case "<=":
                        slt("t3", "t2", "x0");
                        xori("t3", "t3", "1");
                        break;
                    case ">=":
                        slt("t3", "x0", "t2");
                        xori("t3", "t3", "1");
                        break;
                    case "<":
                        slt("t3", "x0", "t2");
                        break;
                    case ">":
                        slt("t3", "t2", "x0");
                        break;
                    case "-":
                        sub("t3", "x0", "t2");
                        break;
                    case "*":
                        mul("t3", "x0", "t2");
                        break;
                    case "/":
                        div("t3", "x0", "t2");
                        break;
                    case "%":
                        rem("t3", "x0", "t2");
                        break;
                    case "<<":
                        sll("t3", "x0", "t2");
                        break;
                    case ">>":
                        sra("t3", "x0", "t2");
                        break;
                    case "&":
                        and("t3", "x0", "t2");
                        break;
                    case "^":
                        xor("t3", "x0", "t2");
                        break;
                    case "|":
                        or("t3", "x0", "t2");
                        break;
                    case "==":
                        xor("t3", "x0", "t2");
                        seqz("t3", "t3");
                        break;
                    case "!=":
                        xor("t3", "x0", "t2");
                        snez("t3", "t3");
                        break;
                }
                if (width == 4) {
                    SW("t3", lhs.getAddrValue(), "sp");
                } else {
                    SB("t3", lhs.getAddrValue(), "sp");
                }
            }
        }
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
