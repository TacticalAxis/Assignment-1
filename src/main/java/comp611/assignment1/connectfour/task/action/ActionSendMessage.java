package comp611.assignment1.connectfour.task.action;

import java.io.PrintWriter;

public class ActionSendMessage implements GameAction {

    private final PrintWriter[] pws;
    private final String message;
    private final boolean inputRequired;

    private String retMessage;

    public ActionSendMessage(String message, boolean inputRequired, PrintWriter... pws) {
        this.pws = pws;
        this.message = message;
        this.inputRequired = inputRequired;

        this.retMessage = null;
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
        this.retMessage = "Successfully sent to client" + (pws.length > 1 ? "s" : "");
    }

    @Override
    public String message() {
        return retMessage;
    }
}