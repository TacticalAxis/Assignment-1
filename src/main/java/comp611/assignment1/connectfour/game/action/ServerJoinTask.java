package comp611.assignment1.connectfour.game.action;

//public class ServerJoinTask {
//
//}

import comp611.assignment1.connectfour.object.Task;

import java.net.Socket;

public class ServerJoinTask extends Task<Socket, ServerJoinTask> {

    private final Socket socket;

    public ServerJoinTask(Socket socket) {
        super(socket);
        this.socket = socket;
    }

    @Override
    public void run() {
        while (true) {
            // try to open a socket to the server
            try {
                socket.getOutputStream().write(("join " + socket.getLocalPort()).getBytes());
                break;
            } catch (Exception e) {
                // wait for a while and try again
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
