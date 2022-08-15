package comp611.assignment1.connectfour.game;

import java.util.Random;

@SuppressWarnings("unused")
public enum PlayerType {
    CROSS("Cross", "Player 1"),
    CIRCLE("Circle", "Player 2");


    private final String label;
    private final String name;

    public static final char X = 'X';
    public static final char O = 'O';

    PlayerType(String label, String name){
        this.label = label;
        this.name = name;
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