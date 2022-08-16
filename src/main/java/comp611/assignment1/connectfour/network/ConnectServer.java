package comp611.assignment1.connectfour.network;

import comp611.assignment1.connectfour.object.ThreadPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectServer {
    public static final int PORT = 7777;
    private final ThreadPool threadPool;
    private boolean stopRequested;

    public ConnectServer() {
        stopRequested = false;
        this.threadPool = new ThreadPool(2);
    }

    public static void main(String[] args) {
        ConnectServer server = new ConnectServer();
        server.startServer();
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
            while (!stopRequested) {
                Socket socket1 = serverSocket.accept();
                System.out.println("Player 1 connected @ " + socket1.getInetAddress());

                // send waiting message to player 1
//                try (PrintWriter pw = new PrintWriter(socket1.getOutputStream(), true)) {
//                    threadPool.perform(new GameTask(new ActionSendMessage("Waiting for player!", pw)));
//                }

                Socket socket2 = serverSocket.accept();
                System.out.println("Player 2 connected @ " + socket2.getInetAddress());

                ConnectGame game = new ConnectGame(socket1, socket2, threadPool);

                Thread thread = new Thread(game);
                thread.start();

                if (stopRequested) {
                    System.out.println("Game over");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Server can't listen on port: " + e);
            System.exit(-1);
        }

        System.out.println("Server finishing");
    }

    public void setStopRequested(boolean stopRequested) {
        this.stopRequested = stopRequested;
    }
}