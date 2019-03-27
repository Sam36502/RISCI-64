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
These are all the addresses (each 0 represents a byte of data, except for the addresses):

```
0x00 00000000 [Space for general Variables]
0x08 00000000
0x10 00000000
0x18 00

0x1A   0      [Top of Stack value]
0x1B   0      [Standard Input]
0x1C   0      [Standard Output]
0x1D   0      [Standard Error]
0x1E   0      [Remote Command]
0x1F   0      [Sound Output]

0x20 00000000 [8x8 Screen Output]
0x28 00000000
0x30 00000000
0x38 00000000
```
