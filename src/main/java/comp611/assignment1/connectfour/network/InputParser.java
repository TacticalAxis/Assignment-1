package comp611.assignment1.connectfour.network;

public class InputParser {

    private InputParser(){}

    public static int parsePositiveInt(String str, int low, int high) {
        int num;// = Integer.parseInt(str);

        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }

        if (num < low || num > high) {
            return -1;
        }

        return num;
    }
}