package pl.piterpti.controller;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import pl.piterpti.flow.Flow;
import pl.piterpti.flow.FlowArgs;
import pl.piterpti.gui.screen.EmptyScreen;
import pl.piterpti.gui.screen.ScreenDefinition;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by piter on 09.04.17.
 */
public class Controller implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass());
    private ApplicationContext context;

    private List<Class <? extends Flow>> registeredFlows;
    private ScreenDefinition screenDefinition;
    private ConcurrentLinkedQueue<Action> actionsToDo = new ConcurrentLinkedQueue<>();
    private Flow currentFlow;
    private Object lock = new Object();

    public Controller(List<Class <? extends Flow>> registeredFlows) {
        this.registeredFlows = registeredFlows;
        screenDefinition = new ScreenDefinition();
        logRegisteredFlows();
        logRegisteredScreen();
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
            EmptyScreen screen = (EmptyScreen) context.getBean(currentFlow.SCREEN_NAME);
            if (currentFlow == null || screen == null) {
                logger.error("Calling flow " + flowName + " error");
                return;
            }
            screen.setController(this);
            currentFlow.runScreen(screen);
            Thread t = new Thread(currentFlow);
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

    private void logRegisteredScreen() {
        logger.info("Registered screens:");
        for (Map.Entry<String, Class<? extends EmptyScreen>> entry : screenDefinition.getScreens().entrySet()) {
            logger.info(entry.getKey() + " - " + entry.getValue());
        }
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public void doAction(long actionId) {
        actionsToDo.add(new Action(actionId));
        synchronized (lock) {
            lock.notify();
        }
    }

    public void doAction(long actionId, FlowArgs args) {
        actionsToDo.add(new Action(actionId, args));
        synchronized (lock) {
            lock.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            while (!actionsToDo.isEmpty()) {
                currentFlow.handleAction(actionsToDo.poll());
            }

            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }

        }
    }
}
