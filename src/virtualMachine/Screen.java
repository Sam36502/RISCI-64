package virtualMachine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;

public class Screen extends Canvas {
	private static JFrame frame;
	private static Canvas canvas;

	public static void init() {
		frame = new JFrame("RISCI-64 Screen");
		canvas = new Screen();
		canvas.setSize(800, 800);
		frame.add(canvas);
		frame.addWindowListener(new ScreenListener());
		frame.pack();
		frame.setVisible(true);
	}

	public static void update() {
		canvas.repaint();
	}

	public void paint(Graphics g) {

		// Show the "screen"
		g.setColor(Color.BLACK);
		g.fillRect(195, 195, 410, 410);

		// Serialize the V-RAM into a string
		String buffer = "";
		for (int i=32; i<64; i++) {
			int before = InputOutput.ram.get(i);
			String buf;
			if (before < 0) {
				buf = Integer.toBinaryString(before);
				buf = buf.substring(buf.length()-8);
			} else {
				buf = Integer.toBinaryString(before+256).substring(1);
			}
			buffer += buf;
		}

		// Display the buffer
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {

				// Translate from an integer to Apple Mac 16-Colour palette
				Color col = null;
				String binStr = buffer.substring((y*32)+(x*4), (y*32)+(x*4)+4);
				int colour = Integer.parseInt(binStr, 2);
				switch (colour) {

				case 0:
					col = Color.WHITE;
					break;

				case 1:
					col = Color.YELLOW;
					break;

				case 2:
					col = getRGB("ff6600");
					break;

				case 3:
					col = getRGB("dd0000");
					break;

				case 4:
					col = getRGB("ff0099");
					break;

				case 5:
					col = getRGB("330099");
					break;

				case 6:
					col = getRGB("0000cc");
					break;

				case 7:
					col = getRGB("0099ff");
					break;

				case 8:
					col = getRGB("00aa00");
					break;

				case 9:
					col = getRGB("006600");
					break;

				case 10:
					col = getRGB("663300");
					break;

				case 11:
					col = getRGB("996633");
					break;

				case 12:
					col = Color.LIGHT_GRAY;
					break;

				case 13:
					col = getRGB("888888");
					break;

				case 14:
					col = Color.DARK_GRAY;
					break;

				case 15:
					col = Color.BLACK;
					break;
				}

				// Set the colour and draw the square
				g.setColor(col);
				g.fillRect(x * 50 + 200, y * 50 + 200, 50, 50);

			}
		}
	}

	private static Color getRGB(String hex) {

		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);

		float[] tmp = new float[3];
		Color.RGBtoHSB(r, g, b, tmp);
		return Color.getHSBColor(tmp[0], tmp[1], tmp[2]);

	}
}
