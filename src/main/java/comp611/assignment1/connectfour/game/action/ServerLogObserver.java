package comp611.assignment1.connectfour.game.action;

import comp611.assignment1.connectfour.object.TaskObserver;

public class ServerLogObserver implements TaskObserver<String> {

    public ServerLogObserver() {

    }

    // task handling
    @Override
    public void update(String progress) {
        System.out.println(progress);
    }
}
