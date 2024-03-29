/**
 * The SudokuSolver class provides a method to solve a Sudoku puzzle using a backtracking algorithm.
 */
package SudokuSolver;

public class SudokuSolver {
    public static boolean solve(int[][] puzzle) {
        return solve(puzzle, 0, 0);
    }

/**
 * The function solves a Sudoku puzzle using backtracking.
 * 
 * @param puzzle A 2D array representing the Sudoku puzzle. Each element in the array represents a cell
 * in the puzzle, with 0 indicating an empty cell and values 1-9 indicating the filled cells.
 * @param row The current row index in the puzzle grid. It represents the row that is currently being
 * filled or checked.
 * @param col The column index of the current cell in the puzzle grid.
 * @return The solve method returns a boolean value.
 */
    private static boolean solve(int[][] puzzle, int row, int col) {
        if (row == 9) {
            // All rows have been filled, puzzle is solved
            return true;
        }
        if (col == 9) {
            // Move to next row
            return solve(puzzle, row + 1, 0);
        }
        if (puzzle[row][col] != 0) {
            // Cell is already filled, move to next cell
            return solve(puzzle, row, col + 1);
        }
        // Try all possible values for the current cell
        for (int value = 1; value <= 9; value++) {
            if (isValid(puzzle, row, col, value)) {
                puzzle[row][col] = value;
                if (solve(puzzle, row, col + 1)) {
                    return true;
                }
                puzzle[row][col] = 0; // Backtrack
            }
        }
        return false;
    }

    private static boolean isValid(int[][] puzzle, int row, int col, int value) {
        // Check row and column
        for (int i = 0; i < 9; i++) {
            if (puzzle[row][i] == value || puzzle[i][col] == value) {
                return false;
            }
        }
        // Check 3x3 grid
        int gridRow = row / 3 * 3;
        int gridCol = col / 3 * 3;
        for (int i = gridRow; i < gridRow + 3; i++) {
            for (int j = gridCol; j < gridCol + 3; j++) {
                if (puzzle[i][j] == value) {
                    return false;
                }
            }
        }
        return true;
    }
}