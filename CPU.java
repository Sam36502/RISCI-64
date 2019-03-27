package virtualMachine;

import java.util.Stack;

public class CPU {
	
	//Main stack & RAM object
	public Stack<Byte> stack = new Stack<Byte>();
	public RAM mem = new RAM();
	public ProgMem prog = new ProgMem();
	
	public void perform (int op) {
		switch (op) {
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
	
	public void compute (byte operation) {
		
		//Splits operation into command and argument
		int command = Integer.parseInt(Integer.toBinaryString(operation).substring(0, 1) ,2);
		int argument = Integer.parseInt(Integer.toBinaryString(operation).substring(2, 7) ,2);
		
		//Interprets commands
		switch (command) {
			case 0:
				perform(argument);
				break;
			case 1:
				mem.set(argument, stack.pop());
				break;
			case 2:
				stack.push(mem.get(argument));
				break;
			case 3:
				prog.jump(argument);
		}
		
		//Advance program counter UNLESS we just performed a jump
		if (command != 3) {
			prog.next();
		}
		
	}
	
}
