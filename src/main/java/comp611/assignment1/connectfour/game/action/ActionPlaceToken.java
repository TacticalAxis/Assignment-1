package comp611.assignment1.connectfour.game.action;

import comp611.assignment1.connectfour.game.ConnectFour;
import comp611.assignment1.connectfour.game.GameAction;
import comp611.assignment1.connectfour.game.PlayerType;

public class ActionPlaceToken implements GameAction {

    private final ConnectFour game;
    private final PlayerType playerType;
    private final int column;

    public ActionPlaceToken(ConnectFour game, PlayerType playerType, int column) {
        this.game = game;
        this.playerType = playerType;
        this.column = column;
    }

    @Override
    public void execute() {
        game.dropToken(column, playerType);
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