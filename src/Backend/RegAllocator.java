package Backend;

import Frontend.CodeSegment;
import Frontend.CopyInstruction;
import Frontend.IRInstruction;
import Frontend.VirtualRegister;
import Utils.Pair;

import java.util.*;

public class RegAllocator {

    final int MAX_DEGREE = 1000000;

    public RegAllocator(CodeSegment cs) {
        init(cs);
        exec(cs);
        //cs.getAllVregs().forEach((reg) -> ((VirtualRegister) reg).tryReplace());
    }

    int K = MachineRegister.K;
    Set<IRInstruction> coalescedMoves, constrainedMoves, frozenMoves, worklistMoves, activeMoves;
    Set<Pair<BaseRegister, BaseRegister>> adjSet;
    Set<BaseRegister> precolored, initial;
    Set<BaseRegister> coalescedNodes;
    Map<BaseRegister, BaseRegister> alias;
    Map<BaseRegister, Set<IRInstruction>> moveList;
    Map<BaseRegister, Integer> degree;
    Map<BaseRegister, Set<BaseRegister>> adjList;
    Set<BaseRegister> spillWorklist, freezeWorklist, simplifyWorklist;
    Stack<BaseRegister> selectStack;
    Set<BaseRegister> coloredNodes, spilledNodes;
    Set<BaseRegister> newTemps;
    Random rand = new Random();

    private void exec(CodeSegment func) {
        rebuild(func);
        func.livenessAnalysis();
        build(func);
        makeWorklist();
        do {
//            System.err.printf("SIZE: %d\n", adjSet.size());
            if (!simplifyWorklist.isEmpty()) simplify();
            else if (!worklistMoves.isEmpty()) coalesce();
            else if (!freezeWorklist.isEmpty()) freeze();
            else if (!spillWorklist.isEmpty()) selectSpill();
        }
        while (!(simplifyWorklist.isEmpty() && worklistMoves.isEmpty() && freezeWorklist.isEmpty() && spillWorklist.isEmpty()));
        assignColors(func);
        /*
        if (!spilledNodes.isEmpty()) {
            rewriteProgram(func);
            exec(func);
        }
         */
    }

    private void init(CodeSegment func) {
        coalescedMoves = new HashSet<>();
        constrainedMoves = new HashSet<>();
        frozenMoves = new HashSet<>();
        worklistMoves = new HashSet<>();
        activeMoves = new HashSet<>();
        adjSet = new HashSet<>();
        precolored = new HashSet<>();
//        precolored = new HashSet<>(Register.Machine.regs.values());
        initial = func.getAllVregs();
        coalescedNodes = new HashSet<>();
        spillWorklist = new HashSet<>();
        freezeWorklist = new HashSet<>();
        simplifyWorklist = new HashSet<>();
        selectStack = new Stack<>();
        coloredNodes = new HashSet<>();
        spilledNodes = new HashSet<>();
        alias = new HashMap<>();
        moveList = new HashMap<>();
        adjList = new HashMap<>();
        degree = new HashMap<>();
        newTemps = new HashSet<>();
    }

    private void rebuild(CodeSegment func) {
        coalescedMoves = new HashSet<>();
        constrainedMoves = new HashSet<>();
        frozenMoves = new HashSet<>();
        activeMoves = new HashSet<>();
        alias = new HashMap<>();
        adjSet = new HashSet<>();

        initial.forEach((reg) -> ((VirtualRegister) reg).color = null);
        moveList.forEach((k, v) -> moveList.put(k, new HashSet<>()));
        degree.forEach((k, v) -> degree.put(k, 0));
        adjList.forEach((k, v) -> adjList.put(k, new HashSet<>()));

        setUnion(precolored, initial).forEach((reg) -> moveList.putIfAbsent(reg, new HashSet<>()));
        initial.forEach((reg) -> degree.putIfAbsent(reg, 0));
        precolored.forEach((reg) -> degree.put(reg, MAX_DEGREE));
        setUnion(precolored, initial).forEach((reg) -> adjList.putIfAbsent(reg, new HashSet<>()));
    }

    private void build(CodeSegment func) {
        for (var block : func.blocks) {
            var live = func.getLiveOut(block);
            for (int i = block.newInstList.size() - 1; i >= 0; i--) {
                var inst = block.newInstList.get(i);
                if (inst instanceof CopyInstruction && !((CopyInstruction) inst).is_imm_assign() && ((CopyInstruction) inst).getRhs() instanceof VirtualRegister) {
                    live.removeAll(inst.getUse());
                    for (var n : setUnion(inst.getUse(), inst.getDef())) {
                        moveList.get(n).add(inst);
                    }
                    worklistMoves.add(inst);
                }
                live.addAll(inst.getDef());
//                live.forEach(x -> {System.err.print(x.getName() + ", ");});
//                System.err.println("");
                // Prevent allocation to zero!!
                //live.add(Register.Machine.get("zero"));
                for (var d : inst.getDef()) {
                    if (live != null)
                        for (var l : live) {
                            addEdge(l, d);
                        }
                }
//                System.err.println(inst.getMessage());
                live.removeAll(inst.getDef());
                live.addAll(inst.getUse());
//                System.err.print("remove: ");
//                inst.getDef().forEach(x -> {System.err.print(x.getName() + ", ");});
//                System.err.println("");
//                System.err.print("add: ");
//                inst.getUse().forEach(x -> {System.err.print(x.getName() + ", ");});
//                System.err.println("");
//                System.err.print("finish live: ");
//                live.forEach(x -> {System.err.print(x.getName() + ", ");});
//                System.err.println("");
            }
        }
    }

    private void addEdge(BaseRegister u, BaseRegister v) {
//        System.err.println(u.getName() + ", " + v.getName());
//        assert adjList.get(u).contains(v) == adjSet.contains(new Pair<>(u, v));
        if (u != v && !adjSet.contains(new Pair<>(u, v))) {
            adjSet.add(new Pair<>(u, v));
            adjSet.add(new Pair<>(v, u));
//            System.err.printf("ADD EDGE: %s %s\n", u, v);
            if (!precolored.contains(u)) {
                adjList.get(u).add(v);
                degree.put(u, degree.getOrDefault(u, 0) + 1);
            }
            if (!precolored.contains(v)) {
                adjList.get(v).add(u);
                degree.put(v, degree.getOrDefault(v, 0) + 1);
            }
        }
    }

    private Set<IRInstruction> getNodeMoves(BaseRegister n) {
        var ret = new HashSet<>(activeMoves);
        ret.addAll(worklistMoves);
        ret.retainAll(moveList.getOrDefault(n, new HashSet<>()));
        return ret;
    }

    private boolean isMoveRelated(BaseRegister n) {
        return getNodeMoves(n).size() > 0;
    }

    private void makeWorklist() {
        for (var n : initial) {
            if (degree.getOrDefault(n, 0) >= K) {
                spillWorklist.add(n);
            } else if (isMoveRelated(n)) {
                freezeWorklist.add(n);
            } else {
                simplifyWorklist.add(n);
            }
        }
        initial.clear();
    }

    private Set<BaseRegister> getAdjacent(BaseRegister n) {
        var ret = new HashSet<>(adjList.get(n));
//        System.err.println(adjList.get(n).size());
        ret.removeAll(selectStack);
        ret.removeAll(coalescedNodes);
        return ret;
    }

    private void simplify() {
        var n = new ArrayList<>(simplifyWorklist).get(0);
        simplifyWorklist.remove(n);
//        System.err.printf("Push to stack %s\n", n);
        selectStack.push(n);
        for (var m : getAdjacent(n)) {
            decrementDegree(m);
        }
    }

    private void enableMoves(Set<BaseRegister> nodes) {
        for (var n : nodes) {
            for (var m : getNodeMoves(n)) {
                if (activeMoves.contains(m)) {
                    activeMoves.remove(m);
                    worklistMoves.add(m);
                }
            }
        }
    }

    private void decrementDegree(BaseRegister m) {
        int d = degree.get(m);
        degree.put(m, d - 1);
        if (d == K) {
            var t = getAdjacent(m);
            t.add(m);
            enableMoves(t);
            spillWorklist.remove(m);
            if (isMoveRelated(m)) {
                freezeWorklist.add(m);
            } else {
                simplifyWorklist.add(m);
            }
        }
    }

    private void addWorkList(BaseRegister u) {
        if ((!precolored.contains(u)) && (!isMoveRelated(u)) && degree.get(u) < K) {
            freezeWorklist.remove(u);
            simplifyWorklist.add(u);
        }
    }

    private boolean Ok(BaseRegister t, BaseRegister r) {
        return degree.get(t) < K || precolored.contains(t) || adjSet.contains(new Pair<>(t, r));
    }

    private boolean isAllOk(Set<BaseRegister> set, BaseRegister u) {
        for (var s : set)
            if (!Ok(s, u)) return false;
        return true;
    }

    private boolean isConservative(Set<BaseRegister> nodes) {
        int k = 0;
        for (var n : nodes)
            if (degree.get(n) >= K) k = k + 1;
        return k < K;
    }

    private BaseRegister getAlias(BaseRegister n) {
        if (coalescedNodes.contains(n)) return getAlias(alias.get(n));
        else return n;
    }

    private <T> Set<T> setUnion(Set<T> a, Set<T> b) {
        var ret = new HashSet<>(a);
        ret.addAll(b);
        return ret;
    }

    private void coalesce() {
        var m = new ArrayList<>(worklistMoves).get(0);
        assert m instanceof CopyInstruction;
        var x = getAlias(((CopyInstruction) m).getLhs());
        var y = getAlias(((CopyInstruction) m).getRhs());
        BaseRegister u, v;
        if (precolored.contains(y)) {
            u = y;
            v = x;
        } else {
            u = x;
            v = y;
        }
        worklistMoves.remove(m);
        if (u == v) {
            coalescedMoves.add(m);
            addWorkList(u);
        } else if (precolored.contains(v) || adjSet.contains(new Pair<>(u, v))) {
            constrainedMoves.add(m);
            addWorkList(u);
            addWorkList(v);
        } else if ((precolored.contains(u) && isAllOk(getAdjacent(v), u)) ||
                ((!precolored.contains(u)) && isConservative(setUnion(getAdjacent(u), getAdjacent(v))))) {
            coalescedMoves.add(m);
            combine(u, v);
            addWorkList(u);
        } else {
            activeMoves.add(m);
        }
    }

    private void combine(BaseRegister u, BaseRegister v) {
//        assert u != Register.Machine.get("zero");
//        System.err.printf("Combine: %s %s\n", u, v);
        if (freezeWorklist.contains(v)) {
            freezeWorklist.remove(v);
        } else {
            spillWorklist.remove(v);
        }
        coalescedNodes.add(v);
        alias.put(v, u);
        moveList.get(u).addAll(moveList.get(v));
        enableMoves(Collections.singleton(v));
        for (var t : getAdjacent(v)) {
            addEdge(t, u);
            decrementDegree(t);
        }
        if (degree.get(u) >= K && freezeWorklist.contains(u)) {
            freezeWorklist.remove(u);
            spillWorklist.add(u);
        }
    }

    private void freezeMoves(BaseRegister u) {
        for (var m : getNodeMoves(u)) {
            var x = ((CopyInstruction) m).getLhs();
            var y = ((CopyInstruction) m).getRhs();
            BaseRegister v;
            if (getAlias(y) == getAlias(u)) {
                v = getAlias(x);
            } else {
                v = getAlias(y);
            }
            activeMoves.remove(m);
            frozenMoves.add(m);
            if (freezeWorklist.contains(v) && getNodeMoves(v).size() == 0) {
                freezeWorklist.remove(v);
                simplifyWorklist.add(v);
            }
        }
    }

    private void freeze() {
        var u = new ArrayList<>(freezeWorklist).get(0);
        freezeWorklist.remove(u);
        simplifyWorklist.add(u);
        freezeMoves(u);
    }

    class degreeComparator implements Comparator<BaseRegister> {
        @Override
        public int compare(BaseRegister o1, BaseRegister o2) {
            return -degree.get(o1).compareTo(degree.get(o2));
        }
    }

    private void selectSpill() {
        //TODO: using better heuristic
        var t = new ArrayList<>(spillWorklist);
        t.sort(new degreeComparator());

        BaseRegister m = t.get(0);
        while (t.size() > 0 && newTemps.contains(t.get(0))) t.remove(0);
        if (t.size() > 0) m = t.get(0);

        spillWorklist.remove(m);
        simplifyWorklist.add(m);
        freezeMoves(m);
    }

    private void assignColors(CodeSegment func) {
        while (!selectStack.isEmpty()) {
            var n = selectStack.pop();
            var okColors = new HashSet<>(MachineRegister.usefulRegs);
            assert okColors.size() == K;
            for (var w : adjList.get(n)) {
                if (setUnion(coloredNodes, precolored).contains(getAlias(w))) {
                    okColors.remove(getAlias(w).getColor());
                }
            }
            if (okColors.isEmpty()) {
                spilledNodes.add(n);
                n.setColor(null);
            } else {
                coloredNodes.add(n);
                var c = new ArrayList<>(okColors).get(0);
                n.setColor(c);
            }
        }
        for (var n : coalescedNodes) {
            n.setColor(getAlias(n).getColor());
        }
    }
/*
    private void rewriteProgram(CodeSegment func) {
        Set<BaseRegister> newTemps = new HashSet<>();
//        System.err.printf("Spilled %d nodes\n", spilledNodes.size());

        for (var v : spilledNodes) {
            var p = func.newVariable();
            for (var inst : new HashSet<>(v.def)) {
                var reg = new Register.Virtual();
                newTemps.add(reg);
                var newInst = new AsmInst.store(reg, Register.Machine.get("sp"), p, 4, inst.belong);
                inst.belong.moveInst(newInst, inst.belong.instructions.indexOf(inst) + 1);
                inst.replaceDefWith(v, reg);
            }

            for (var inst : new HashSet<>(v.use)) {
                var reg = new Register.Virtual();
                newTemps.add(reg);
                var newInst = new AsmInst.load(Register.Machine.get("sp"), p, 4, reg, inst.belong);
                inst.belong.moveInst(newInst, inst.belong.instructions.indexOf(inst));
                inst.replaceUseWith(v, reg);
            }
        }
        this.newTemps.addAll(newTemps);

        spilledNodes = new HashSet<>();
        initial = setUnion(setUnion(coloredNodes, coalescedNodes), newTemps);
        coloredNodes = new HashSet<>();
        coalescedNodes = new HashSet<>();
    }
 */
}
