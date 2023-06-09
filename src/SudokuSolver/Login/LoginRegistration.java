package SudokuSolver.Login;

import javax.swing.*;

import SudokuSolver.MainMenu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginRegistration extends JFrame {
    public LoginRegistration() {
        setTitle("Sudoku Solver - Login and Registration");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        // Create login and registration panel
        JPanel loginRegistrationPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginRegistrationPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));


        // Create username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTextField = new JTextField();
        loginRegistrationPanel.add(usernameLabel);
        loginRegistrationPanel.add(usernameTextField);

        // Create password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        loginRegistrationPanel.add(passwordLabel);
        loginRegistrationPanel.add(passwordField);

        // Create login and registration buttons
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        loginRegistrationPanel.add(loginButton);
        loginRegistrationPanel.add(registerButton);

        // Add action listeners to the buttons
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate login credentials
                String username = usernameTextField.getText();
                String password = new String(passwordField.getPassword());
                if (validateLogin(username, password)) {
                    dispose(); // Close login and registration GUI
                    new MainMenu(username); // Open main menu
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open registration GUI
                new Registration();
            }
        });

        getContentPane().add(loginRegistrationPanel, BorderLayout.CENTER);

        // Set the title of the window
        setTitle("Sudoku Solver - Login and Registration");

        // Set the size of the window
        setSize(400, 300);

        // Center the window on the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);

        // Make the window visible
        setVisible(true);
    }

    private boolean validateLogin(String username, String password) {
        try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku", "root@localhost", "");

            // Prepare the SQL statement
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the SQL statement
            ResultSet rs = stmt.executeQuery();

            // Check if the result set has any rows
            if (rs.next()) {
                // Login successful
                return true;
            } else {
                // Login failed
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void main(String[] args) {
        // new LoginRegistration();
    }
}