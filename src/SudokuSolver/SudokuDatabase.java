package SudokuSolver;

import java.sql.*;

public class SudokuDatabase {
    private Connection connection;
    private String url = "jdbc:mysql://localhost:3306/sudoku";
    private String username = "root";
    private String password = "password";

    public SudokuDatabase() throws SQLException {
        // Connect to the database
        String url = "jdbc:mysql://localhost:3306/sudoku";
        String username = "root@localhost";
        String password = "";
        connection = DriverManager.getConnection(url, username, password);
    }

    public String getPuzzle() throws SQLException {
        String query = "SELECT puzzle FROM puzzles ORDER BY RAND() LIMIT 1";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("puzzle");
            } else {
                throw new SQLException("No puzzles found in database.");
            }
        }
    }

    public String getSolution(int id) throws SQLException {
        // Retrieve the solution from the database
        String query = "SELECT solution FROM puzzles WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("solution");
        } else {
            return null;
        }
    }

    public void saveScore(String username, int puzzleId, int score, int time) throws SQLException {
        String query = "INSERT INTO scores (username, puzzle_id, score, time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, username, password);
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setInt(2, puzzleId);
            stmt.setInt(3, score);
            stmt.setInt(4, time);
            stmt.executeUpdate();
        }
    }
    
    public ResultSet getLeaderboard() throws SQLException {
        String query = "SELECT username, puzzle_id, score, time FROM scores ORDER BY score DESC, time ASC";
        Connection conn = DriverManager.getConnection(url, username, password);
        PreparedStatement stmt = conn.prepareStatement(query);
        return stmt.executeQuery();
    }
    
    public int getPuzzleId(String puzzle) throws SQLException {
        int puzzleId = 0;
        String query = "SELECT id FROM puzzles WHERE puzzle = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, puzzle);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    puzzleId = resultSet.getInt("id");
                }
            }
        }
        return puzzleId;
    }

    public void close() throws SQLException {
        // Close the database connection
        connection.close();
    }

    public static void main(String[] args) {
        try {
            SudokuDatabase database = new SudokuDatabase();
            String puzzle = database.getPuzzle();
            String solution = database.getSolution(1);
            database.close();
            // Use the puzzle and solution data as needed
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
