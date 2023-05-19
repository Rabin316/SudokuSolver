package SudokuSolver;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Leaderboard extends JFrame {
    public Leaderboard() {
        setTitle("Leaderboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create leaderboard panel
        JPanel leaderboardPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        leaderboardPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Add column headers
        leaderboardPanel.add(new JLabel("Rank", SwingConstants.CENTER));
        leaderboardPanel.add(new JLabel("Username", SwingConstants.CENTER));
        leaderboardPanel.add(new JLabel("Puzzle ID", SwingConstants.CENTER));
        leaderboardPanel.add(new JLabel("Score", SwingConstants.CENTER));
        leaderboardPanel.add(new JLabel("Time (s)", SwingConstants.CENTER));

        try {
            // Retrieve leaderboard data from the database
            SudokuDatabase database = new SudokuDatabase();
            ResultSet resultSet = database.getLeaderboard();
            int rank = 1;
            while (resultSet.next()) {
                // Add leaderboard data to the panel
                leaderboardPanel.add(new JLabel(String.valueOf(rank), SwingConstants.CENTER));
                leaderboardPanel.add(new JLabel(resultSet.getString("username"), SwingConstants.CENTER));
                leaderboardPanel.add(new JLabel(String.valueOf(resultSet.getInt("puzzle_id")), SwingConstants.CENTER));
                leaderboardPanel.add(new JLabel(String.valueOf(resultSet.getInt("score")), SwingConstants.CENTER));
                leaderboardPanel.add(new JLabel(String.valueOf(resultSet.getInt("time")), SwingConstants.CENTER));
                rank++;
            }
            database.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        getContentPane().add(leaderboardPanel, BorderLayout.CENTER);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) ((screenSize.getWidth() - getWidth()) / 2);
        int centerY = (int) ((screenSize.getHeight() - getHeight()) / 2);
        setLocation(centerX, centerY);
        setVisible(true);
    }
}
