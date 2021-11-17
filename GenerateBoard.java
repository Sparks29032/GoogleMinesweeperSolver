import java.awt.*;
import java.awt.image.*;

public class GenerateBoard {
	int w = 0, h = 0;
	
	//light and dark green colors on board respectively
	int[] c1 = new int[] {170, 215, 81};
	int[] c2 = new int[] {162, 209, 73};
	
	//find where the board is located on the screen
	public int[][][] findBoardCoords(int a, int b) throws AWTException {
		int[][][] board = new int[a][b][2];
		ReadScreen rs = new ReadScreen();
		int width = rs.screenSize.width;
		int height = rs.screenSize.height;
		BufferedImage capture = rs.capture();
		
		int w = 0, h = -1;
		
		int counter = 0;
		int[] colors;
		boolean flag;
		do {
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c1[i] != colors[i]) {
					flag = true;
				}
			}
			counter++;
		} while(flag && height > counter / width);
		
		board[0][0][0] = counter / width;
		board[0][0][1] = counter % width;
		
		do {
			counter++;
			w++;
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c2[i] != colors[i]) {
					flag = true;
				}
			}
		} while(flag && height > counter / width);
		
		this.w = w;
		
		do {
			counter += width;
			h++;
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c1[i] != colors[i]) {
					flag = true;
				}
			}
		} while(flag && height > counter / width);
		
		this.h = h;
		
		if (height < counter / width) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		for (int i = 0; i < a; i++) {
			for (int j = 0; j < b; j++) {
				board[i][j][0] = board[0][0][0] + h * i;
				board[i][j][1] = board[0][0][1] + w * j;
			}
		}
		return board;
	}
	
	//print the board (for dev purposes)
	public void printBoard(int[][][] board) {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				System.out.print("(" + board[i][j][0] + ", " + board[i][j][1] + ")\t");
			}
			System.out.println();
		}
	}
	
	//to determine what colors each number is (for dev purposes)
	public void printColors(int[][][] board) throws AWTException {
		ReadScreen rs = new ReadScreen();
		BufferedImage capture = rs.capture();
		int[] colors;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				colors = rs.getColors(board[i][j][0] + h/2, board[i][j][1] + w/2, capture);
				System.out.print("(" + colors[0] + "," + colors[1] + "," + colors[2] + ")\t");
			}
			System.out.println();
		}
	}
	
	//get vals on board
	public char[][] getNums(int[][][] board, int a, int b) throws AWTException {
		ReadScreen rs = new ReadScreen();
		char[][] nums = new char[a][b];
		BufferedImage capture = rs.capture();
		int[] colors;
		int color;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				colors = rs.getColors(board[i][j][0] + h/2, board[i][j][1] + w/2, capture);
				color = findValue(colors);
				nums[i][j] = (char) color;
			}
		}
		
		return nums;
	}
	
	//find what number is at a certain position given the color
	public int findValue(int[] color) {
		int sens = 30;
		int[] b1 = new int[]{228, 194, 159};
		int[] b2 = new int[]{215, 184, 153};
		int[] blue = new int[]{35, 122, 207};
		int[] green = new int[]{87, 149, 76};
		int[] red = new int[]{211, 52, 51};
		int[] purple = new int[]{166, 103, 157};
		int[] orange = new int[]{235, 153, 53};
		int[] teal = new int[]{96, 171, 167};
		boolean flag;
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if ((b1[i] - sens > color[i] || b1[i] + sens < color[i]) && (b2[i] - sens > color[i] || b2[i] + sens < color[i])) {
				flag = false;
			}
		}
		
		if (flag) {
			return 45;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (blue[i] - sens > color[i] || blue[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 49;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (green[i] - sens > color[i] || green[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 50;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (red[i] - sens > color[i] || red[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 51;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (purple[i] - sens > color[i] || purple[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 52;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (orange[i] - sens > color[i] || orange[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 53;
		}
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if (teal[i] - sens > color[i] || teal[i] + sens < color[i]) {
				flag = false;
			}
		}
		
		if (flag) {
			return 54;
		}
		
		return 35;
	}
}
