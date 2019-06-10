package virtualMachine;

import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFrame;

public class Shell {
	
	public static Scanner input = new Scanner(System.in);
	public static CPU cpu = InputOutput.cpu;
	public static Stack<Byte> stack = cpu.stack;
	public static RAM ram = cpu.mem;
	public static String stdInput = "";
	public static String stdOutput = "";
	public static int inCounter = 0;
	
	//returns the integer address of 
	public static int getNamedAddr (String name) {
		switch (name) {
		case "top":
			return 26;
		case "stdin":
			return 27;
		case "stdout":
			return 28;
		case "stderr":
			return 29;
		case "sndout":
			return 31;
		case "screen":
			return 32;
		default:
			System.out.println("Invalid address name.");
			return 0;
		}
	}
	
	
	//Shell prompt onto the computer
	public static void shell () {
		System.out.println("RISCI Shell started.\nUse 'exit' to quit shell.\nUse 'help' for a list of commands");
		String command[] = new String[3];
		while (!"exit".equals(command[0])) {
			System.out.print(InputOutput.prompt);
			command = input.nextLine().toLowerCase().split(" ");
			switch(command[0]) {
				case "help": //Help menu
					if (command.length < 2) {
						System.out.println("All RISCI Shell commands: (case insensitive)\n--------------------------------");
						System.out.println("  exit                  - Quits the shell and returns to the Main VM menu.");
						System.out.println("  help                  - Shows a list of all available commands.");
						System.out.println("  insert <val> <addr>   - Insert a value into RAM.");
						System.out.println("  showstack             - Show the contents of the stack.");
						System.out.println("  showram               - Show the contents of RAM (output in hex).");
						System.out.println("  input                 - Prompts the user for input to store into stdInput.");
						System.out.println("  print                 - Outputs the content of stdOutput.");
						System.out.println("  flushin               - Flushes standard input.");
						System.out.println("  flushout              - Flushes standard output.");
						System.out.println("  screen                - Displays the screen and updates it.");
						System.out.println("All RISCI CPU commands:\n--------------------------------");
						System.out.println("  pushfrom <addr>       - Pushes a value from RAM onto the Stack");
						System.out.println("  popto <addr>          - Pops top value off the Stack and moves the to RAM");
						System.out.println("  perform <operation>   - Pops top values off the stack and performs an operation, then pushes result onto stack");
						System.out.println("  jumpif (<addr>)       - Pops top two values off the stack and compares them. (doesn't jump in the shell)");
					} else {
						// If the user passes an argument to help, provide a more detailed help page for certain commands
						switch (command[1]) {
							case "perform":
								System.out.println("Perform <operation>:\n----------------------\n Will take the top two values off of the stack and perform");
								System.out.println(" an operation on them. Afterwards, it will push the result onto the stack.");
								System.out.println("\nOperations:");
								System.out.println(" - addition\n - subtraction\n - multiplication\n - division\n - modulo\n - increment\n - decrement");
								break;
							case "insert":
								System.out.println("Insert <value> <address>:\n"
										+ "-------------------------\n"
										+ " Takes the provided value and pushes it into an address in RAM.");
								break;
							case "showstack":
								System.out.println("Showstack:\n"
										+ "-------------------------\n"
										+ " Prints out the contents of the stack from top to bottom\n"
										+ " format: 'stackpos: value'");
								break;
							case "showram":
								System.out.println("Showram:\n"
										+ "-------------------------\n"
										+ " Prints out the contents of RAM as ASCII characters. In groups of 8\n"
										+ " format: 'DataHere - start of addresses'");
								break;
							case "input":
								System.out.println("Input:\n"
										+ "-------------------------\n"
										+ " Prompts the user for an input string."
										+ " Moves the chars in the string, once per tick, into stdin (RAM 27)\n"
										+ " If all chars in the input string have been read, then the last\n"
										+ " char will stay in stdin until cleared.");
								break;
							case "print":
								System.out.println("Print:\n"
										+ "-------------------------\n"
										+ " Takes all characters sent to stdout (RAM 28) and prints them out.\n"
										+ " E.g. insert 72 stdout -> print -> 'H' is printed in console");
								break;
							case "screen":
								System.out.println("Screen:\n"
										+ "-------------------------\n"
										+ " If screen has not yet been called, it will open a new window.\n"
										+ " with the screen of the RISCI-64 displayed on it. The screen's\n"
										+ " output is based on the memory from 32-64 (see documentation).\n"
										+ " If the window is already open, then 'screen' will simply \n"
										+ " refresh the window, displaying any changes that have occured."
										+ " E.g. insert 6 35 -> screen -> Blue pixel is displayed in the\n"
										+ " top right corner of the screen.");
								break;
							default:
								System.out.println("There is no help page for that.");
						}
					}
					break;
				case "insert":
					if (command.length != 3) {
						System.out.println("Invalid number of arguments. Try 'help'.");
					} else {
						int addr = 0;
						int value = 0;
						
						//Try parsing the address
						try {
							addr = Integer.parseInt(command[2]);
						} catch (NumberFormatException e) {
							addr = getNamedAddr(command[2]);
						}
						
						//Try parsing the Value
						try {
							value = Integer.parseInt(command[1]);
						} catch (NumberFormatException e) {
							System.out.println("Value out of range (0-255)");
							break;
						}
						
						if (addr > 63 || addr < 0) {
							System.out.println("Address out of range (0-63)");
							break;
						}
						ram.set(addr,(byte) value);
					}
					break;
				case "showstack": //List out the stack from top to bottom
					Byte[] temp = new Byte[stack.size()];
					int size = stack.size();
					for (int i=0; i<size; i++) {
						temp[size-i-1] = stack.pop();
						System.out.println(i+": "+temp[size-i-1]);
					}
					for (byte curr : temp) stack.push(curr);
					break;
				case "showram":
					ram.debugMem();
					break;
				case "pushfrom":
					if (command.length != 2) {
						System.out.println("Invalid number of arguments. Try 'help'.");
					} else {
						int addr = 0;
						try {
							addr = Integer.parseInt(command[1]);
							
							//If the input to pushfrom is not a number
						} catch (NumberFormatException e) {
							addr = getNamedAddr(command[1]);
						}
						
						if (addr > 63 || addr < 0) {
							System.out.println("Address out of range (0-63)");
						}
						stack.push(ram.get(addr));
					}
					break;
				case "popto":
					if (command.length != 2) {
						System.out.println("Invalid number of arguments. Try 'help'.");
					} else {
						int addr = 0;
						try {
							addr = Integer.parseInt(command[1]);
						} catch (NumberFormatException e) {
							addr = getNamedAddr(command[1]);
						}
						if (addr > 63 || addr < 0) {
							System.out.println("Address out of range (0-63)");
						}
						if (stack.size() > 0) {
							ram.set(addr, stack.pop());
						} else {
							System.out.println("Stack is empty.");
						}
					}
					break;
				case "perform":
					if (command.length != 2) {
						System.out.println("Invalid number of arguments. Try 'help'.");
					} else {
						int b = 0;
						int a = 0;
						int stackSize = stack.size();
						if (stackSize > 0) {
							b = stack.pop();
						} else {
							System.out.println("Stack is empty.");
						}
						if (stackSize > 1) {
							a = stack.pop();
						}
						switch (command[1]) {
							case "addition":
								stack.push( (byte)(a + b));
								break;
							case "subtraction":
								stack.push( (byte)(a - b));
								break;
							case "multiplication":
								stack.push( (byte)(a * b));
								break;
							case "division":
								stack.push( (byte)(a / b));
								break;
							case "modulo":
								stack.push( (byte)(a % b));
								break;
							case "increment":
								if (stackSize > 1) stack.push((byte) a);
								stack.push( (byte)(b + 1));
								break;
							case "decrement":
								if (stackSize > 1) stack.push((byte) a);
								stack.push( (byte)(b - 1));
								break;
						}
					}
					break;
				case "jumpif":
					if (command.length != 1) {
						System.out.println("Invalid number of arguments. (You can't jump in a shell!)");
					}
					if (stack.size() > 1) {
						if (stack.pop() == stack.pop()) {
							System.out.println("Top two values are equal.");
						} else {
							System.out.println("Top to values are NOT equal.");
						}
					} else {
						System.out.println("Not enough values on the Stack to compare.");
					}
					break;
				case "input":
					System.out.print("Input string: ");
					stdInput += input.nextLine();
					break;
				case "print":
					System.out.println("Output so far: "+stdOutput);
					break;
				case "flushin":
					stdInput = "";
					inCounter = 0;
					break;
				case "flushout":
					stdOutput = "";
					break;
				case "exit":
					break;
				case "screen":
					if (!InputOutput.screenStarted) {
						Screen.init();
						InputOutput.screenStarted = true;
					}
					Screen.update();
					break;
				default:
					System.out.println("Command not recognised. Try 'help'.");
			}
			
			//putting stdout into the output buffer
			if (ram.get(28) != 0) {
				stdOutput += "" + (char) ram.get(28);
				ram.set(28, (byte) 0);
			}
			
			//putting input buffer chars into RAM
			if (stdInput != "") {
				ram.set(27, (byte) stdInput.charAt(inCounter));
				if (inCounter < stdInput.length() - 1) {
					inCounter++;
				}
			}
		}
	}
}
