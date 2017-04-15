package pl.piterpti.flow;

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import pl.piterpti.controller.Action;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.gui.screen.EmptyScreen;

/**
 * Created by piter on 09.04.17.
 */
public abstract class Flow implements Runnable {

    protected Logger logger = Logger.getLogger(this.getClass());
    public String SCREEN_NAME = "EmptyScreen";
    protected Controller controller;

    public Flow() {

    }

    @Override
    public void run() {

    }

    public void runScreen(Stage stage) {
        if (stage == null) {
            throw new RuntimeException("Stage can not be null!");
        }

        init();
    }

    protected abstract void init();

    public void handleAction(Action action) {
        logger.info("Action " + action.getId() + " - " + Actions.resolveActionName(action.getId()));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
