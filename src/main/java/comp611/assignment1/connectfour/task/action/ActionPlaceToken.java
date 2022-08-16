package comp611.assignment1.connectfour.task.action;

import comp611.assignment1.connectfour.app.ConnectFour;
import comp611.assignment1.connectfour.app.PlayerType;

public class ActionPlaceToken implements GameAction {

    private final ConnectFour game;
    private final PlayerType playerType;
    private final int column;

    private String message;

    public ActionPlaceToken(ConnectFour game, PlayerType playerType, int column) {
        this.game = game;
        this.playerType = playerType;
        this.column = column;
        this.message = null;
    }

    @Override
    public void execute() {
        game.dropToken(column, playerType);
        this.message = "Token dropped at " + column + " from " + playerType.getLabel();
    }

    @Override
    public String message() {
        return this.message;
    }

    @Override
    public String toString() {
        return "ActionPlaceToken{" +
                "game=" + game +
                ", playerType=" + playerType +
                ", column=" + column +
                '}';
    }
}