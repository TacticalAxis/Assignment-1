package comp611.assignment1.connectfour.app;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Player {

    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private final PlayerType playerType;

    public Player(Socket socket, BufferedReader in, PrintWriter out, PlayerType playerType) {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.playerType = playerType;
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public PlayerType getPlayerType() {
        return playerType;
    }
}
