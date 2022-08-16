package comp611.assignment1.connectfour.app;

public enum PlayerType {
    CROSS("Cross"),
    CIRCLE("Circle");

    private final String label;

    PlayerType(String label) {
        this.label = label;
    }

    public static PlayerType getPlayer(char symbol) {
        return symbol == 'X' ? CROSS : CIRCLE;
    }

    public Character getToken() {
        return this == PlayerType.CROSS ? 'X' : 'O';
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return "PlayerType{" +
                "label='" + label + '\'' +
                '}';
    }
}