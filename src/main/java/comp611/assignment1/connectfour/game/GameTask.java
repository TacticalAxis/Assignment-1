package comp611.assignment1.connectfour.game;

import comp611.assignment1.connectfour.object.Task;

public class GameTask extends Task<GameAction, String> {

    private final GameAction action;

    public GameTask(GameAction param) {
        super(param);
        this.action = param;
    }

    @Override
    public void run() {
        action.execute();
    }
}