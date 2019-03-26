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
Now on to the actual documentation...\

# CPU Opcodes

0 00 Perform the Operation specified by the argument on the top element of the stack.\
1 01 Pop the top value off the stack and put it at the argument address in memory.\
2 10 Push the value at the argument address onto the stack.\
3 11 Jump to the line specified by the argument in program memory.
