package pl.piterpti.flow;

import org.apache.log4j.Logger;
import pl.piterpti.controller.Controller;
import pl.piterpti.gui.screen.EmptyScreen;
import sun.awt.X11.Screen;

/**
 * Created by piter on 09.04.17.
 */
public abstract class Flow implements Runnable {

    protected Logger logger = Logger.getLogger(this.getClass());
    protected boolean runFlow = true;
    public String SCREEN_NAME = "EmptyScreen";
    protected Controller controller;
    protected EmptyScreen screen;

    public Flow() {

    }

    public void run() {

    }

    public void runScreen(EmptyScreen screen) {
        if (SCREEN_NAME == null) {
            throw new RuntimeException("Null screen name value");
        }

        this.screen = screen;
        this.screen.setVisible(true);
        controller = screen.getController();
    }

    public void handleAction(long actionId) {
        logger.info("Action " + actionId);
    }

}
