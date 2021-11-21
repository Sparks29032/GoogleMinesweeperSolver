import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PlayGame {
	Robot robot;
	GenerateBoard gs;
	int[][][] boardCoords;
	double[][] board;
	int[][] elimBoard;
	int[][] mines;
	double[][] guess;
	
	//size of the board
	//easy:		08 x 10
	//medium:	14 x 18
	//hard:		20 x 24
	int a = 14;
	int b = 18;
	
	ArrayList<Integer> variables;
	ArrayList<Integer> isMine;
	ArrayList<Integer> toClick;
	
	public PlayGame() throws AWTException {
		gs = new GenerateBoard();
		boardCoords = gs.findBoardCoords(a, b);
		mines = new int[a][b];
		board = new double[a][b];
	}
	
	public void clickSquare(int y, int x) throws AWTException {
		robot = new Robot();
		robot.mouseMove(x, y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		//robot.mouseMove(0, 0);
	}
	
	/*
	public void ruleBasedAlgorithm() throws AWTException, InterruptedException {
		clickSquare(boardCoords[a/2][b/2][0], boardCoords[a/2][b/2][1]);
		int max = 100;
		int counter = 0;
		while (counter < max) {
			Thread.sleep(1000);
			char[][] nums = gs.getNums(boardCoords, a, b);
			for (int i = 0; i < nums.length; i++) {
				for (int j = 0; j < nums[i].length; j++) {
					if (Character.isDigit(nums[i][j])) {
						decFromNearby(i, j, nums);
					}
				}
			}
			
			//solving time
			do {
				for (int i = 0; i < nums.length; i++) {
					for (int j = 0; j < nums[i].length; j++) {
						if (Character.isDigit(nums[i][j])) {
							if (nums[i][j] == '0') {
								nums[i][j] = '-';
								setClearsNearby(i, j, nums);
							}
							else if (numUnknownNearby(i, j, nums) == (int) nums[i][j] - 48) {
								nums[i][j] = '-';
								setMinesNearby(i, j, nums);
							}
						}
					}
				}
			} while (containsUncleared(nums));
			
			for (int i = 0; i < a; i++) {
				for (int j = 0; j < b; j++) {
					if (mines[i][j] == 1) {
						clickSquare(boardCoords[i][j][0] + gs.h/2, boardCoords[i][j][1] + gs.w/2);
						mines[i][j] = 0;
					}
				}
			}

			printVals(nums);
			counter++;
		}
	}
	*/

	//start algorithm
	public void eliminationAlgorithm() throws AWTException, InterruptedException {
		//click middle square
		clickSquare(boardCoords[a/2][b/2][0], boardCoords[a/2][b/2][1]);
		
		//only runs gaussean a few times
		int max = 100;
		int counter = 0;
		
		//to store state of board
		char[][] nums;
		char[][] prevNums = new char[a][b];
		
		//while we still want to run the alg
		while (counter < max) {
			//sleep then get the board
			Thread.sleep(1000);
			nums = gs.getNums(boardCoords, a, b);
			printVals(nums);
			for (int i = 0; i < nums.length; i++) {
				for (int j = 0; j < nums[i].length; j++) {
					if (Character.isDigit(nums[i][j])) {
						decFromNearby(i, j, nums);
					}
				}
			}
			
			//while the board state is changing
			while(!equalBoardState(nums, prevNums)) {
				//set prevNums to nums
				prevNums = setEqual(nums, prevNums);
				
				//if a number has the same num of unknowns by it as mines, they're all mines
				for (int i = 0; i < nums.length; i++) {
					for (int j = 0; j < nums[i].length; j++) {
						if (Character.isDigit(nums[i][j])) {
							if (numUnknownNearby(i, j, nums) == (int) nums[i][j] - 48) {
								nums[i][j] = '-';
								setMinesNearby(i, j, nums);
							}
							
						}
					}
				}
				
				//if a number is 0, there are no more mines next to it
				for (int i = 0; i < nums.length; i++) {
					for (int j = 0; j < nums[i].length; j++) {
						if (Character.isDigit(nums[i][j])) {
							if (nums[i][j] == '0') {
								nums[i][j] = '-';
								setClearsNearby(i, j, nums);
							}
							
						}
					}
				}
				
				//click all non-mines
				for (int i = 0; i < a; i++) {
					for (int j = 0; j < b; j++) {
						if (mines[i][j] == 1) {
							clickSquare(boardCoords[i][j][0] + gs.h/2, boardCoords[i][j][1] + gs.w/2);
							mines[i][j] = 0;
						}
					}
				}
				
				//get the new board and reduce it by the mines
				Thread.sleep(1000);
				nums = gs.getNums(boardCoords, a, b);
				printVals(nums);
				for (int i = 0; i < nums.length; i++) {
					for (int j = 0; j < nums[i].length; j++) {
						if (Character.isDigit(nums[i][j])) {
							decFromNearby(i, j, nums);
						}
					}
				}
			}
			
			//counts number of gausseans used
			++counter;
			
			//create a system of equations
			board = createSystem(nums);
			Gaussean g = new Gaussean();
			elimBoard = g.elim(board);
			
			isMine = new ArrayList<Integer>();
			toClick = new ArrayList<Integer>();
			int res;
			//fix
			for (int i = 0; i < elimBoard.length; i++) {
				res = elimBoard[i][variables.size()];
				if (res == 0) {
					if (isNonNeg(elimBoard[i])) {
						for (int j = 0; j < elimBoard[i].length - 1; j++) {
							if (elimBoard[i][j] != 0) {
								toClick.add(variables.get(j));
							}
						}
					}
				}
				
				else if (res > 0) {
					if (sumPos(elimBoard[i]) == res) {
						for (int j = 0; j < elimBoard[i].length - 1; j++) {
							if (elimBoard[i][j] > 0) {
								isMine.add(variables.get(j));
							}
							if (elimBoard[i][j] < 0) {
								toClick.add(variables.get(j));
							}
						}
					}
				}
				
				else if (res < 0) {
					if (sumNeg(elimBoard[i]) == res) {
						for (int j = 0; j < elimBoard[i].length - 1; j++) {
							if (elimBoard[i][j] < 0) {
								isMine.add(variables.get(j));
							}
							if (elimBoard[i][j] > 0) {
								toClick.add(variables.get(j));
							}
						}
					}
				}
			}
			
			for (int i : isMine) {
				mines[i / this.b][i % this.b] = 9;
			}
			
			for (int i : toClick) {
				clickSquare(boardCoords[i / this.b][i % this.b][0] + gs.h/2, boardCoords[i / this.b][i % this.b][1] + gs.w/2);
			}
		}
	}

	public void printVals(char[][] nums) {
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if (mines[i][j] == 9) {
					System.out.print("x ");
				}
				else if (mines[i][j] == 1) {
					System.out.print(". ");
				}
				else {
					System.out.print(nums[i][j] + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public int numUnknownNearby(int a, int b, char[][] nums) {
		int count = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (nums[a + i][b + j] == '#' && mines[a + i][b + j] != 9) {
						count++;
					}
				}
			}
		}
		
		return count;
	}
	
	public void setMinesNearby(int a, int b, char[][] nums) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (nums[a + i][b + j] == '#') {
						if (mines[a + i][b + j] != 9) {
							mines[a + i][b + j] = 9;
							decNumsNearby(a + i, b + j, nums);
						}
					}
				}
			}
		}
	}

	public void setClearsNearby(int a, int b, char[][] nums) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (nums[a + i][b + j] == '#') {
						if (mines[a + i][b + j] != 9) {
							mines[a + i][b + j] = 1;
						}
					}
				}
			}
		}
	}
	
	public void decNumsNearby(int a, int b, char[][] nums) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (Character.isDigit(nums[a + i][b + j])) {
						nums[a + i][b + j] = (char) ((int) nums[a + i][b + j] - 1);
					}
				}
			}
		}
	}
	
	public void decFromNearby(int a, int b, char[][] nums) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < mines.length && 0 <= b + j && b + j < mines[a].length) {
					if (mines[a + i][b + j] == 9) {
						nums[a][b] = (char) ((int) nums[a][b] - 1);
					}
				}
			}
		}
	}
	
	public boolean equalBoardState(char[][] b1, char[][] b2) {
		if (b1.length != b2.length) {
			return false;
		}
		for (int i = 0; i < b1.length; i++) {
			if (b1[i].length != b2[i].length) {
				return false;
			}
			for (int j = 0; j < b1[i].length; j++) {
				if (b1[i][j] != b2[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean containsUncleared(char[][] nums) {
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if (nums[i][j] == '0') {
					return true;
				}
			}
		}
		return false;
	}
	
	/*
	public void setGuesses(int a, int b, char[][] nums) {
		double counter = 0;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < mines.length && 0 <= b + j && b + j < mines[a].length) {
					if (nums[a + i][b + j] == '#' && mines[a + i][b + j] != 9) {
						counter++;
					}
				}
			}
		}
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < mines.length && 0 <= b + j && b + j < mines[a].length) {
					if (nums[a + i][b + j] == '#' && mines[a + i][b + j] != 9) {
						guess[a + i][b + j] += ((int) nums[a][b] - 48) / counter;
					}
				}
			}
		}
	}
	*/
	
	public char[][] setEqual(char[][] b1, char[][] b2) {
		int row = b1.length;
		int col = b1[0].length;
		b2 = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				b2[i][j] = b1[i][j];
			}
		}
		return b2;
	}
	
	public void getVariables(char[][] nums) {
		variables = new ArrayList<Integer>();
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if(nums[i][j] == '#' && mines[i][j] != 9) {
					if (isVariable(nums, i, j)) {
						variables.add(i * this.b + j);
					}
				}
			}
		}
	}
	
	public double[][] createSystem(char[][] nums) {
		getVariables(nums);
		int counter = 0;
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if (Character.isDigit(nums[i][j])) {
					if(isResult(nums, i, j)) {
						counter++;
					}
				}
			}
		}
		double[][] system = new double[counter][variables.size() + 1];
		
		counter = 0;
		
		for (int i = 0; i < nums.length; i++) {
			for (int j = 0; j < nums[i].length; j++) {
				if (Character.isDigit(nums[i][j])) {
					if(isResult(nums, i, j)) {
						system[counter++] = getEquation(nums, i, j, ((int) nums[i][j]) - 48);
					}
				}
			}
		}
		
		return system;
	}
	
	public boolean isVariable(char[][] nums, int a, int b) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (Character.isDigit(nums[a + i][b + j])) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public double[] getEquation(char[][] nums, int a, int b, int res) {
		double[] eq = new double[variables.size() + 1];
		eq[variables.size()] = res;
		int temp;
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					temp = variables.indexOf((a + i) * this.b + (b + j));
					if (temp != -1) {
						eq[temp] = 1;
					}
				}
			}
		}
		
		return eq;
	}
	
	public boolean isResult(char[][] nums, int a, int b) {
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (0 <= a + i && a + i < nums.length && 0 <= b + j && b + j < nums[a].length) {
					if (variables.indexOf((a + i) * this.b + (b + j)) != -1) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean isNonNeg(int[] row) {
		for (int i = 0; i < row.length - 1; i++) {
			if (row[i] < 0) {
				return false;
			}
		}
		
		return true;
	}
	
	public int sumPos(int[] row) {
		int sum = 0;
		for (int i = 0; i < row.length - 1; i++) {
			if (row[i] > 0) {
				sum += row[i];
			}
		}
		
		return sum;
	}
	
	public int sumNeg(int[] row) {
		int sum = 0;
		for (int i = 0; i < row.length - 1; i++) {
			if (row[i] < 0) {
				sum += row[i];
			}
		}
		
		return sum;
	}
	
	public static void main(String[] args) throws AWTException, InterruptedException {
		Thread.sleep(3000);
		PlayGame pg = new PlayGame();
		pg.eliminationAlgorithm();
	}
}
