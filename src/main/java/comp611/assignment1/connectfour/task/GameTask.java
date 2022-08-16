package comp611.assignment1.connectfour.task;

import comp611.assignment1.connectfour.observer.TaskObserver;
import comp611.assignment1.connectfour.task.action.GameAction;

@SuppressWarnings("unchecked")
public class GameTask extends Task<GameAction, String> {

    private final GameAction action;

    public GameTask(GameAction param, TaskObserver<String>... observers) {
        super(param);
        this.action = param;
        for (TaskObserver<String> t : observers) {
            this.addListener(t);
        }
    }

    @Override
    public void run() {
        action.execute();

        // notify task observers
        notifyAll("Task " + this.getId() + ": " + getParam().message());
    }
}