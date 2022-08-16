package comp611.assignment1.connectfour.app;

public class InputParser {

    private InputParser() {
    }

    public static int parsePositiveInt(String str, int low, int high) {
        int num;

        // parse string to int
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }

        // check if int is in range
        if (num < low || num > high) {
            return -1;
        }

        // return int
        return num;
    }
}