package comp611.assignment1.connectfour.app;

import comp611.assignment1.connectfour.observer.ServerLogObserver;
import comp611.assignment1.connectfour.observer.TaskObserver;
import comp611.assignment1.connectfour.task.GameTask;
import comp611.assignment1.connectfour.task.ThreadPool;
import comp611.assignment1.connectfour.task.action.ActionPlaceToken;
import comp611.assignment1.connectfour.task.action.ActionSendMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@SuppressWarnings("unchecked")
public class ConnectGame implements Runnable {

    // setup sockets
    private final Socket player1Sock;
    private final Socket player2Sock;

    // setup local thread pool instance (injected)
    private final ThreadPool threadPool;
    private final TaskObserver<String> logger;

    // setup players
    private Player player1;
    private Player player2;

    public ConnectGame(Socket player1Sock, Socket player2Sock, ThreadPool threadPool) {
        this.threadPool = threadPool;
        this.player1Sock = player1Sock;
        this.player2Sock = player2Sock;
        this.logger = new ServerLogObserver();
    }

    public void run() {
        System.out.println("Game started with " + player1Sock.getInetAddress());

        // setup player socket input and output streams
        try (PrintWriter pw1 = new PrintWriter(player1Sock.getOutputStream(), true);
             PrintWriter pw2 = new PrintWriter(player2Sock.getOutputStream(), true);
             BufferedReader br1 = new BufferedReader(new InputStreamReader(player1Sock.getInputStream()));
             BufferedReader br2 = new BufferedReader(new InputStreamReader(player2Sock.getInputStream()))) {

            // init connect game
            ConnectFour c4 = new ConnectFour(6, 7);

            // setup players
            this.player1 = new Player(player1Sock, br1, pw1, PlayerType.CROSS);
            this.player2 = new Player(player2Sock, br2, pw2, PlayerType.CIRCLE);

            // setup current player (for switching turns)
            Player currentPlayer = player1;

            // send welcome message to players
            threadPool.perform(new GameTask(new ActionSendMessage("Welcome to Connect Four!", player1.getOut(), player2.getOut()), logger));

            do {
                // get state based on the actions of the next turn
                GameState state = next(currentPlayer, c4);

                // check state
                if (state == GameState.INVALID) {
                    continue;
                }

                // switch current player
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
        // display board to player and get input
        threadPool.perform(new GameTask(new ActionSendMessage(c4.displayBoard(), player.getOut()), logger));
        threadPool.perform(new GameTask(new ActionSendMessage(player.getPlayerType().getLabel() + ", please enter your move:", true, player.getOut()), logger));

        // read input from client
        String move = player.getIn().readLine().trim();
        int moveInt = InputParser.parsePositiveInt(move, 1, c4.getWidth());

        // check if move was valid
        if (c4.isValidMove(moveInt)) {
            threadPool.perform(new GameTask(new ActionPlaceToken(c4, player.getPlayerType(), moveInt), logger));
        } else {
            threadPool.perform(new GameTask(new ActionSendMessage("Invalid move!", player.getOut()), logger));
            return GameState.INVALID;
        }

        // check if game was won
        if (c4.hasWon() != null) {
            String winMessage = c4.displayBoard() + "\n[ " + c4.hasWon().getLabel() + " ] has won the game!";
            threadPool.perform(new GameTask(new ActionSendMessage(winMessage, player2.getOut(), player1.getOut()), logger));
            return GameState.FINISHED;
        }

        // send the waiting message to the player, if the game is not over
        threadPool.perform(new GameTask(new ActionSendMessage("Token Placed!\n" + c4.displayBoard() + "\nWaiting for opponent...", player.getOut()), logger));

        return GameState.CONTINUE;
    }
}