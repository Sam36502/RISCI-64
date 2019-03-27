# RISCI-64
*A 2-Bit Virtual Machine*\
\
This is just a little Project I made to mess about with Theoretical CPU architecture.\
**DISCLAIMER:** I only know the basics of System Architecture design!
There is probably a much better way to make a 2-Bit VM.\
**DISCLAIMER 2:** This is all hobby code. Comments are few and probably worded strangely, and
I wrote a lot of this code in seperate, unplanned stages. If you notice any odd bits of code,\
such as redundancies or inefficiencies, I'd love if you could let me know of a better solution. Thanks.\
\
*whew*\
\
Now on to the actual documentation...

# CPU Opcodes

0 00 Perform the Operation specified by the argument on the top element of the stack.\
1 01 Pop the top value off the stack and put it at the argument address in memory.\
2 10 Push the value at the argument address onto the stack.\
3 11 Jump to the line specified by the argument in program memory.
\
Every command that's passed to the CPU is 1 Byte.
2 Bits for the instruction and a 6-Bit Argument.
Here's an example:
\
`10000011` -> pushfrom 3 -> Take the value of memory address 3 (0x03) and push it onto the stack
\
Because of the 6-Bit argument, The RISCI-64 can only access 64 bytes of RAM.

# RAM

Ram is split up into a block of general variables, many system variables such as stdout, and an 8x8 grid's worth of nibbles.
If you want to output an image, you can write the 4-Bit Pixel data into the top of RAM and update the screen.
\
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
0x1E       0  [Remote Command]
0x1F        0 [Sound Output]

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

**Remote Command**
This byte allows any external process to write commands to the CPU.
If you want to use the RISCI with a "shell" instead of by writing
programs, this is the byte to use.

**Sound Output**
This byte was mainly just an idea, I thought might be cool.
The top 3 bits determine the "Instrument" and the left-over
5 bits determines the pitch. (This is still in the concept phase, There's no defined list of instruments or Pitches yet)

**8x8 Screen Output**
These 32 bytes are each split in half to make 64 nibbles.
Each nibble corresponds to a pixel in an 8x8 grid. The first nibble (0x20) is the top left
of the screen and all other pixels follow. Left to right, top to bottom.
The 16 possible colors correspomd to the Microsoft Windows default 16-color palette:
```
0000 00 Black
0001 01 Maroon
0010 02 Green
0011 03 Olive
0100 04 Navy
0101 05 Purple
0110 06 Teal
0111 07 Silver
1000 08 Gray
1001 09 Red
1010 10 Lime
1011 11 Yellow
1100 12 Blue
1101 13 Fuchsia
1110 14 Aqua
1111 15 White
```
