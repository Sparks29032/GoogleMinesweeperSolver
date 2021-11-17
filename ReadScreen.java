import java.awt.*;
import java.awt.image.*;

public class ReadScreen {
	Robot robot;
	Dimension screenSize;
	Rectangle screen;
	
	//make a rectangle that is the screen once it is called
	public ReadScreen() throws AWTException {
		robot = new Robot();
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screen = new Rectangle(screenSize);
	}
	
	//get a screen capture
	public BufferedImage capture(){
		return robot.createScreenCapture(screen);
	}
	
	//read colors from the screen capture
	public int[] getColors(int y, int x, BufferedImage capture) {
		int RGBnum;
		int[] arr;
		RGBnum = capture.getRGB(x, y);
		arr = convertRGB(RGBnum);
		return arr;
	}
	
	//convert the rgb number into three separate color values: red, green, blue
	public int[] convertRGB(int RGBnum) {
		int[] RGB = new int[3];
		RGB[2] = RGBnum % 256 + 256;
		RGBnum /= 256;
		RGB[1] = RGBnum % 256 + 255;
		RGBnum /= 256;
		RGB[0] = RGBnum % 256 + 255;
		return RGB;
	}
}
