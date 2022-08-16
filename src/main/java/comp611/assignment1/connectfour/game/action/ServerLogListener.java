package comp611.assignment1.connectfour.game.action;

import comp611.assignment1.connectfour.object.TaskObserver;

public class ServerLogListener implements TaskObserver<String> {

    public ServerLogListener() {

    }

    @Override
    public void update(String progress) {
        System.out.println(progress);
    }
}
