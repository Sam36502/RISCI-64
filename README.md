# RISCI-64
*A 2-Bit Virtual Machine*\

This is just a little Project I made to mess about with Theoretical CPU architecture.\
**Disclaimer:** Read my code at your own risk. it's is\
predominantly unorganized Hobby code. If you find any\
possible imporovements, feel free to let me know.

Now on to the actual documentation...

# How to use the VM
Run the .jar from this repository.
You will probably want to also get the Compiler [here](https://github.com/Sam36502/RISCompile)

With the compiler you can make '.ass' (Assembly) files that can be run from this VM.
However, you do not need the Compiler as the VM can also be used via a shell.
The shell uses mostly the same keywords as the .RIS files, plus some extras for debugging.

# The Name
Please note, this has no affiliation with RISC OS. The name mainly comes
from the concept of a Reduced Instruction Set Computer, which I thought fit
the description of a 2-bit computer. The 'I' on the end of the name has no
Fixed meaning. I just thought it gave it a more unique and better-sounding
name. And of the course the 64 is reference to the 64 Bytes of available RAM / Program memory.

# CPU Opcodes
Num | Bitcode | Action
--- | --- | ---
0 | 00 | Perform the Operation specified by the argument on the top element(s) of the stack.
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

**8x8 Screen Output**
This represents an 8x8 grid of 16-colour pixels based on the Apple Macintosh default 16-color palette.
to set a specific pixel to a colour, you can look up the colour in the palette below and get the 4-bit binary.
After that you will have to figure out how it will fit in the grid. Every byte in the addresses from 0x20 to 0x38 represents
2 pixels in the 8x8 grid. so say you wanted to set the top left pixel to red (3 -> 0011).
you would then set byte 0x20 to `0011 0000`.

to set the pixel below and to the right of the first one to blue (6 -> 0110) you would set byte 0x24 to `0000 0110`

Colour | Name | Hex RGB | Num | 4-Bit Binary
--- | --- | ---
![#ffffff](https://placehold.it/15/ffffff/000000?text=+) | White | ffffff | 0 | 0000
![#ffff00](https://placehold.it/15/ffff00/000000?text=+) | Yellow | ffff00 | 1 | 0001
![#ff6600](https://placehold.it/15/ff6600/000000?text=+) | Orange | ff6600 | 2 | 0010
![#dd0000](https://placehold.it/15/dd0000/000000?text=+) | Red | dd0000 | 3 | 0011
![#ff0099](https://placehold.it/15/ff0099/000000?text=+) | Magenta | ff0099 | 4 | 0100
![#330099](https://placehold.it/15/330099/000000?text=+) | Purple | 330099 | 5 | 0101
![#0000cc](https://placehold.it/15/0000cc/000000?text=+) | Blue | 0000cc | 6 | 0110
![#0099ff](https://placehold.it/15/0099ff/000000?text=+) | Cyan | 0099ff | 7 | 0111
![#00aa00](https://placehold.it/15/00aa00/000000?text=+) | Green | 00aa00 | 8 | 1000
![#006600](https://placehold.it/15/006600/000000?text=+) | Dark Green | 006600 | 9 | 1001
![#663300](https://placehold.it/15/663300/000000?text=+) | Brown | 663300 | 10 | 1010
![#996633](https://placehold.it/15/996633/000000?text=+) | Tan | 996633 | 11 | 1011
![#bbbbbb](https://placehold.it/15/bbbbbb/000000?text=+) | Light Grey | bbbbbb | 12 | 1100
![#888888](https://placehold.it/15/888888/000000?text=+) | Medium Grey | 888888 | 13 | 1101
![#444444](https://placehold.it/15/444444/000000?text=+) | Dark Grey | 444444 | 14 | 1110
![#000000](https://placehold.it/15/000000/000000?text=+) | Black | 000000 | 15 | 1111
