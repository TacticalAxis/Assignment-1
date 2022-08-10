package comp611.assignment1.game;

import java.util.Random;

@SuppressWarnings("unused")
public enum PlayerType {
    CROSS("Cross"),
    CIRCLE("Circle");

    private final String label;

    public static final char X = 'X';
    public static final char O = 'O';

    PlayerType(String label){
        this.label = label;
    }

    public Character getToken() {
        return this == PlayerType.CROSS ? 'X' : 'O';
    }

    public static PlayerType getPlayer(char symbol){
        return symbol == 'X' ? CROSS : CIRCLE;
    }

    public String getLabel() {
        return label;
    }

    // for testing purposes only
    public static PlayerType r() {
        Random random = new Random();
        return random.nextBoolean() ? PlayerType.CROSS : PlayerType.CIRCLE;
    }

    @Override
    public String toString() {
        return "PlayerType{" +
                "label='" + label + '\'' +
                '}';
    }
}