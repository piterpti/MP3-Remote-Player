package pl.piterpti.view.controller;

import pl.piterpti.controller.Controller;

/**
 * Controller contains flow controller
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
