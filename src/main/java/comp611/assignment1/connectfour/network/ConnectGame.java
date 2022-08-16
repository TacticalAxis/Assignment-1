package comp611.assignment1.connectfour.network;

import comp611.assignment1.connectfour.game.*;
import comp611.assignment1.connectfour.game.action.ActionPlaceToken;
import comp611.assignment1.connectfour.game.action.ActionSendMessage;
import comp611.assignment1.connectfour.game.action.ServerLogObserver;
import comp611.assignment1.connectfour.object.TaskObserver;
import comp611.assignment1.connectfour.object.ThreadPool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("unchecked")
public class ConnectGame implements Runnable {

    private final Socket player1Sock;
    private final Socket player2Sock;

    private final ThreadPool threadPool;
    private final TaskObserver<String> logger;

    private Player player1;
    private Player player2;

    public ConnectGame(Socket player1Sock, Socket player2Sock, ThreadPool threadPool) {
        this.threadPool = threadPool;
        this.player1Sock = player1Sock;
        this.player2Sock = player2Sock;
        this.logger = new ServerLogObserver();
    }

    public void run() {

        ServerLogObserver listener = new ServerLogObserver();

        System.out.println("Game started with " + player1Sock.getInetAddress());

        try (PrintWriter pw1 = new PrintWriter(player1Sock.getOutputStream(), true);
             PrintWriter pw2 = new PrintWriter(player2Sock.getOutputStream(), true);
             BufferedReader br1 = new BufferedReader(new InputStreamReader(player1Sock.getInputStream()));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(player2Sock.getInputStream()))) {

            ConnectFour c4 = new ConnectFour(6, 7);

//            Player player1 = new Player(player1Sock, br1, pw1, PlayerType.CROSS);
//            Player player2 = new Player(player2Sock, br2, pw2, PlayerType.CIRCLE);

            this.player1 = new Player(player1Sock, br1, pw1, PlayerType.CROSS);
            this.player2 = new Player(player2Sock, br2, pw2, PlayerType.CIRCLE);

            Player currentPlayer = player1;

            threadPool.perform(new GameTask(new ActionSendMessage("Welcome to Connect Four!", player1.getOut(), player2.getOut()), listener));

            do {
                GameState state = next(currentPlayer, c4);

                if (state == GameState.INVALID) {
                    continue;
                }

//                if (state == GameState.FINISHED) {
//                    System.out.println("ITS GETTING HERE!");
////                        threadPool.perform(new GameTask(new ActionSendMessage(c4.hasWon().getLabel() + " has won the game!", player2.getOut(), player1.getOut()), listener));
//                }

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
        threadPool.perform(new GameTask(new ActionSendMessage(c4.displayBoard(), player.getOut()), logger));
        threadPool.perform(new GameTask(new ActionSendMessage(player.getPlayerType().getLabel() + ", please enter your move:", true, player.getOut()), logger));

        // read input from client
        String move = player.getIn().readLine().trim();
        int moveInt = InputParser.parsePositiveInt(move, 1, c4.getWidth());

        if (c4.isValidMove(moveInt)) {
            threadPool.perform(new GameTask(new ActionPlaceToken(c4, player.getPlayerType(), moveInt), logger));
        } else {
            threadPool.perform(new GameTask(new ActionSendMessage("Invalid move!", player.getOut()), logger));
            return GameState.INVALID;
        }

        if (c4.hasWon() != null) {
            String winMessage = c4.hasWon().getLabel() + " has won the game!";
            threadPool.perform(new GameTask(new ActionSendMessage(winMessage, player1.getOut()), logger));
            threadPool.perform(new GameTask(new ActionSendMessage(winMessage, player2.getOut()), logger));
            return GameState.FINISHED;
        }

        threadPool.perform(new GameTask(new ActionSendMessage("Token Placed!\n" + c4.displayBoard() + "\nWaiting for opponent...", player.getOut()), logger));

        return GameState.CONTINUE;
    }
}

