package pl.piterpti.view.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.piterpti.controller.Controller;

/**
 * Created by piter on 15.04.17.
 */
public class OpenScene {

    public Object start(Stage window, String view) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("views/" + view));
//        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/" + view));
        Scene scene =  new Scene(loader.load(), 600 ,400);
        window.setScene(scene);
        window.show();
        return loader.getController();
    }
}

