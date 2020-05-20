package Semantic;

import AST.ASTNode;
import Frontend.Address;
import Frontend.VirtualRegister;
import Utils.Position;

public class VariableSymbol extends Symbol {
    private VirtualRegister virtualRegister;
    private int offsetInClass;
    private Address addrInClass;

    public void setOffsetInClass(int offsetInClass) {
        this.offsetInClass = offsetInClass;
        this.addrInClass = new Address();
        this.addrInClass.setAddr(offsetInClass);
    }

    public Address getAddrInClass() {
        return addrInClass;
    }

    public int getOffsetInClass() {
        return offsetInClass;
    }

    public VariableSymbol(Type tp, String na, ASTNode def, Position pos) {
        super(tp, na, def, pos);
    }

    public void setVirtualRegister(VirtualRegister virtualRegister) {
        this.virtualRegister = virtualRegister;
    }

    public VirtualRegister getVirtualRegister() {
        return virtualRegister;
    }

    @Override
    public boolean isVariableSymbol() {
        return true;
    }

    @Override
    public boolean isFunctionSymbol() {
        return false;
    }
}
