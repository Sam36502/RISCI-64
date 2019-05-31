package virtualMachine;

import java.util.EmptyStackException;
import java.util.Stack;

public class CPU {
	
	//Main stack & RAM object
	public Stack<Byte> stack = new Stack<Byte>();
	public RAM mem = new RAM();
	public ProgMem prog = new ProgMem();
	String stdin = "";
	int stdincount = 0;
	
	public void perform (int op) {
		switch (op) {
			case 0:
				break;
			case 1:
				stack.push((byte) (stack.pop() + stack.pop()));
				break;
			case 2:
				stack.push((byte) (stack.pop() - stack.pop()));
				break;
			case 3:
				stack.push((byte) (stack.pop() * stack.pop()));
				break;
			case 4:
				stack.push((byte) (stack.pop() / stack.pop()));
				break;
			case 5:
				stack.push((byte) (stack.pop() % stack.pop()));
				break;
			case 6:
				stack.push((byte) (stack.pop() + 1));
				break;
			case 7:
				stack.push((byte) (stack.pop() - 1));
				break;
		}
	}
	
	public void compute (byte operation) throws EmptyStackException {
		
		//Splits operation into command and argument
		String binString = Integer.toBinaryString(operation);
		int command;
		int argument;
		
		//Trimming leading 1s off of the input
		if (binString.length() > 8) {
			binString = binString.substring(binString.length()-8, binString.length());
		}
		
		//Splitting operation into command and argument
		if (binString.length() > 7) {
			command = Integer.parseInt(binString.substring(0, 2) ,2);
			argument = Integer.parseInt(binString.substring(2, 8) ,2);
		} else if (binString.length() == 7) {
			command = 1;
			argument = Integer.parseInt(binString.substring(2, 7), 2);
		} else {
			command = 0;
			argument = operation;
		}
		
		//Interprets commands
		//System.out.println("  Command: "+command+"\n  Argument: "+argument); //Debugging
		switch (command) {
			case 0:
				perform(argument);
				break;
			case 1:
				mem.set(argument, stack.pop());
				if (argument == 28) {
					System.out.print((char) mem.get(28));
				}
				break;
			case 2:
				if (argument == 27 || stdincount < stdin.length()) {
					mem.set(27, (byte) stdin.charAt(stdincount));
				} else {
					mem.set(27, (byte) 0);
				}
				
				//Pushes value at the address of the top of the stack
				if (argument == 26) {
					stack.push(mem.get(mem.get(26)));
				} else {
					stack.push(mem.get(argument));
				}
				break;
			case 3:
				if (stack.pop() == stack.pop()) {
					prog.jump(argument);
				}
		}
		
		//copy top of stack to top address
		if (stack.size() > 0) {
			if (stack.peek() != 26) {
				mem.set(26, stack.peek());
			}
		} else {
			mem.set(26, (byte) 0);
		}
		
		//Advance program counter UNLESS we just performed a jump
		if (!prog.didJump) {
			prog.next();
		}
		prog.didJump=false;
		
	}
	
}
