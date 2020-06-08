package Frontend;

import Semantic.Scope;

import java.util.*;

public class RegisterAllocator {
    Stack<String> unused;
    Map<String, VirtualRegister> map;
    VirtualRegister head;
    VirtualRegister tail;

    RegisterAllocator() {
        unused = new Stack<>();
        map = new LinkedHashMap<>();
        head = new VirtualRegister(null, Scope.intType);
        tail = head;
    }

    void init(List<String> list) {
        for (String s : list) {
            unused.add(s);
            map.put(s, null);
        }
    }

    String askForReg(VirtualRegister r, int id, boolean load) {
        if (r.getOccupyReg() == null) {
            if (unused.isEmpty())
                flush_one(id);
            assert !unused.isEmpty();
            VirtualRegister p = head;
            while (p.getNextReg() != null)
                p = p.getNextReg();
            p.setNextReg(r);
            r.setNextReg(null);
            String newReg = unused.pop();
            map.put(newReg, r);
            r.setOccupyReg(newReg);
            if (load) {
                if (r.getWidth() == 4)
                    IRInstruction.LW(newReg, r.getAddrValue(), "sp");
                else
                    IRInstruction.LB(newReg, r.getAddrValue(), "sp");
            }
            return newReg;
        } else {
            VirtualRegister p = head;
            VirtualRegister q = p.getNextReg();
            while (q != null) {
                if (q == r) {
                    p.setNextReg(q.getNextReg());
                    break;
                }
                p = q;
                q = q.getNextReg();
            }
            assert q != null;
            while (p.getNextReg() != null) {
                p = p.getNextReg();
            }
            p.setNextReg(q);
            q.setNextReg(null);
            return q.getOccupyReg();
        }
    }

    void flush_one(int id) {
        assert head.getNextReg() != null;
        VirtualRegister q = head.getNextReg();
        if (q.getWidth() == 4)
            IRInstruction.SW(q.getOccupyReg(), q.getAddrValue(), "sp");
        else
            IRInstruction.SB(q.getOccupyReg(), q.getAddrValue(), "sp");
        head.setNextReg(q.getNextReg());
        q.setNextReg(null);
        unused.add(q.getOccupyReg());
        map.put(q.getOccupyReg(), null);
        q.setOccupyReg(null);
    }

    void flush_all(int id) {
        while (head.getNextReg() != null) {
            flush_one(id);
        }
    }
}
