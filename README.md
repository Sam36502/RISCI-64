# RISCI-64
*A 2-Bit Virtual Machine*\
\
This is just a little Project I made to mess about with Theoretical CPU architecture.\
**Disclaimer:** I only know the basics of System Architecture design!
There is probably a much better way to make a 2-Bit VM.\
**Disclaimer 2:** This is all hobby code. Comments are few and probably worded strangely, and
I wrote a lot of this code in seperate, unplanned stages. If you notice any odd bits of code,\
such as redundancies or inefficiencies, I'd love if you could let me know of a better solution. Thanks.\
\
*whew*\
\
Now on to the actual documentation...

# How to use the VM
Run the .jar from this repository.
You will probably want to also get the Compiler [here](https://github.com/Sam36502/RISCompile)

With the compiler you can make '.ass' (Assembly) files that can be run from this VM.
However, you do not need the Compiler as the VM can also be used via a shell.
The shell uses mostly the same keywords as the .RIS files, plus some extras for debugging.

# CPU Opcodes
Num | Bitcode | Action
--- | --- | ---
0 | 00 | Perform the Operation specified by the argument on the top element of the stack.
1 | 01 | Pop the top value off the stack and put it at the argument address in memory.
2 | 10 | Push the value at the argument address onto the stack.
3 | 11 | Jump to the line specified by the argument in program memory.

Every command that's passed to the CPU is 1 Byte.
2 Bits for the instruction and a 6-Bit Argument.
Here's an example:

`10000011` -> pushfrom 3 -> Take the value of memory address 3 (0x03) and push it onto the stack

Because of the 6-Bit argument, The RISCI-64 can only access 64 bytes of RAM.

# RAM

Ram is split up into a block of general variables, many system variables such as stdout, and an 8x8 grid's worth of nibbles.
If you want to output an image, you can write the 4-Bit Pixel data into the top of RAM and update the screen.

These are all the addresses (each 0 in the block represents a byte of data):

```
0x00 00000000 [Space for general Variables]
0x08 00000000
0x10 00000000
0x18 00

0x1A   0      [Top of Stack value]
0x1B    0     [Standard Input]
0x1C     0    [Standard Output]
0x1D      0   [Standard Error]
0x1E       0  [I dunno (general variable)]
0x1F        0 [I dunno (general variable)]

0x20 00000000 [8x8 Screen Output]
0x28 00000000
0x30 00000000
0x38 00000000
```
Now to explain what all these Do:

**General Variables**
These are pretty self explanatory, they're for storage of values.
It's important to note that RAM states can be stored in small .mem
files. You can use these memory files to start the program with some variables
already initialized. This is helpful for ASCII strings and such.

**Top of Stack Value**
This is where the system stores the Stack's top value. The stack itself
however, is seperate from RAM. This variable exists so that you can push an address itself
onto the stack and "go" there in memory. E.g.:\
`pushfrom 0x03` -> Pushes value of 3 onto stack.\
`pushfrom 0x1A` -> Pushes value of the address on top of the stack, onto the stack. (Does this even make sense?)

**Standard Input**
This is where the system will try to read input from. If a user
enters something through the I/O system it will be written here.
Only 1 Byte at a time though.

**Standard Output**
This is where the user should push their characters to be output.
Note that this will be read by I/O as an ASCII character when output.

**Standard Error**
This is where the system will write Runtime Errors and where the User
can push their own error Messages.
