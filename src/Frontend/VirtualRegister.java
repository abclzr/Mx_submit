package Frontend;

import Semantic.Type;
import Semantic.VariableSymbol;

public class VirtualRegister {
    private CodeSegment inCodeSegment;
    private VariableSymbol varSymbol;
    private int value;
    private int width;
    private Type type;
    private int relativeAddress;
    private Address addr;
    private String globalVarName;
    private int write_times;
    private int read_times;
    private IRInstruction last_read;
    private IRInstruction last_write;
    private String occupyReg;
    private VirtualRegister nextReg;

    public void setNextReg(VirtualRegister nextReg) {
        this.nextReg = nextReg;
    }

    public VirtualRegister getNextReg() {
        return nextReg;
    }

    public String getOccupyReg() {
        return occupyReg;
    }

    public void setOccupyReg(String occupyReg) {
        this.occupyReg = occupyReg;
    }

    public int getRead_times() {
        return read_times;
    }

    public int getWrite_times() {
        return write_times;
    }

    public void read_ex(IRInstruction i) {
        ++read_times;
        last_read = i;
    }

    public void write_ex(IRInstruction i) {
        ++write_times;
        last_write = i;
    }

    public IRInstruction getLast_read() {
        return last_read;
    }

    public IRInstruction getLast_write() {
        return last_write;
    }

    public int getWidth() {
        return width;
    }

    public void setGlobalVarName(String globalVarName) {
        this.globalVarName = globalVarName;
    }

    public String getGlobalVarName() {
        return globalVarName;
    }

    public String getName() {
        return "%" + addr.getAddr();
    }

    VirtualRegister(CodeSegment inCode, Type tp) {
        this.inCodeSegment = inCode;
        this.value = 0;
        this.width = tp.getWidth();
        this.type = tp;
        if (inCode != null) this.relativeAddress = inCode.Allocate(this.width);
        this.addr = new Address();
        this.addr.setAddr(this.relativeAddress);
        this.read_times = 0;
        this.write_times = 0;
        this.last_read = null;
        this.last_write = null;
        this.occupyReg = null;
        this.nextReg = null;
    }

    public int getRelativeAddress() {
        return relativeAddress;
    }

    public CodeSegment getInCodeSegment() {
        return inCodeSegment;
    }

    public int getAddrValue() {
        return this.addr.getAddr();
    }

    public Address getAddr() {
        return addr;
    }

    public void setAddr(Address addr) {
        this.addr = addr;
    }
}
