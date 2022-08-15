package comp611.assignment1.connectfour.game;

import comp611.assignment1.connectfour.object.TaskObserver;

public class GameTaskObserver implements TaskObserver<String> {

    @Override
    public void update(String progress) {
        System.out.println(progress);
    }
}