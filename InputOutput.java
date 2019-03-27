package virtualMachine;

import java.io.IOException;
import java.util.*;

public class InputOutput {
	
	public static void main(String[] args) {
		RAM ram = new RAM();
		CPU cpu = new CPU();
		Scanner in = new Scanner(System.in);
		int input = 0;
		try {
			ram.loadState("C:\\Users\\Sam\\Desktop\\RISCI-64\\helloworld.ass");
		} catch (IOException e) {
			e.printStackTrace();
		}
		ram.debugMem();
		while (true) {
			System.out.println("Enter Address: ");
			input = Integer.parseInt(in.nextLine());
			System.out.println("Mem at address "+input+": "+Integer.toBinaryString(ram.get(input)));
		}
	}

}
