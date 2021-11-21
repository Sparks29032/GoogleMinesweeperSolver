import java.awt.*;
import java.awt.image.*;

public class GenerateBoard {
	//pixel-fix (width then height)
	int[] pixelFix = new int[] {1, 1};
	
	//width and height of each square
	int w = 0, h = 0;
	
	//light and dark green colors on board respectively
	int[] c1 = new int[] {170, 215, 81};
	int[] c2 = new int[] {162, 209, 73};
	
	//title card color
	int[] t1 = new int[] {84, 111, 68};
	int[] t2 = new int[] {76, 111, 71};
	
	//board colors when clicked
	int[] b1 = new int[]{228, 194, 159};
	int[] b2 = new int[]{215, 184, 153};
	
	//numbers from 1-6
	int[] blue = new int[]{35, 122, 207};
	int[] green = new int[]{87, 149, 76};
	int[] red = new int[]{211, 52, 51};
	int[] purple = new int[]{166, 103, 157};
	int[] orange = new int[]{235, 153, 53};
	int[] teal = new int[]{96, 171, 167};
	
	//find where the board is located on the screen
	public int[][][] findBoardCoords(int a, int b) throws AWTException {
		int[][][] board = new int[a][b][2];
		ReadScreen rs = new ReadScreen();
		int width = rs.screenSize.width;
		int height = rs.screenSize.height;
		BufferedImage capture = rs.capture();
				
		int counter = height * width - 1;
		int[] colors;
		boolean flag;
		
		int w = 0, h = -1;
		
		/*
		do {
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c1[i] != colors[i]) {
					flag = true;
					break;
				}
			}
			counter--;
		} while(flag && counter > 0);
		
		flag = true;
		
		while(flag && counter > 0) {
			counter--;
			w++;
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c2[i] != colors[i]) {
					flag = true;
					break;
				}
			}
		}
		
		this.w = w;
		
		flag = true;
		
		while (flag && counter > 0) {
			counter -= width;
			h++;
			flag = false;
			colors = rs.getColors(counter / width, counter % width, capture);
			for (int i = 0; i < 3; i++) {
				if (c1[i] != colors[i]) {
					flag = true;
					break;
				}
			}
		}
	
		this.h = h;
		
		System.out.println(h + " " + w);
		
		if (counter < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		*/
		
		/*
		
		counter = 0;
		do {
			flag = false;
			colors = rs.getColors(counter % height, counter / height, capture);
			for (int i = 0; i < 3; i++) {
				if (t1[i] - 10 > colors[i] || t1[i] + 10 < colors[i]) {
					flag = true;
				}
			}
			counter++;
		} while(flag && width > counter / height);
		int border1 = counter / height;
		
		counter = 0;
		do {
			flag = false;
			colors = rs.getColors(counter % height, width - counter / height - 1, capture);
			for (int i = 0; i < 3; i++) {
				if (t1[i] - 10 > colors[i] || t1[i] + 10 < colors[i]) {
					flag = true;
				}
			}
			counter++;
		} while(flag && width > counter / height);
		int border2 = width - counter / height - 1;
		
		System.out.println(border1 + " " + border2);
		
		w = (border2 - border1 + 1) / b;
		h = w;
		
		System.out.println(h + " " + w);
		
		*/
		
		counter = 0;
		do {
			flag = false;
			colors = rs.getColors(height - counter / width - 1, width - counter % width - 1, capture);
			for (int i = 0; i < 3; i++) {
				if (c1[i] != colors[i]) {
					flag = true;
				}
			}
			counter++;
		} while(flag && height > counter / width);
		
		int[] endCorner = new int[2];
		endCorner[0] = height - counter / width - 1;
		endCorner[1] = width - counter % width - 1;
		
		counter = 0;
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

		w = (endCorner[0] - board[0][0][0]) / a + pixelFix[0];
		h = (endCorner[1] - board[0][0][1]) / b + pixelFix[1];
		
		this.w = w;
		this.h = h;
		
		System.out.println(board[0][0][0] + " " + board[0][0][1]);
		
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
		int counter;
		double sens = 0.7;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				color = -1;
				counter = - (int) (h/2 * sens);
				/*while (color == -1) {
					colors = rs.getColors(board[i][j][0] + h/2 + counter, board[i][j][1] + w/2, capture);
					color = findValue(colors);
					counter++;
					if (counter > h/2 * sens) {
						break;
					}
				}*/
				while (color == -1 || color == 45) {
					colors = rs.getColors(board[i][j][0] + h/2 + counter, board[i][j][1] + w/2 + counter, capture);
					color = findValue(colors);
					counter++;
					if (counter > h/2 * sens) {
						break;
					}
				}
				nums[i][j] = (char) color;
			}
		}
		
		return nums;
	}
	
	//find what number is at a certain position given the color
	public int findValue(int[] color) {
		int sens = 20;
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
		
		flag = true;
		for (int i = 0; i < 3; i++) {
			if ((c1[i] - sens * 0.25 > color[i] || c1[i] + sens * 0.25 < color[i]) && (c2[i] - sens * 0.25 > color[i] || c2[i] + sens * 0.25 < color[i])) {
				flag = false;
			}
		}
		
		if (flag) {
			return 35;
		}
		
		return -1;
	}
}
