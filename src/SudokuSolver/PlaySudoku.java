package SudokuSolver;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class PlaySudoku extends JFrame {
    public JTextField[][] Board;
    private long startTime = 0;
    private boolean puzzleGenerated = false;
    public String username;
    private JLabel puzzleIdLabel;
    private int score = 0;

    public PlaySudoku(String username) {
        this.username = username;
        setTitle("Play Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel BoardPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        Board = new JTextField[9][9];

        // Create puzzle ID label
        puzzleIdLabel = new JLabel("Puzzle ID: ");
        puzzleIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        getContentPane().add(puzzleIdLabel, BorderLayout.NORTH);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(40, 40));
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
                textField.getDocument().addDocumentListener(new DocumentListener() {
                    public void insertUpdate(DocumentEvent e) {
                        // Start the timer if a puzzle has been generated
                        if (puzzleGenerated && startTime == 0) {
                            startTime = System.currentTimeMillis();
                        }
                    }

                    public void removeUpdate(DocumentEvent e) {
                    }

                    public void changedUpdate(DocumentEvent e) {
                    }
                });

            }
        }

        // Buttons Solve, Reset, and Check
        JButton backButton = new JButton("Back");
        JButton generate = new JButton("Generate");
        JButton check = new JButton("Check");
        JPanel ButtonPanel = new JPanel();

        // Timer Option
        JLabel timerLabel = new JLabel("00:00:00");
        ButtonPanel.add(timerLabel);
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (startTime != 0) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    long seconds = elapsedTime / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60));
                }
            }
        });
        timer.start();
        // Back Option
        backButton.setToolTipText("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close PlaySudoku UI
                new MainMenu(username); // Open Main Menu
            }
        });
        ButtonPanel.add(backButton);
        ButtonPanel.add(generate);
        ButtonPanel.add(check);
        // Add an ActionListener to the "Generate" button
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // Retrieve a puzzle from the database
                    SudokuDatabase database = new SudokuDatabase();
                    String puzzle = database.getPuzzle();
                    int puzzleId = database.getPuzzleId(puzzle);
                    database.close();

                    // Update the puzzle ID label
                    puzzleIdLabel.setText("Puzzle ID: " + puzzleId);

                    // Populate the UI with the puzzle data
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            char c = puzzle.charAt(i * 9 + j);
                            if (c != '0') {
                                Board[i][j].setText(String.valueOf(c));
                                Board[i][j].setEditable(false);
                            } else {
                                Board[i][j].setText("");
                                Board[i][j].setEditable(true);
                            }
                        }
                    }

                    // Start the timer
                    puzzleGenerated = true;
                    startTime = System.currentTimeMillis();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Add an ActionListener to the "Check" button
        // Add an ActionListener to the "Check" button
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (puzzleGenerated) {
                    // Stop the timer
                    long elapsedTime = 0;
                    long pausedTime = 0;

                    if (startTime != 0) {
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = 0;
                    }

                    // Display the elapsed time in the dialog message
                    String message = "";
                    if (elapsedTime > 0) {
                        long seconds = elapsedTime / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        message = String.format("Time taken: %02d:%02d:%02d", hours, minutes % 60, seconds % 60);
                    }
                    // ...
                    // Check if all cells are filled
                    boolean allFilled = true;
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (Board[i][j].getText().isEmpty()) {
                                allFilled = false;
                                break;
                            }
                        }
                        if (!allFilled) {
                            break;
                        }
                    }

                    if (allFilled) {
                        try {
                            // Retrieve the ID of the puzzle from the puzzleIdLabel
                            String puzzleId = puzzleIdLabel.getText().split(": ")[1];

                            // Retrieve the solution from the database
                            SudokuDatabase database = new SudokuDatabase();
                            String solution = database.getSolution(Integer.parseInt(puzzleId));
                            database.close();

                            // Check the solution against the user input
                            boolean correct = true;
                            for (int i = 0; i < 9; i++) {
                                for (int j = 0; j < 9; j++) {
                                    String value = Board[i][j].getText();
                                    if (value.equals(String.valueOf(solution.charAt(i * 9 + j)))) {
                                        Board[i][j].setBackground(Color.WHITE);
                                    } else {
                                        Board[i][j].setBackground(Color.RED);
                                        correct = false;
                                    }
                                }
                            }

                            // Display a congratulatory message if the solution is correct
                            if (correct) {
                                score += 20; // Increment the user's score by 20

                                if (startTime != 0) {
                                    elapsedTime = System.currentTimeMillis() - startTime;
                                    startTime = 0;
                                }

                                try {
                                    // Save the user's score to the database
                                    database.saveScore(username, Integer.parseInt(puzzleId), score,
                                            (int) elapsedTime / 1000);
                                    database.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null,
                                        "Congratulations! You solved the puzzle.\n" + message + "\nScore: " + score,
                                        "Sudoku Solver", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                // Display an error message if the solution is incorrect
                                // Pause the timer and show the dialog
                                if (elapsedTime > 0) {
                                    pausedTime = elapsedTime; // Store the elapsed time
                                    startTime = 0; // Pause the timer
                                    JOptionPane.showMessageDialog(null,
                                            "Sorry, your solution is incorrect. Please try again.",
                                            "Sudoku Solver", JOptionPane.ERROR_MESSAGE);
                                    startTime = System.currentTimeMillis() - pausedTime; // Resume the timer with the
                                                                                         // remaining time
                                }
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        // Display a warning message if all cells are not filled
                        // Pause the timer and show the dialog
                        if (elapsedTime > 0) {
                            pausedTime = elapsedTime; // Store the elapsed time
                            startTime = 0; // Pause the timer
                            JOptionPane.showMessageDialog(null,
                                    "Please fill in all cells before checking the solution.",
                                    "Sudoku Solver", JOptionPane.WARNING_MESSAGE);
                            startTime = System.currentTimeMillis() - pausedTime; // Resume the timer with the remaining
                                                                                 // time
                        }

                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please generate a puzzle before checking the solution.",
                            "Sudoku Solver", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);
        setVisible(true);
    }

    public static void main(String[] args) {
        // new PlaySudoku(username);
    }
}