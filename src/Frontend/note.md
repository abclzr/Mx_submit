#virtual register
Every virtual register is mapped to a physical address in ram.

The value of a virtual register is:

 - int type: 4B in ram, the value of the integer
 - bool type: 1B in ram, the value of boolean
 - string type: 4B in ram, the address of string
 - array type: 4B in ram, the address of array
 - class type: 4B in ram, the address of class object

sp(stack pointer), ap(allocator pointer) should be of global variables.

#CodeSegment
Each code segment contains a function. The first code segment should initialize all global variables.

Each code segment's first virtual registers are parameters of its function. Remember each class method's first parameter is this-pointer.

In the codesegment construction process, each variable or right value is mapped to a virtual register. And virtual register will request space in ram(1B or 4B) from the codesegment. Codesegment allocate space and return the start address to the virtual register. 

The address stored in the virtual register will be useful in codegen stage. It's the virtual register's offset to sp.