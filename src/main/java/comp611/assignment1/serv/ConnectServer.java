package comp611.assignment1.serv;

import comp611.assignment1.game.ConnectFour;
import comp611.assignment1.game.PlayerType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectServer {
    public static final int PORT = 7777;
    private boolean stopRequested;

    public ConnectServer() {
        stopRequested = false;
    }

    public static void main(String[] args) {
        ConnectServer server = new ConnectServer();
        server.startServer();
    }

    public void startServer() {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started at " + InetAddress.getLocalHost() + " on port " + PORT);
            while(!stopRequested) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection made with " + socket.getInetAddress());
                ConnectGame game = new ConnectGame(socket);
                Thread thread = new Thread(game);
                thread.start();

                if(stopRequested) {
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

    public void requestStop() {
        stopRequested = true;
    }

    private class ConnectGame implements Runnable {

        private final Socket socket;
        public ConnectGame(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            System.out.println("Game started with " + socket.getInetAddress());
            try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), false);
                 BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {  // create an autoflush output stream for the socket

                ConnectFour c4 = new ConnectFour(6, 7);

                PlayerType server = PlayerType.CIRCLE;
                PlayerType client = PlayerType.CROSS;

                do {
                    if(c4.hasWon() != null) {
                        requestStop();
                        break;
                    }

                    pw.println(c4.displayBoard());
                    pw.println("Enter your move: ");
                    pw.println("\u0004");
                    pw.flush();

                    String move = br.readLine().trim();
                    int moveInt = InputParser.parsePositiveInt(move, 1, c4.getWidth());

                    if(moveInt == -1) {
                        pw.println("Invalid move");
                        pw.println("\u0004");
                        pw.flush();
                        continue;
                    }

                    c4.dropToken(moveInt, client);

                    if (c4.hasWon() != null) {
                        pw.println(c4.displayBoard());
                        pw.println("Game over! " + (c4.hasWon() == PlayerType.CROSS ? "You win!" : "You lose!"));
                        pw.println("\u0004");
                        pw.flush();
                        continue;
                    }

                    c4.dropToken(c4.getRandomColumn(), server);
                    pw.flush();

                } while (!c4.gameOver());

            } catch (IOException e) {
                System.err.println("Client error with game: " + e);
            } finally {
                try {
                    System.out.println("Closing connection with " + socket.getInetAddress());
                    socket.close();
                } catch (IOException e) {
                    System.err.println("Can't close socket: " + e);
                }
            }
        }
    }
}