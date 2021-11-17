
public class Gaussean {
	public int[][] elim(double[][] matrix) {
		boolean flag;
		if (matrix.length == 0) {
			return new int[0][0];
		}
		int[][] eliminated = new int[matrix.length][matrix[0].length];
		for (int counter = 0; counter < matrix[0].length - 1; counter++) {
			for (int i = 0; i < matrix.length; i++) {
				flag = true;
				for (int j = 0; j < counter; j++) {
					if (matrix[i][j] != 0) {
						flag = false;
					}
				}
				if (flag && matrix[i][counter] != 0) {
					multRow(matrix, i, counter);
					reduce(matrix, i, counter);
				}
			}
		}
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				eliminated[i][j] = (int) matrix[i][j];
			}
		}
		
		return eliminated;
	}
	
	public void multRow(double[][] matrix, int a, int b) {
		double temp = matrix[a][b];
		for (int j = 0; j < matrix[a].length; j++) {
			matrix[a][j] /= temp;
		}
	}
	
	public void reduce(double[][] matrix, int a, int b) {
		for (int i = 0; i < a; i++) {
			for (int j = b + 1; j < matrix[i].length; j++) {
				matrix[i][j] -= matrix[i][b] * matrix[a][j];
			}
			matrix[i][b] -= matrix[i][b] * matrix[a][b];
		}
		for (int i = a + 1; i < matrix.length; i++) {
			for (int j = b + 1; j < matrix[i].length; j++) {
				matrix[i][j] -= matrix[i][b] * matrix[a][j];
			}
			matrix[i][b] -= matrix[i][b] * matrix[a][b];
		}
	}
	
	public void printMatrix(int[][] matrix) {
		for (int[] row : matrix) {
			for (int val : row) {
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Gaussean g = new Gaussean();
		double[][] matrix = {{5, 3, 2, 3}, {4, 2, 7, 4}, {5, 3, 0, 7}};
		int[][] eliminated = g.elim(matrix);
		g.printMatrix(eliminated);
	}
}
