package pl.piterpti.flow;

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import pl.piterpti.controller.Action;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Flow definition
 */
public abstract class Flow implements Runnable {

    protected Logger logger = Logger.getLogger(this.getClass());
    public String SCREEN_NAME = "EmptyScreen";
    protected Controller controller;
    protected boolean runFlag = true;
    protected ConcurrentLinkedQueue<Action> flowActions = new ConcurrentLinkedQueue<>();

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
        logger.debug("Adding action to queue " + action.getId() + " - " + Actions.resolveActionName(action.getId()));
        flowActions.add(action);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract void closeFlow();
}
