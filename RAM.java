package virtualMachine;

import java.io.*;

public class RAM {
	
	byte[] mem = new byte[64];
	
	public RAM() {
		for (int i=0; i<64; i++) {
			mem[i] = 0;
		}
	}
	
	public byte get(int addr) {
		return mem[addr];
	}
	
	public void set(int addr, byte value) {
		mem[addr] = value;
	}
	
	//Prints RAM out as Chars
	public void debugMem() {
		System.out.println("Displaying memory as chars:\n---------------------------");
		for(int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				System.out.print((char) mem[y*8+x]);
			}
			System.out.println(" - "+y*8);
		}
	}
	
	//Loads RAM from a file
	public void loadState(String filename) throws IOException {
		FileInputStream in = new FileInputStream(new File(filename));
		for (int i=0; i<64; i++) {
			in.read(mem);
			/*
			String bin = Integer.toBinaryString(mem[i]);
			System.out.println(bin.length());
			if (bin.length() > 8) {
				System.out.println(bin);
				bin = bin.substring(bin.length()-8, bin.length());
				System.out.println(bin);
				mem[i] = (byte) Integer.parseInt(bin, 2);
				System.out.println(mem[i]);
			}
			*/
		}
		in.close();
	}
	
	//Saves all RAM into a file
	public void saveState(String filename) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
		writer.write(mem.toString());
		writer.close();
	}
	
}
