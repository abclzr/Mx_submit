package Frontend;

import java.util.ArrayList;
import java.util.List;

public class ConstantPoolTable {
    private byte[] pool;
    private int length;
    private List<String> constStringList;

    ConstantPoolTable() {
        pool = new byte[10000000];
        length = 0;
        constStringList = new ArrayList<>();
    }

    public int getAddress() {
        return length;
    }

    public void allocate(byte c) {
        pool[length++] = c;
    }

    public String allocate(String s) {
        constStringList.add(s);
        int n = s.length();
        this.allocate((byte) n);
        this.allocate((byte) (n >> 8));
        this.allocate((byte) (n >> 16));
        this.allocate((byte) (n >> 24));
        byte[] buffer = s.getBytes();
        for (int i = 0; i < n; ++i)
            this.allocate(buffer[i]);
        this.allocate((byte) 0);
        return ".str." + constStringList.size();
    }

    public void printall() {
        int i = 1;
        for (String s : constStringList) {
            System.out.println("\t.globl \t.str." + i);
            System.out.println(".str." + i + ":");
            System.out.println("\t.asciz \t" + s);
            i++;
        }
    }
}
