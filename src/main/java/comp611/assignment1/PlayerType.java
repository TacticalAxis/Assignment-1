package comp611.assignment1;

@SuppressWarnings("unused")
public enum PlayerType {
    PLAYER_1("Player 1"),
    PLAYER_2 ("Player 2");

    private final String label;

    public static final char X = 'X';
    public static final char O = 'O';

    PlayerType(String label){
        this.label = label;
    }

    public Character getToken() {
        switch (this) {
            case PLAYER_1:
                return 'X';
            case PLAYER_2:
                return 'O';
            default:
                return null;
        }
    }

    public static PlayerType getPlayer(char symbol){
        return symbol == 'X' ? PLAYER_1 : PLAYER_2;
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