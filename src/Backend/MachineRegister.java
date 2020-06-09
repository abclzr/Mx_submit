package Backend;

import javax.crypto.Mac;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MachineRegister extends BaseRegister {
    static int K;

    static Map<String, MachineRegister> allRegs;
    static Set<MachineRegister> usefulRegs;

    public static void init() {
        allRegs = new HashMap<>();
        for (int i = 0; i <= 7; ++i) {
            String na = "a" + i;
            allRegs.put(na, new MachineRegister(na));
        }
        allRegs.put("sp", new MachineRegister("sp"));
        usefulRegs = new HashSet<>();
        for (int i = 0; i <= 11; ++i) {
            String na = "s" + i;
            MachineRegister nm = new MachineRegister(na);
            usefulRegs.add(nm);
            allRegs.put(na, nm);
        }
        K = usefulRegs.size();
    }

    public static MachineRegister get(String name) {
        return allRegs.get(name);
    }

    String name;

    MachineRegister(String _name) {
        this.name = _name;
        super.color = this;
    }

    @Override
    public String getName() {
        return name;
    }
}
