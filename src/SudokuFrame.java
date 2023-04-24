import SudokuSolve.SudokuSolve;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuFrame extends JFrame {
    private JTextField[][] Board;

    SudokuFrame() {
        setTitle("SudokuSolver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel BoardPanel = new JPanel(new GridLayout(9, 9, 2, 2));
        Board = new JTextField[9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(50, 50));
                textField.setHorizontalAlignment(JTextField.CENTER);
                Board[i][j] = textField;
                BoardPanel.add(textField);

                // Add borders around each 3x3 grid
                if (i % 3 == 2 && j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 2, Color.BLACK));
                } else if (i % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 0, Color.BLACK));
                } else if (j % 3 == 2) {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 2, Color.BLACK));
                } else {
                    textField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 0, Color.BLACK));
                }
              
            }
        }
        // Buttons Solve and Reset
        JPanel ButtonPanel = new JPanel();
        JButton resetButton = new JButton("Reset");
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        ButtonPanel.add(solveButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // resetSudoku();
            }
        });
        ButtonPanel.add(resetButton);
        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }

    private void solveSudoku() {
        int[][] puzzle = new int[9][9];

        // Get the values entered by the user and fill the puzzle array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                try {
                    puzzle[i][j] = Integer.parseInt(Board[i][j].getText());
                } catch (NumberFormatException e) {
                    puzzle[i][j] = 0;
                }
            }
        }

        // Solve the Sudoku puzzle
        if (SudokuSolve.solve(puzzle)) {
            // Update the UI with the solution
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Board[i][j].setText(Integer.toString(puzzle[i][j]));
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "The puzzle could not be solved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new SudokuFrame();
    }
}
