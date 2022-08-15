package comp611.assignment1.connectfour.game.action;

import comp611.assignment1.connectfour.object.TaskObserver;

public class ServerJoinListener implements TaskObserver<ServerJoinTask> {

    private final boolean found;

    public ServerJoinListener(boolean found) {
        this.found = found;
    }

    public boolean isFound() {
        return found;
    }

    public void update(ServerJoinTask progress) {
//        progress.execute();
    }
}