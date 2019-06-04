package virtualMachine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class InputOutput {
	
	//Current Program Version
	public static final String VERSION = "1.7.2";
	
	//User Settings
	public static int clockspeed = 250;
	public static boolean showProg = false; 
	public static String prompt = " > ";
	
	public static CPU cpu = new CPU();
	public static RAM ram = cpu.mem;
	public static ProgMem prog = cpu.prog;
	public static Scanner input = new Scanner(System.in);
	public static boolean isRunning = true;
	public static boolean screenStarted = false;
	
	//Function for changing the settings
	public static void changeSettings () {
		System.out.print("What setting would you like to set:\n  - clockspeed: "+clockspeed+"ms\n  - show program: "+showProg+"\n  - prompt: \""+prompt+"\"\n"+prompt);
		String sett = input.nextLine().toLowerCase();
		switch(sett) {
			case "clockspeed":
				System.out.print("Enter the new clockspeed (No. of milliseconds between instructions): ");
				clockspeed = Integer.parseInt(input.nextLine());
				break;
			case "show program":
				System.out.print("Would you like to see the binary code as it's being executed? (y/n): ");
				String ans = input.nextLine().toLowerCase();
				if ("y".equals(ans) || "yes".equals(ans)) {
					showProg = true;
				} else {
					showProg = false;
				}
				break;
			case "prompt":
				System.out.print("Enter the new prompt: ");
				prompt = input.nextLine();
				break;
			default:
				System.out.println("Invalid Setting!");
		}
	}
	
	//Loads an 'ass' program into memory
	public static void loadProg (String filename) throws IOException {
		FileInputStream in = new FileInputStream(new File(filename));
		byte[] buff = new byte[128];
		in.read(buff);
		in.close();
		
		//Loading Preset Memory into RAM
		for (int i=0; i<64; i++) {
			String tmp = Integer.toBinaryString(buff[i]);
			if (tmp.length() > 8) {
				tmp = tmp.substring(tmp.length()-8, tmp.length());
			}
			ram.set(i, (byte) Integer.parseInt(tmp, 2));
		}
		
		//Loading Program into Prog mem
		for (int i=64; i<128; i++) {
			String tmp = Integer.toBinaryString(buff[i]);
			if (tmp.length() > 8) {
				tmp = tmp.substring(tmp.length()-8, tmp.length());
			}
			prog.prog[i-64] = (byte) Integer.parseInt(tmp, 2);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(" ______    ___   _______  _______  ___          ___      _   ___ \r\n" + 
				"|    _ |  |   | |       ||       ||   |        |   |    | | |   |\r\n" + 
				"|   | ||  |   | |  _____||       ||   |  ____  |   |___ | |_|   |\r\n" + 
				"|   |_||_ |   | | |_____ |       ||   | |____| |    _  ||       |\r\n" + 
				"|    __  ||   | |_____  ||      _||   |        |   | | ||___    |\r\n" + 
				"|   |  | ||   |  _____| ||     |_ |   |        |   |_| |    |   |\r\n" + 
				"|___|  |_||___| |_______||_______||___|        |_______|    |___|");
		System.out.println("\nWelcome to the RISCI-64 VM\n  by Samuel Pearce\n  Version: "+VERSION);
		
		//Runs the program if a filename is passed as an argument
		if (args.length > 0) {
			
			//Load the file
			String progfile = args[0];
			try {
				loadProg(progfile);
			} catch (IOException e) {
				System.out.println("File not found.");
				System.exit(0);
			}
			
			System.out.print("Would you like to pass anything as input? : ");
			cpu.stdin = input.nextLine();
			
			while (prog.prgcounter < 64) {
				
				//Outputs binary string of current command, if the setting is true
				if (showProg) {
					String binstr = Integer.toBinaryString(prog.get());
					if (binstr.length() > 8) {
						binstr = binstr.substring(binstr.length()-8, binstr.length());
					}
					System.out.println("Current Binary Operation: " + binstr);
				}
				
				//Sends the command from the file to the cpu to be processed
				try {
					cpu.compute(prog.get());
				} catch (Exception e) {
					System.out.println("Exception:\n  "+e+"\n  on line "+prog.prgcounter);
					prog.next();
				}
				
				try {
					Thread.sleep(clockspeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.exit(0);
		}
		
		while (isRunning) {
			System.out.print("\nWould you like to:\n  1 - Load a program.\n  2 - Use VM with shell\n  3 - Change settings\n  4 - Exit program\n"+prompt);
			int opt = 0;
			try {
				opt = Integer.parseInt(input.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Only numbers 1-4 Allowed.");
			}
			switch (opt) {
				case 1:
					System.out.print("Enter the filename of the compiled '.ass' program: ");
					String progfile = input.nextLine();
					try {
						loadProg(progfile);
					} catch (IOException e) {
						System.out.println("File not found.");
						break;
					}
					
					/*//Mem loading function
					System.out.print("Enter the filename of the '.mem' RAM file: ");
					String memfile = input.nextLine();
					try {
						ram.loadState(memfile);
					} catch (IOException e) {
						System.out.println("File not found.");
						break;
					}
					*/
					
					System.out.print("Would you like to pass anything as input? : ");
					cpu.stdin = input.nextLine();
					
					while (prog.prgcounter < 64) {
						
						//Outputs binary string of current command, if the setting is true
						if (showProg) {
							String binstr = Integer.toBinaryString(prog.get());
							if (binstr.length() > 8) {
								binstr = binstr.substring(binstr.length()-8, binstr.length());
							}
							System.out.println("Current Binary Operation: " + binstr);
						}
						
						//Sends the command from the file to the cpu to be processed
						try {
							cpu.compute(prog.get());
						} catch (Exception e) {
							System.out.println("Exception:\n  "+e+"\n  on line "+prog.prgcounter);
							prog.next();
						}
						
						try {
							Thread.sleep(clockspeed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					break;
				case 2:
					Shell.shell();
					break;
				case 3:
					changeSettings();
					break;
				case 4:
					isRunning = false;
					System.out.println("\nBye!");
					break;
				default:
					System.out.println("Invalid option.");
			}
		}
	}

}
