package pl.piterpti.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.Mp3PlayerFlow;

/**
 * Main player class
 */
public class Main extends Application {

    private static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        logger.info("App starting");

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/empty.fxml"));
        Parent root = loader.load();
        stage.setTitle("Mp3 remote player");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();

        stage.setOnCloseRequest(event -> System.exit(0));

        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        Controller controller = context.getBean("controller", Controller.class);
        controller.setContext(context);
        controller.setStage(stage);
        Thread executor = new Thread(controller);
        executor.setName("MainController");
        executor.start();
        controller.callFlow(Mp3PlayerFlow.class);
    }
}
