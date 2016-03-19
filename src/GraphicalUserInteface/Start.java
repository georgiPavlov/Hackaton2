package GraphicalUserInteface;

import comunication.ComunicationStream;
import comunication.StateManager;
import comunication.StatePakage;

/**
 * Created by georgipavlov on 18.03.16.
 */
public class Start {
    public static void main(String[] args) {
        ComunicationStream<StatePakage> commLine = new ComunicationStream<>();
        StateManager stateManager = new StateManager(commLine);
        GraphicUserInterface Interface = new GraphicUserInterface(commLine);

        Thread thread = new Thread(stateManager);
        thread.start();
        Interface.start();
        try {
            Interface.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            thread.interrupt();
        }
    }
}
