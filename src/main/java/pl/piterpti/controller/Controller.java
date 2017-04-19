package pl.piterpti.controller;

import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import pl.piterpti.flow.Flow;
import pl.piterpti.message.FlowArgs;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by piter on 09.04.17.
 */
public class Controller implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass());
    private ApplicationContext context;

    private List<Class <? extends Flow>> registeredFlows;
    private ConcurrentLinkedQueue<Action> actionsToDo = new ConcurrentLinkedQueue<>();
    private Flow currentFlow;
    private final Object lock = new Object();
    private Stage stage;

    public Controller(List<Class <? extends Flow>> registeredFlows) {
        this.registeredFlows = registeredFlows;
        logRegisteredFlows();
    }

    public void callFlow(Class <? extends Flow> flowClass) {
        try {
            Field field = flowClass.getField("FLOW_NAME");
            String flowName = (String) field.get(null);
            if (flowName == null) {
                logger.error("Null flowName - can not run flow");
                return;
            }

            currentFlow = context.getBean(flowName, flowClass);
            currentFlow.setController(this);
            currentFlow.runScreen(stage);
            Thread t = new Thread(currentFlow);
            t.setName(currentFlow.SCREEN_NAME);
            t.start();

            logger.info("Starting flow " + flowName);

        } catch (Exception e) {
            logger.error("Exception when calling flow: " + e.getMessage());
        }
    }

    private void logRegisteredFlows() {
        logger.info("Registered flows:");
        for (Class c : registeredFlows) {
            logger.info(c.getName());
        }
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void doAction(long actionId) {
        actionsToDo.add(new Action(actionId));
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void doAction(long actionId, FlowArgs args) {
        actionsToDo.add(new Action(actionId, args));
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void closeApp() {
        currentFlow.closeFlow();
        System.exit(0);
    }

    @Override
    public void run() {
        while (true) {
            while (!actionsToDo.isEmpty()) {
                Action currentAction = actionsToDo.poll();
                if (currentAction.getId() == Actions.APP_CLOSED) {
                    closeApp();
                }
                currentFlow.handleAction(currentAction);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }

            synchronized (lock) {
                try {
                    lock.wait(100);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }

        }
    }
}
