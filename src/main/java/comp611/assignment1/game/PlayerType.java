package comp611.assignment1.game;

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
        switch (this) {
            case CROSS:
                return 'X';
            case CIRCLE:
                return 'O';
            default:
                return null;
        }
    }

    public static PlayerType getPlayer(char symbol){
        return symbol == 'X' ? CROSS : CIRCLE;
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