// By: Caesar R. Watts-Hall
// Date: June 08, 2023
import java.util.ArrayList;
import java.util.List;

class TicTacToe {
    public static final int EMPTY = 0;
    public static final int CROSS = 1;
    public static final int NOUGHT = 2;

    private int[][] board;
    private int currentPlayer;

    public TicTacToe() {
        board = new int[3][3];
        currentPlayer = CROSS;
    }

    public boolean isGameOver() {
        return hasWinner() || getAvailableCells().isEmpty();
    }

    public boolean hasWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != EMPTY && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != EMPTY && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
                return true;
            }
        }

        // Check diagonals
        if (board[0][0] != EMPTY && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return true;
        }
        if (board[0][2] != EMPTY && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return true;
        }

        return false;
    }

    public List<int[]> getAvailableCells() {
        List<int[]> availableCells = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    availableCells.add(new int[]{i, j});
                }
            }
        }
        return availableCells;
    }

    public boolean placeMove(int x, int y, int player) {
        if (board[x][y] != EMPTY) {
            return false;
        }
        board[x][y] = player;
        currentPlayer = (currentPlayer == CROSS) ? NOUGHT : CROSS;
        return true;
    }

    public void displayBoard() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.print(" ");
            for (int j = 0; j < 3; j++) {
                String value = "?";
                if (board[i][j] == CROSS) {
                    value = "X";
                } else if (board[i][j] == NOUGHT) {
                    value = "O";
                } else if (board[i][j] == EMPTY) {
                    value = " ";
                }
                System.out.print(value);
                if (j != 2) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
            if (i != 2) {
                System.out.println("-----------");
            }
        }
        System.out.println();
    }

    public int minimax(int depth, int turn) {
        if (hasWinner()) {
            return turn == NOUGHT ? +1 : -1;
        }
        if (getAvailableCells().isEmpty()) {
            return 0;
        }

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int[] cell : getAvailableCells()) {
            int x = cell[0];
            int y = cell[1];
            if (turn == NOUGHT) { // AI
                placeMove(x, y, NOUGHT);
                int currentScore = minimax(depth + 1, CROSS);
                max = Math.max(currentScore, max);
                if (depth == 0) {
                    System.out.println("Computer score for position " + x + "," + y + " = " + currentScore);
                }
                if (currentScore >= 0) { // Pruning
                    if (depth == 0) { // Root node
                        placeMove(x, y, NOUGHT);
                        displayBoard();
                        break;
                    }
                }
                if (currentScore == 1) { // Pruning
                    board[x][y] = EMPTY; // Undo move
                    break;
                }
                if (i == getAvailableCells().size() - 1 && max < 0) { // Last move
                    if (depth == 0) {
                        placeMove(x, y, NOUGHT);
                        displayBoard();
                    }
                }
            } else if (turn == CROSS) { // Human
                placeMove(x, y, CROSS);
                int currentScore = minimax(depth + 1, NOUGHT);
                min = Math.min(currentScore, min);
                if (min == -1) { // Pruning
                    board[x][y] = EMPTY; // Undo move
                    break;
                }
            }
            board[x][y] = EMPTY; // Undo move
        }
        return turn == NOUGHT ? max : min;
    }
}

public class Main {
    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.displayBoard();
        while (!game.isGameOver()) {
            game.minimax(0, TicTacToe.NOUGHT);
            if (game.isGameOver()) {
                break;
            }
            game.placeMove(1, 1, TicTacToe.CROSS); // Example: human player always places move at (1, 1)
            game.displayBoard();
        }
        if (game.hasWinner()) {
            System.out.println("We have a winner!");
        } else {
            System.out.println("It's a draw!");
        }
    }
}
