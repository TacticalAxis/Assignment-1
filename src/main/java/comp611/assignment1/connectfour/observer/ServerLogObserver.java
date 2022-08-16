package comp611.assignment1.connectfour.observer;

public class ServerLogObserver implements TaskObserver<String> {

    public ServerLogObserver() {
        // nothing to be constructed here
    }

    // task handling
    @Override
    public void update(String progress) {
        System.out.println(progress);
    }
}
