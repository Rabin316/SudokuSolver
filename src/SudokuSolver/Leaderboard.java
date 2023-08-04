/**
 * The Leaderboard class is a Java Swing application that displays a leaderboard for a Sudoku game,
 * allowing users to select a difficulty level and view the rankings of players based on their
 * performance.
 */
package SudokuSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Leaderboard extends JFrame {
    private JComboBox<String> difficultyComboBox;
    private JTable leaderboardTable;
    private DefaultTableModel tableModel;

    public Leaderboard() {
        setTitle("Leaderboard");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);

        // Create leaderboard panel
        JPanel leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Create difficulty selection panel
        JPanel selectionPanel = new JPanel(new FlowLayout());
        selectionPanel.add(new JLabel("Difficulty:"));
        difficultyComboBox = new JComboBox<>();
        difficultyComboBox.addItem("All");
        difficultyComboBox.addItem("Easy");
        difficultyComboBox.addItem("Medium");
        difficultyComboBox.addItem("Hard");
        selectionPanel.add(difficultyComboBox);

        JButton showButton = new JButton("Show");
        showButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Retrieve and display the leaderboard based on the selected difficulty
                String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
                showLeaderboard(selectedDifficulty);
            }
        });
        selectionPanel.add(showButton);

        leaderboardPanel.add(selectionPanel, BorderLayout.NORTH);

        // Create leaderboard table
        tableModel = new DefaultTableModel();
        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setRowHeight(25);
        leaderboardTable.getTableHeader().setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        leaderboardTable.setDefaultRenderer(Object.class, centerRenderer);

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(leaderboardPanel);
        pack();
        setPreferredSize(new Dimension(500, 400));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void showLeaderboard(String difficulty) {
        try {
            // Retrieve leaderboard data from the database based on the selected difficulty
            SudokuDatabase database = new SudokuDatabase();
            ResultSet resultSet = database.getLeaderboard(difficulty);

            // Create column headers
            Vector<String> columnNames = new Vector<>();
            columnNames.add("Rank");
            columnNames.add("Username");
            columnNames.add("Puzzles Solved");
            columnNames.add("Score");
            columnNames.add("Shortest Time (s)");

            // Populate data rows
            Vector<Vector<Object>> data = new Vector<>();
            int rank = 1;
            while (resultSet.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rank);
                row.add(resultSet.getString("username"));
                row.add(resultSet.getInt("puzzles_solved"));
                row.add(resultSet.getInt("total_score"));
                row.add(resultSet.getInt("shortest_time"));
                data.add(row);
                rank++;
            }

            // Update table model
            tableModel.setDataVector(data, columnNames);

            database.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Leaderboard();
            }
        });
    }
}
