package SudokuSolver;

import java.util.Random;

public class SudokuGenerator {
    private static final int[][] easyTemplate = {
            {0, 0, 0, 2, 6, 0, 7, 0, 1},
            {6, 8, 0, 0, 7, 0, 0, 9, 0},
            {1, 9, 0, 0, 0, 4, 5, 0, 0},
            {8, 2, 0, 1, 0, 0, 0, 4, 0},
            {0, 0, 4, 6, 0, 2, 9, 0, 0},
            {0, 5, 0, 0, 0, 3, 0, 2, 8},
            {0, 0, 9, 3, 0, 0, 0, 7, 4},
            {0, 4, 0, 0, 5, 0, 0, 3, 6},
            {7, 0, 3, 0, 1, 8, 0, 0, 0}
    };

    private static final int[][] mediumTemplate = {
            {0, 0, 0, 0, 0, 0, 6, 8, 0},
            {0, 0, 0, 0, 7, 3, 0, 0, 9},
            {3, 0, 9, 0, 0, 0, 0, 4, 5},
            {4, 9, 0, 0, 0, 0, 0, 0, 0},
            {8, 0, 3, 0, 5, 0, 9, 0, 2},
            {0, 0, 0, 0, 0, 0, 0, 3, 6},
            {9, 6, 0, 0, 0, 0, 3, 0, 8},
            {7, 0, 0, 6, 8, 0, 0, 0, 0},
            {0, 2, 8, 0, 0, 0, 0, 0, 0}
    };

    private static final int[][] hardTemplate = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 3, 0, 8, 5},
            {0, 0, 1, 0, 2, 0, 0, 0, 0},
            {0, 0, 0, 5, 0, 7, 0, 0, 0},
            {0, 0, 4, 0, 0, 0, 1, 0, 0},
            {0, 9, 0, 0, 0, 0, 0, 0, 0},
            {5, 0, 0, 0, 0, 0, 0, 7, 3},
            {0, 0, 2, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 0, 4, 0, 0, 0, 9}
    };

    private static final int[][][] templates = {easyTemplate, mediumTemplate, hardTemplate};

    public static int[][] generate(int difficulty) {
        int[][] template = templates[difficulty];
        int[][] puzzle = new int[9][9];
        Random random = new Random();
    
        // Copy template to puzzle
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                puzzle[i][j] = template[i][j];
            }
        }
        // Remove cells from puzzle
        int cellsToRemove = 0;
        switch (difficulty) {
            case 0:
                cellsToRemove = 40;
                break;
            case 1:
                cellsToRemove = 50;
                break;
            case 2:
                cellsToRemove = 60;
                break;
        }
        boolean[][] tried = new boolean[9][9]; // Keep track of which values have been tried
        while(cellsToRemove > 0)
        {
            int i = random.nextInt(9);
            int j = random.nextInt(9);
            if (!tried[i][j] && puzzle[i][j]!=0) // Check if value has not been tried and is not already 0
            {
                puzzle[i][j] = 0;
                cellsToRemove--;
            }
            tried[i][j] = true; // Mark value as tried
        }
        return puzzle;
    }
       
}
