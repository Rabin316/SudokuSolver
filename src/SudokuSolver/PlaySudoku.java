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
    private SudokuGenerator generator;
    public SudokuSolver solver;
    private int[][] puzzle;
    private int[][] solution;
    private int score = 0;
    // Declare selectedOption as a class member variable
    private String selectedOption;
    private int currentDifficulty;
    private long timeTaken;
    public long elapsedTime = 0;

    public PlaySudoku(String username) {
        this.username = username;
        setTitle("Play Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel BoardPanel = new JPanel(new GridLayout(9, 9, 0, 0));
        Board = new JTextField[9][9];

        // Create puzzle ID label
        puzzleIdLabel = new JLabel("Difficulty Level: ");
        puzzleIdLabel.setFont(new Font("Arial", Font.BOLD, 12));
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

        generator = new SudokuGenerator();
        solver = new SudokuSolver();

        // Add an ActionListener to the "Generate" button
        generate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Create a dialog box to choose the difficulty level
                String[] options = { "Easy", "Medium", "Hard" };
                selectedOption = (String) JOptionPane.showInputDialog(
                        PlaySudoku.this,
                        "Choose difficulty level:",
                        "Difficulty Level",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options,
                        options[0]);

                if (selectedOption != null) {
                    // Store the current difficulty level
                    if (selectedOption.equals("Easy")) {
                        currentDifficulty = SudokuGenerator.EASY;
                    } else if (selectedOption.equals("Medium")) {
                        currentDifficulty = SudokuGenerator.MEDIUM;
                    } else {
                        currentDifficulty = SudokuGenerator.HARD;
                    }

                    generator.generate(currentDifficulty);
                    puzzle = generator.getPuzzle();
                    solution = generator.getSolution();

                    // Update the puzzle ID label with the chosen difficulty level
                    puzzleIdLabel.setText("Difficulty Level: " + selectedOption);

                    // Populate the UI with the puzzle data
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            int value = puzzle[i][j];
                            if (value != 0) {
                                Board[i][j].setText(String.valueOf(value));
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
                }
            }
        });
        // Add an ActionListener to the "Check" button
        check.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (puzzleGenerated) {
                    // Stop the timer
                    long elapsedTime = 0;
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
                        timeTaken = elapsedTime;
                    }

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
                        // Check the solution against the user input
                        boolean correct = true;
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                String value = Board[i][j].getText();
                                if (!value.equals(String.valueOf(solution[i][j]))) {
                                    correct = false;
                                    break;
                                }
                            }
                            if (!correct) {
                                break;
                            }
                        }

                        if (correct) {
                            // Calculate the score based on the difficulty level solved
                            if (selectedOption.equals("Easy")) {
                                score = 10;
                            } else if (selectedOption.equals("Medium")) {
                                score = 20;
                            } else if (selectedOption.equals("Hard")) {
                                score = 30;
                            }

                            // Display the congratulations message with the score, timer, and difficulty
                            // level
                            JOptionPane.showMessageDialog(PlaySudoku.this,
                                    "Congratulations!\nScore: " + score +
                                            "\nTime taken: " + message +
                                            "\nDifficulty Level: " + selectedOption,
                                    "Congratulations!",
                                    JOptionPane.INFORMATION_MESSAGE);

                            // Save the score in the database
                            try {
                                SudokuDatabase database = new SudokuDatabase();
                                database.saveScore(username, score, (int) (timeTaken / 1000), selectedOption);
                                database.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(PlaySudoku.this,
                                    "Incorrect solution. Please try again.",
                                    "Incorrect Solution",
                                    JOptionPane.ERROR_MESSAGE);
                            // Reset the startTime to resume the timer
                            startTime = System.currentTimeMillis() - elapsedTime;
                        }
                    } else {
                        JOptionPane.showMessageDialog(PlaySudoku.this,
                                "Please fill all cells before checking.",
                                "Incomplete Sudoku",
                                JOptionPane.WARNING_MESSAGE);
                        // Reset the startTime to resume the timer
                        startTime = System.currentTimeMillis() - elapsedTime;
                    }

                } else {
                    JOptionPane.showMessageDialog(PlaySudoku.this,
                            "Please generate a puzzle first.",
                            "No Puzzle Generated",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new PlaySudoku("Player");
            }
        });
    }
}
