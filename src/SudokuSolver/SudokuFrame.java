package SudokuSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SudokuFrame extends JFrame {
    public JTextField[][] Board;

    SudokuFrame() {
        setTitle("SudokuSolver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(SudokuFrame.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()));
                        String line;
                        int row = 0;
                        while ((line = reader.readLine()) != null && row < 9) {
                            String[] values = line.split(",");
                            for (int col = 0; col < 9 && col < values.length; col++) {
                                String value = values[col].trim();
                                if (!value.isEmpty()) {
                                    Board[row][col].setText(value);
                                }
                            }
                            row++;
                        }
                        reader.close();
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(SudokuFrame.this, "Error reading file: " + ex.getMessage(),
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        fileMenu.add(openItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

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
                if (solveSudoku()) {
                    JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle solved!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(SudokuFrame.this, "Puzzle is unsolvable.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        ButtonPanel.add(solveButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        Board[i][j].setText("");
                    }
                }
            }
        });

        ButtonPanel.add(resetButton);
        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    
    private boolean solveSudoku() {
        int[][] puzzle = new int[9][9];
        // Copy values from text fields to puzzle array
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = Board[i][j].getText().trim();
                if (!value.isEmpty()) {
                    puzzle[i][j] = Integer.parseInt(value);
                }
            }
        }
        if (SudokuSolver.solve(puzzle)) {
            // Copy values from puzzle array back to text fields
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    Board[i][j].setText(Integer.toString(puzzle[i][j]));
                }
            }
            return true;
        } else {
            return false;
        }
    }

 

    public static void main(String[] args) {
        new SudokuFrame();
    }
}
