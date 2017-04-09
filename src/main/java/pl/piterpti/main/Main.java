package pl.piterpti.main;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.Mp3PlayerFlow;

import javax.swing.*;

/**
 * Created by piter on 09.04.17.
 */
public class Main {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            logger.info("App started");
            ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");

            Controller controller = context.getBean("controller", Controller.class);
            controller.setContext(context);
            Thread executor = new Thread(controller);
            executor.start();
            controller.callFlow(Mp3PlayerFlow.class);
        });
    }
}
