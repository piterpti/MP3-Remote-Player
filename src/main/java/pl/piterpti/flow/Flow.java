package pl.piterpti.flow;

import org.apache.log4j.Logger;
import pl.piterpti.gui.screen.EmptyScreen;

/**
 * Created by piter on 09.04.17.
 */
public abstract class Flow implements Runnable {

    protected Logger logger = Logger.getLogger(this.getClass());
    protected boolean runFlow = true;
    public String SCREEN_NAME = "EmptyScreen";

    public Flow() {

    }

    public void run() {

    }

    public void runScreen(EmptyScreen screen) {
        if (SCREEN_NAME == null) {
            throw new RuntimeException("Null screen name value");
        }
        screen.setVisible(true);
    }

    public void handleAction(long actionId) {
        logger.info("Action " + actionId);
    }
}
