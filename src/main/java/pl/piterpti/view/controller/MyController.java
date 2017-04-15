package pl.piterpti.view.controller;

import pl.piterpti.controller.Controller;

/**
 * Created by piter on 15.04.17.
 */
public abstract class MyController {

    protected Controller controller;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
