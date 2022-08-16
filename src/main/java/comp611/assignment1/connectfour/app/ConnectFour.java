package comp611.assignment1.connectfour.app;

import java.util.ArrayList;
import java.util.List;

public class ConnectFour {

    char[][] board;
    private boolean gameOver;

    public ConnectFour(int height, int width) {
        board = new char[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                board[i][j] = ' ';
            }
        }
        this.gameOver = false;
    }

    public synchronized boolean gameOver() {
        return gameOver;
    }

    private int getHeight() {
        return board.length;
    }

    public int getWidth() {
        return board[0].length;
    }

    private boolean isInBounds(int row, int column) {
        return row >= 0 && row < getHeight() && column >= 0 && column < getWidth();
    }

    private List<Character[]> getAllBRTTLDiagonals() {
        List<Character[]> diagonals = new ArrayList<>();

        // iterate through the top row
        for (int i = 0; i < getWidth(); i++) {
            diagonals.add(getTopRightToBottomLeftDiagonal(0, i));
        }

        // iterate through the bottom row
        for (int i = 0; i < getWidth(); i++) {
            diagonals.add(getTopRightToBottomLeftDiagonal(getHeight() - 1, i));
        }

        cleanList(diagonals);

        return diagonals;
    }

    public String displayBoard() {
        StringBuilder sb = new StringBuilder();
        // append underscores for the top row
        for (int i = 0; i < (getWidth() * 2) + 1; i++) {
            sb.append("_");
        }
        sb.append("\n");
        for (int i = 0; i < getHeight(); i++) {
            sb.append("|");
            for (int j = 0; j < getWidth(); j++) {
                sb.append(board[i][j]);
                sb.append("|");
            }
            if (i != getHeight() - 1) {
                sb.append("\n");
            }
        }
        sb.append("\n");
        // append underscores to the end of the board
        for (int i = 0; i < (getWidth() * 2) + 1; i++) {
            sb.append("\u0305");
        }
        return sb.toString();
    }

    // get bottom right to top left diagonals
    private Character[] getBottomRightToTopLeftDiagonals(int row, int column) {
        int currentRow = row;
        int currentColumn = column;

        Character[] diagonal = new Character[getHeight()];

        while (isInBounds(currentRow, currentColumn)) {
            if (!isInBounds(currentRow + 1, currentColumn + 1)) {
                break;
            } else {
                currentRow++;
                currentColumn++;
            }
        }

        int dCount = 0;
        while (isInBounds(currentRow, currentColumn)) {
            diagonal[dCount] = getBoard()[currentRow][currentColumn];
            currentRow--;
            currentColumn--;
            dCount++;
        }

        return diagonal;
    }

    private List<Character[]> getAllTRTBLDiagonals() {
        List<Character[]> diagonals = new ArrayList<>();

        // iterate through the top row
        for (int i = 0; i < getWidth(); i++) {
            diagonals.add(getBottomRightToTopLeftDiagonals(0, i));
        }

        // iterate through the bottom row
        for (int i = 0; i < getWidth(); i++) {
            diagonals.add(getBottomRightToTopLeftDiagonals(getHeight() - 1, i));
        }

        cleanList(diagonals);

        return diagonals;
    }

    // get top right to bottom left diagonals
    private Character[] getTopRightToBottomLeftDiagonal(int row, int column) {
        int currentRow = row;
        int currentColumn = column;

        Character[] diagonal = new Character[getHeight()];

        while (isInBounds(currentRow, currentColumn)) {
            if (!isInBounds(currentRow - 1, currentColumn + 1)) {
                break;
            } else {
                currentRow--;
                currentColumn++;
            }
        }

        int dCount = 0;
        while (isInBounds(currentRow, currentColumn)) {
            diagonal[dCount] = getBoard()[currentRow][currentColumn];
            currentRow++;
            currentColumn--;
            dCount++;
        }

        return diagonal;
    }

    public char[][] getBoard() {
        return board;
    }

    public boolean isValidMove(int move) {
        if (move < 1 || move > getWidth()) {
            return false;
        }

        return board[0][move - 1] == ' ';
    }

    public PlayerType hasWon() {
        // look through horizontally -
        PlayerType pt;
        for (int row = 0; row < getBoard().length; row++) {
            pt = checkWinner(getBoard()[row]);
            if (pt != null) {
                return pt;
            }
        }

        // look through vertically |
        for (int col = 0; col < getBoard()[0].length; col++) {
            char[] column = new char[getBoard().length];
            for (int row = 0; row < getBoard().length; row++) {
                column[row] = getBoard()[row][col];
            }
            pt = checkWinner(column);
            if (pt != null) {
                return pt;
            }
        }

        // look through diagonally \
        for (Character[] diagonal : getAllBRTTLDiagonals()) {
            char[] diag = new char[diagonal.length];
            for (int i = 0; i < diagonal.length; i++) {
                diag[i] = diagonal[i];
            }
            pt = checkWinner(diag);
            if (pt != null) {
                return pt;
            }
        }

        // look through diagonally /
        for (Character[] diagonal : getAllTRTBLDiagonals()) {
            char[] diag = new char[diagonal.length];
            for (int i = 0; i < diagonal.length; i++) {
                diag[i] = diagonal[i];
            }
            pt = checkWinner(diag);
            if (pt != null) {
                gameOver = true;
                return pt;
            }
        }

        return null;
    }

    private PlayerType checkWinner(char[] set) {
        char last = '\0';
        int count = 0;

        for (char c : set) {
            if (c == ' ') {
                continue;
            }

            if (c == last) {
                count++;
            } else {
                count = 0;
            }
            if (count == 3) {
                gameOver = true;
                return PlayerType.getPlayer(last);
            }
            last = c;
        }

        return null;
    }

    private void cleanList(List<Character[]> list) {
        list.removeIf(diagonal -> {
            for (Character character : diagonal) {
                if (character != null) {
                    return false;
                }
            }
            return true;
        });

        list.forEach(diagonal -> {
            for (int i = 0; i < diagonal.length; i++) {
                if (diagonal[i] == null) {
                    diagonal[i] = ' ';
                }
            }
        });
    }

    public void dropToken(int col, PlayerType pt) {
        int useCol = col - 1;

        if (useCol < 0 || useCol >= getWidth()) {
            return;
        }

        int i = board.length - 1;
        while (board[i][useCol] != ' ') {
            i--;
        }
        board[i][useCol] = pt.getToken();
    }
}