package comp611.assignment1.connectfour.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConnectClient {
    public static final String HOST_NAME = "0.0.0.0";
    public static final int HOST_PORT = 7777; // host port number

    private ConnectClient() {}

    public static void main(String[] args) {
        ConnectClient client = new ConnectClient();
        client.startClient();
    }

    public void startClient() {
        Scanner keyboardInput = new Scanner(System.in);

        try (Socket socket = new Socket(HOST_NAME, HOST_PORT)) {
            try(PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                boolean finished = false;
                do {
                    boolean inputRequired = false;
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    if (line == null) {
                        System.out.println("Server closed connection");
                        break;
                    }

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

                    String toPrint = sb.toString();

                    System.out.print(toPrint);
//                    System.out.println(inputRequired);

                    if (toPrint.contains("Game Over!")) {
                        finished = true;
                    } else {
                        if(inputRequired) {
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