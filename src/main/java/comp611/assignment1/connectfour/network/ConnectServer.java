package comp611.assignment1.connectfour.network;

import comp611.assignment1.connectfour.game.*;
import comp611.assignment1.connectfour.game.action.ActionPlaceToken;
import comp611.assignment1.connectfour.game.action.ActionSendMessage;
import comp611.assignment1.connectfour.object.ThreadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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

                ConnectGame game = new ConnectGame(socket1, socket2);

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

    public void requestStop() {
        stopRequested = true;
    }

    private class ConnectGame implements Runnable {

        private final Socket player1Sock;
        private final Socket player2Sock;

        public ConnectGame(Socket player1Sock, Socket player2Sock) {
            this.player1Sock = player1Sock;
            this.player2Sock = player2Sock;
        }

        public void run() {
            System.out.println("Game started with " + player1Sock.getInetAddress());

            try (PrintWriter pw1 = new PrintWriter(player1Sock.getOutputStream(), true);
                 PrintWriter pw2 = new PrintWriter(player2Sock.getOutputStream(), true);
                 BufferedReader br1 = new BufferedReader(new InputStreamReader(player1Sock.getInputStream()));
                 BufferedReader br2 = new BufferedReader(new InputStreamReader(player2Sock.getInputStream()))) {

                ConnectFour c4 = new ConnectFour(6, 7);

                Player player1 = new Player(player1Sock, br1, pw1, PlayerType.CROSS);
                Player player2 = new Player(player2Sock, br2, pw2, PlayerType.CIRCLE);

                Player currentPlayer = player1;

                threadPool.perform(new GameTask(new ActionSendMessage("Welcome to Connect Four!", player1.getOut(), player2.getOut())));

                do {
                    GameState state = next(currentPlayer, c4);

                    if (state == GameState.INVALID) {
                        continue;
                    }

                    if (state == GameState.FINISHED) {
                        threadPool.perform(new GameTask(new ActionSendMessage(c4.hasWon().getLabel() + " has won the game!", player1.getOut(), player2.getOut())));
                    }

                    currentPlayer = currentPlayer == player1 ? player2 : player1;
                } while (!c4.gameOver());
            } catch (IOException e) {
                System.err.println("Game error: " + e);
            } finally {
                try {
                    System.out.println("Closing connection with " + player1Sock.getInetAddress());
                    player1Sock.close();
                } catch (IOException e) {
                    System.err.println("Can't close socket: " + e);
                }
            }
        }

        private GameState next(Player player, ConnectFour c4) throws IOException {
            threadPool.perform(new GameTask(new ActionSendMessage(c4.displayBoard(), player.getOut())));
            threadPool.perform(new GameTask(new ActionSendMessage(player.getPlayerType().getLabel() + ", please enter your move:", true, player.getOut())));

            // read input from client
            String move = player.getIn().readLine().trim();
            int moveInt = InputParser.parsePositiveInt(move, 1, c4.getWidth());

            if (c4.isValidMove(moveInt)) {
                threadPool.perform(new GameTask(new ActionPlaceToken(c4, player.getPlayerType(), moveInt)));
            } else {
                threadPool.perform(new GameTask(new ActionSendMessage("Invalid move!", player.getOut())));
                return GameState.INVALID;
            }

            if (c4.hasWon() != null) {
                requestStop();
                return GameState.FINISHED;
            }

            threadPool.perform(new GameTask(new ActionSendMessage("Token Placed!\n"+c4.displayBoard()+"\nWaiting for opponent...", player.getOut())));

            return GameState.CONTINUE;
        }
    }
}