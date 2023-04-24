
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuFrame extends JFrame {
    SudokuFrame()
    {
        setTitle("SudokuSolver");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel BoardPanel = new JPanel(new GridLayout(9,9,2,2));
        JTextField[][] Board = new JTextField [9][9];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                JTextField textField = new JTextField();
                textField.setPreferredSize(new Dimension(50, 50));
                textField.setHorizontalAlignment(JTextField.CENTER);
                Board[i][j] = textField;
                BoardPanel.add(textField);
            }
        }
        //Buttons Solve and Reset
        JPanel ButtonPanel = new JPanel();
        JButton resetButton =new JButton("Reset");
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //SolveSudoku();
            }
        });
        ButtonPanel.add(solveButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //ResetSudoku();
            }
        });
        ButtonPanel.add(resetButton);
        getContentPane().add(BoardPanel, BorderLayout.CENTER);
        getContentPane().add(ButtonPanel, BorderLayout.SOUTH);
        pack();
        setVisible(true);
    }
    public static void main(String[] args) {
        new SudokuFrame();
    }
}
