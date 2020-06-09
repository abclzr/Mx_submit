package Backend;

import Frontend.IRInstruction;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseRegister {
    int index;
    Set<IRInstruction> def = new HashSet<>();
    Set<IRInstruction> use = new HashSet<>();
    public MachineRegister color = null;

    public void addDef(IRInstruction inst) {
        def.add(inst);
    }

    public void delDef(IRInstruction inst) {
        def.remove(inst);
    }

    public void addUse(IRInstruction inst) {
        use.add(inst);
    }

    public void delUse(IRInstruction inst) {
        use.remove(inst);
    }

    public MachineRegister getColor() {
        return color;
    }

    public void setColor(MachineRegister color) {
        this.color = color;
    }

    public abstract String getName();
}
