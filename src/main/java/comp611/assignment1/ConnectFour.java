package comp611.assignment1;

import java.util.Arrays;
import java.util.Random;

@SuppressWarnings({"ManualArrayCopy", "unused"})
public class ConnectFour {

    char[][] board;

    public ConnectFour(int height, int width){
        board = new char[height][width];
    }

    private int getHeight() {
        return board.length;
    }

    private int getWidth() {
        return board[0].length;
    }

    public static void main(String[] args) {
        ConnectFour c4 = new ConnectFour(6, 7);

        Random random = new Random();

        for (int i = 0; i < c4.getHeight(); i++) {
            for (int j = 0; j < c4.getWidth(); j++) {
                int num = random.nextInt(2);
                System.out.println("test: " + i + " " + j);
                c4.board[i][j] = (num == 1) ? 'X' : 'O';
            }
        }

        for (int i = 0; i < c4.getHeight(); i++) {
            System.out.println(Arrays.toString(c4.getBoard()[i]));
        }

        PlayerType pt = c4.hasWon();
        System.out.println(pt);
    }

    public boolean isPlayable(int column){
        return board[0][column] == 0;
    }

    public char[][] getBoard() {
        return board;
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
        for (char[] chars : board) {
            char[] setToCheck = new char[chars.length];
            for (int j = 0; j < chars.length; j++) {
                setToCheck[j] = chars[j];
            }
            pt = checkWinner(setToCheck);
            if (pt != null) {
                return pt;
            }
        }

        // look through lDiagonal /


        // look through rDiagonal \



        return null;
    }

    private PlayerType checkWinner(char[] set) {
        char last = '\0';
        int count = 0;
        for (char c : set) {
            if (last == '\0') {
                last = c;
                continue;
            }

            if (c == last) {
                count += 1;
            } else {
                last = c;
                count = 0;
            }
        }

        return count >= 4 ? PlayerType.getPlayer(last) : null;
    }
}