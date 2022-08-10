package comp611.assignment1.game;

import java.util.*;

@SuppressWarnings({"unused"})
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

    private char[] getRow(int row) {
        return board[row];
    }

    private char[] getColumn(int column) {
        char[] columnArray = new char[getHeight()];
        for (int i = 0; i < getHeight(); i++) {
            columnArray[i] = board[i][column];
        }
        return columnArray;
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

    // get bottom right to top left diagonals
    private Character[] getBottomRightToTopLeftDiagonals(int row, int column) {
        int currentRow = row;
        int currentColumn = column;

        Character[] diagonal = new Character[getHeight()];

        while (isInBounds(currentRow, currentColumn)) {
            if(!isInBounds(currentRow + 1, currentColumn + 1)) {
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
            if(!isInBounds(currentRow - 1, currentColumn + 1)) {
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

    public static void main(String[] args) {
        ConnectFour c4 = new ConnectFour(6, 7);

        c4.board[0] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};
        c4.board[1] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};
        c4.board[2] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};
        c4.board[3] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};
        c4.board[4] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};
        c4.board[5] = new char[]{' ', ' ', ' ', ' ', ' ', ' ', ' '};


        c4.enterToken();
        Random random = new Random();

        c4.board[0] = new char[]{' ', ' ', ' ', 'O', ' ', ' ', ' '};
        c4.board[1] = new char[]{' ', 'X', ' ', 'X', ' ', ' ', ' '};
        c4.board[2] = new char[]{' ', 'X', ' ', 'X', 'X', ' ', ' '};
        c4.board[3] = new char[]{' ', 'O', 'O', 'X', 'X', ' ', ' '};
        c4.board[4] = new char[]{' ', 'O', 'X', 'O', 'O', ' ', ' '};
        c4.board[5] = new char[]{'O', 'X', 'X', 'X', 'O', 'O', ' '};

//        for (int i = 0; i < c4.getHeight(); i++) {
//            for (int j = 0; j < c4.getWidth(); j++) {
//                int num = random.nextInt(2);
//                c4.board[i][j] = (num == 1) ? 'X' : 'O';
//            }
//        }

        for (int i = 0; i < c4.getHeight(); i++) {
            System.out.println(Arrays.toString(c4.getBoard()[i]));
        }

        // drop token
        c4.dropToken(2, PlayerType.CROSS);

        System.out.println();

        // print table again
        for (int i = 0; i < c4.getHeight(); i++) {
            System.out.println(Arrays.toString(c4.getBoard()[i]));
        }

        PlayerType pt = c4.hasWon();
        System.out.println("Winner: " + pt);
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
                return pt;
            }
        }

        return null;
    }

    private PlayerType checkWinner(char[] set) {
        char last = '\0';
        int count = 0;

        for (char c : set) {
            if(c == ' ') {
                continue;
            }

            if (c == last) {
                count++;
            } else {
                count = 0;
            }
            if (count == 3) {
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

    public void dropToken(int col, PlayerType pt){
        int i = board.length - 1;
        while (board[i][col] != ' ') {
            i--;
        }
        System.out.println("i: " + i);
        board[i][col] = pt.getToken();
    }

    public void enterToken() {
        Scanner scanner = new Scanner(System.in);
        int inputCol = 0;


        PlayerType pt = PlayerType.CROSS;
        for (int i = 1; i < getHeight() * getWidth(); i++) {
            if (i % 2 == 0) {
                pt = PlayerType.CIRCLE;
            } else {
                pt = PlayerType.CROSS;
                ;
            }

            System.out.println("Enter a column between 0 and " + getHeight());
            inputCol = scanner.nextInt();
            if (inputCol < 0 || inputCol > getHeight()) {
                System.out.println("Invalid column");
                System.out.println("Enter a column between 0 and " + getHeight());
                inputCol = scanner.nextInt();
            }
            dropToken(inputCol, pt);

            for (int j = 0; j < getHeight(); j++) {
                System.out.println(Arrays.toString(getBoard()[j]));
            }

            pt = hasWon();
            if(pt != null) {
                System.out.println("Winner: " + pt);
                break;
            }
        }
    }
}