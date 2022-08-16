package comp611.assignment1.connectfour.game;

import comp611.assignment1.connectfour.object.Task;
import comp611.assignment1.connectfour.object.TaskObserver;

public class GameTask extends Task<GameAction, String> {

    private final GameAction action;

    public GameTask(GameAction param, TaskObserver<String>... observers) {
        super(param);
        this.action = param;
        for(TaskObserver<String> t : observers) {
            this.addListener(t);
        }
    }

    @Override
    public void run() {
        action.execute();
        notifyAll(getParam().message());
    }

    @Override
    public void notifyAll(String progress) {
        super.notifyAll(progress);
    }
}