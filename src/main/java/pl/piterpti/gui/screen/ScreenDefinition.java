package pl.piterpti.gui.screen;

import java.util.HashMap;

/**
 * Created by piter on 09.04.17.
 */
public class ScreenDefinition {

    HashMap<String, Class<?extends EmptyScreen>> screens;

    public ScreenDefinition() {
        screens = new HashMap<String, Class<? extends EmptyScreen>>();
        registerScreen(MainScreen.class.getName(), MainScreen.class);
    }

    private void registerScreen(String name, Class<? extends EmptyScreen> screenClass) {
        screens.put(name, screenClass);
    }

    public HashMap<String, Class<? extends EmptyScreen>> getScreens() {
        return screens;
    }
}
