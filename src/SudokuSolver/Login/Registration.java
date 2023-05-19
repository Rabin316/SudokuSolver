package SudokuSolver.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class Registration extends JFrame{
    public Registration() {
        setTitle("Registration");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    
        // Create registration panel
        JPanel registrationPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        registrationPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
    
        // Create username label and text field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameTextField = new JTextField();
        registrationPanel.add(usernameLabel);
        registrationPanel.add(usernameTextField);
    
        // Create password label and password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();
        registrationPanel.add(passwordLabel);
        registrationPanel.add(passwordField);
    
        // Create confirm password label and password field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        registrationPanel.add(confirmPasswordLabel);
        registrationPanel.add(confirmPasswordField);
    
        // Create register button
        JButton registerButton = new JButton("Register");
        registrationPanel.add(registerButton);
    
        // Add action listener to the button
        registerButton.addActionListener((ActionListener) new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Validate registration form
                String username = usernameTextField.getText();
String password = new String(passwordField.getPassword());
String confirmPassword = new String(confirmPasswordField.getPassword());
if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
JOptionPane.showMessageDialog(null, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
} else if (!password.equals(confirmPassword)) {
JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
} else if (validateUsername(username)) {
JOptionPane.showMessageDialog(null, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
} else {
// Add new user to the database
if (addUser(username, password)) {
JOptionPane.showMessageDialog(null, "Registration successful", "Success", JOptionPane.INFORMATION_MESSAGE);
dispose(); // Close registration GUI
} else {
JOptionPane.showMessageDialog(null, "Registration failed", "Error", JOptionPane.ERROR_MESSAGE);
}
}
}
});
getContentPane().add(registrationPanel, BorderLayout.CENTER);
pack();
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
setLocation(centerX, centerY);
setVisible(true);

}
private boolean validateUsername(String username) {
    try {
    // Connect to the database
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku", "root", "root");
    // Prepare the SQL statement
    PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
    stmt.setString(1, username);

    // Execute the SQL statement
    ResultSet rs = stmt.executeQuery();

    // Check if the result set has any rows
    if (rs.next()) {
        // Username already exists
        return true;
    } else {
        // Username does not exist
        return false;
    }
} catch (SQLException ex) {
    ex.printStackTrace();
    return false;
}
}
private boolean addUser(String username, String password) {
    try {
    // Connect to the database
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku", "root@localhost", "");
    // Prepare the SQL statement
    PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)");
    stmt.setString(1, username);
    stmt.setString(2, password);

    // Execute the SQL statement
    int rows = stmt.executeUpdate();

    // Check if the statement was executed successfully
    if (rows > 0) {
        // User added successfully
        return true;
    } else {
        // User not added
        return false;
    }
} catch (SQLException ex) {
    ex.printStackTrace();
    return false;
}
}
public static void main(String[] args) {
    //new LoginRegistration();
    }
}
