package SudokuSolver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SudokuDatabase {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/sudoku";
    private String username = "root";
    private String password = "password";

    public SudokuDatabase() throws SQLException {
        // Connect to the database
        connection = DriverManager.getConnection(url, username, password);
    }

    public void saveScore(String username, int score, int time, String difficulty) throws SQLException {
        String query = "INSERT INTO scores (username, score, time_taken, difficulty) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, score);
            stmt.setInt(3, time);
            stmt.setString(4, difficulty);
            stmt.executeUpdate();
        }
    }
    
   public ResultSet getLeaderboard(String difficulty) throws SQLException {
    String query;
    if (difficulty.equals("All")) {
        query = "SELECT username, COUNT(id) AS puzzles_solved, MIN(time_taken) AS shortest_time, SUM(score) AS total_score FROM scores GROUP BY username ORDER BY puzzles_solved DESC, shortest_time ASC";
    } else {
        query = "SELECT username, COUNT(id) AS puzzles_solved, MIN(time_taken) AS shortest_time, SUM(score) AS total_score FROM scores WHERE difficulty = ? GROUP BY username ORDER BY puzzles_solved DESC, shortest_time ASC";
    }
    PreparedStatement stmt = connection.prepareStatement(query);
    if (!difficulty.equals("All")) {
        stmt.setString(1, difficulty);
    }
    return stmt.executeQuery();
}


    public void close() throws SQLException {
        // Close the database connection
        connection.close();
    }
}

