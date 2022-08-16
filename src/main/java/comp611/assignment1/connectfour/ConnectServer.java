package comp611.assignment1.connectfour;

import comp611.assignment1.connectfour.app.ConnectGame;
import comp611.assignment1.connectfour.task.ThreadPool;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectServer {

    // listen for incoming connections on port
    public static final int PORT = 7777;

    // local thread pool instance
    private final ThreadPool threadPool;

    // stop flag
    private boolean stopRequested;

    public ConnectServer() {
        stopRequested = false;

        // initialise thread pool
        this.threadPool = new ThreadPool(2);
    }

    public static void main(String[] args) {
        ConnectServer server = new ConnectServer();
        server.startServer();
    }

    public void startServer() {
        // create server socket, bind to port and listen for incoming connections
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
            while (!stopRequested) {
                // accept incoming connection
                Socket socket1 = serverSocket.accept();
                System.out.println("Player 1 connected @ " + socket1.getInetAddress());

                // accept incoming connection
                Socket socket2 = serverSocket.accept();
                System.out.println("Player 2 connected @ " + socket2.getInetAddress());

                // once two players are connected, start a new game
                ConnectGame game = new ConnectGame(socket1, socket2, threadPool);

                // start game thread
                Thread thread = new Thread(game);
                thread.start();

                // wait for game thread to finish
                if (stopRequested) {
                    System.out.println("Game over");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Server can't listen on port: " + e);
            System.exit(-1);
        }

        // shutdown thread pool and finish up
        System.out.println("Server finishing");
        threadPool.destroyPool();
    }

    // set stop flag
    public void setStopRequested(boolean stopRequested) {
        this.stopRequested = stopRequested;
    }
}