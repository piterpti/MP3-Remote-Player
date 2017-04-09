package pl.piterpti.flow;

import pl.piterpti.controller.Actions;
import pl.piterpti.tools.Mp3Player;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3PlayerFlow extends Flow {

    @SuppressWarnings("unused")
    public static String FLOW_NAME = "Mp3PlayerFlow";
    private Mp3Player mp3Player;

    public Mp3PlayerFlow() {
        super();
        SCREEN_NAME = "MainScreen";
        mp3Player = new Mp3Player();
    }

    @Override
    public void run() {
        logger.info("Run method");
    }

    @Override
    public void handleAction(long actionId) {
        super.handleAction(actionId);
        if (actionId == Actions.PLAY_MUSIC) {
            mp3Player.play();

        }
    }
}
