package Frontend;

public class MallocInstruction extends IRInstruction {
    VirtualRegister start_addr, malloc_size;
    private int malloc_size_int;
    private boolean is_class_malloc;
    MallocInstruction(op o, VirtualRegister sa, VirtualRegister ms) {
        super(o);
        assert o == op.MALLOC;
        this.start_addr = sa;
        this.malloc_size = ms;
        this.is_class_malloc = false;
    }

    MallocInstruction(op o, VirtualRegister sa, int ms) {
        super(o);
        assert o == op.MALLOC;
        this.start_addr = sa;
        this.malloc_size_int = ms;
        this.is_class_malloc = true;
    }

    @Override
    public void codegen() {
        if (this.is_class_malloc) {
            li("a0", malloc_size_int);
            call("malloc");
            sw("a0", start_addr.getAddrValue() + "(sp)");
        } else {
            lw("a0", malloc_size.getAddrValue() + "(sp)");
            call("malloc");
            sw("a0", start_addr.getAddrValue() + "(sp)");
        }
    }

    @Override
    public String getMessage() {
        if (this.is_class_malloc)
            return (start_addr.getName() + " = malloc(" + malloc_size_int + ")");
        else
            return (start_addr.getName() + " = malloc(" + malloc_size.getName() + ")");
    }
}
