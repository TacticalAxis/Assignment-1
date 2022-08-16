package comp611.assignment1.connectfour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectClient {
    // setup host and port to connect to
    public static final String HOST_NAME = "0.0.0.0";
    public static final int HOST_PORT = 7777;

    // hide public constructor
    private ConnectClient() {}

    public static void main(String[] args) {
        ConnectClient client = new ConnectClient();
        client.startClient();
    }

    public void startClient() {
        // setup user input scanner
        Scanner keyboardInput = new Scanner(System.in);

        // setup socket to connect to server
        try (Socket socket = new Socket(HOST_NAME, HOST_PORT)) {
            // setup input and output streams
            try (PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                boolean finished = false;
                do {
                    boolean inputRequired = false;

                    // to be used to concat server line data
                    StringBuilder sb = new StringBuilder();

                    // init line data
                    String line = br.readLine();

                    // if line null, game is over
                    if (line == null) {
                        System.out.println("\nGame Over.");
                        break;
                    }

                    // concat line data
                    while (!line.equals("\u0004")) {
                        if (line.trim().equals("_")) {
                            inputRequired = true;
                            line = br.readLine();
                            continue;
                        }

                        sb.append(line);
                        sb.append("\n");
                        line = br.readLine();

                        if (line == null) {
                            break;
                        }
                    }

                    // display line data
                    String toPrint = sb.toString();
                    System.out.print(toPrint);

                    // set finished to a value of true if game is over
                    if (toPrint.contains("Game Over")) {
                        finished = true;
                    } else {
                        if (inputRequired) {
                            pw.println(keyboardInput.nextLine());
                            pw.flush();
                        }
                    }
                } while (!finished);
            } catch (IOException e) {
                System.err.println("Client error with game: " + e);
            }
        } catch (IOException e) {
            System.err.println("Client could not make connection: " + e);
            System.exit(-1);
        }
    }
}