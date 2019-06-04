package virtualMachine;

public class ProgMem {
	
	byte[] prog = new byte[64];
	int prgcounter = 0;
	boolean didJump = false;
	
	public ProgMem () {
		for (int i=0; i<64; i++) {
			prog[i] = 0;
		}
	}
	
	public byte get () {
		return prog[prgcounter];
	}
	
	public void next () {
		didJump = false;
		prgcounter++;
	}
	
	public void jump (int addr) {
		didJump = true;
		prgcounter=addr;
	}
	
}
