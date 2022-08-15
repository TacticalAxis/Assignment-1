package comp611.assignment1.connectfour.game.action;

import comp611.assignment1.connectfour.game.GameAction;

import java.io.PrintWriter;

public class ActionSendMessage implements GameAction {

    private final PrintWriter[] pws;
    private final String message;
    private final boolean inputRequired;

    public ActionSendMessage(String message, boolean inputRequired, PrintWriter... pws) {
        this.pws = pws;
        this.message = message;
        this.inputRequired = inputRequired;
    }

    public ActionSendMessage(String message, PrintWriter... pws) {
        this.pws = pws;
        this.message = message;
        this.inputRequired = false;
    }

    @Override
    public void execute() {
        for (PrintWriter printWriter : pws) {
            printWriter.println(message);
            if (inputRequired) {
                printWriter.println("_");
            }
            printWriter.println("\u0004");
            printWriter.flush();
        }
    }
}