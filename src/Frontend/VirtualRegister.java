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
        this.relativeAddress = inCode.Allocate(this.width);
        this.addr = new Address();
        this.addr.setAddr(this.relativeAddress);
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
