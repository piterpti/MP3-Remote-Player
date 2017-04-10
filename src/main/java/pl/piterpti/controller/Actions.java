package pl.piterpti.controller;

import java.util.HashMap;

/**
 * Created by piter on 09.04.17.
 */
public class Actions {

    public static final long PLAY_MUSIC = 1L;
    public static final long STOP_MUSIC = 2L;
    public static final long PAUSE_MUSIC = 3L;
    public static final long NEXT_MUSIC = 4L;
    public static final long PREV_MUSIC = 5L;
    public static final long CUSTOM_MUSIC = 6L;
    public static final long CUSTOM_MUSIC_BY_NAME = 7L;


    public static HashMap<Long, String> actionNames = new HashMap<>();

    static {
        actionNames.put(PLAY_MUSIC, "playMusic");
        actionNames.put(STOP_MUSIC, "stopMusic");
        actionNames.put(PAUSE_MUSIC, "pauseMusic");
        actionNames.put(NEXT_MUSIC, "nextMusic");
        actionNames.put(PREV_MUSIC, "prevMusic");
        actionNames.put(CUSTOM_MUSIC, "customMusic");
        actionNames.put(CUSTOM_MUSIC_BY_NAME, "customMusicByName");
    }

    public static String resolveActionName(long actionId) {
        return actionNames.get(actionId);
    }

}
