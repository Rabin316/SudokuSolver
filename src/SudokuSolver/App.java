/**
 * The App class is the entry point of the SudokuSolver application and it initializes the
 * LoginRegistration class.
 */
package SudokuSolver;

import SudokuSolver.Login.LoginRegistration;

public class App {
    public static void main(String[] args) {
       LoginRegistration lr=new LoginRegistration();
        lr.main(args);
    }
}