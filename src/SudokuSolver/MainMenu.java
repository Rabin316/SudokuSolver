package SudokuSolver;

import javax.swing.*;

import SudokuSolver.Login.LoginRegistration;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {
    public MainMenu(String username) {
        setTitle("Sudoku Solver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // Create user menu
        JMenu userMenu = new JMenu("User: " + username);
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(userMenu);

        // Create logout menu item
        JMenuItem logoutMenuItem = new JMenuItem("Logout");
        userMenu.add(logoutMenuItem);
        // Create leaderboard menu

        // Add action listener to the logout menu item
        logoutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new LoginRegistration(); // Open login and registration UI
            }
        });

        // Create menu panel
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Create title label
        JLabel titleLabel = new JLabel("Sudoku Solver", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        menuPanel.add(titleLabel);

        // Create play button
        JButton playButton = new JButton("Play Sudoku");
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new PlaySudoku(username); // Open PlaySudoku UI
            }
        });
        menuPanel.add(playButton);

        // Create solver button
        JButton solverButton = new JButton("Sudoku Solver");
        solverButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close main menu
                new SudokuFrame(username); // Open Sudoku Solver UI
            }
        });
        menuPanel.add(solverButton);

        // Create leaderboard menu item
        JButton leaderboard = new JButton("LeaderBoard");
        // Add action listener to the leaderboard menu item
        leaderboard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Leaderboard(); // Open leaderboard UI
            }
        });

        menuPanel.add(leaderboard);
        getContentPane().add(menuPanel, BorderLayout.CENTER);

        // Set the size of the window
        setSize(400, 500);

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);

        // Make the window visible
        setVisible(true);
    }

    // public static void main(String[] args) {
    //     // new MainMenu();
    // }
}