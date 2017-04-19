package pl.piterpti.controller;

import java.util.HashMap;

/**
 * Actions for player
 */
public class Actions {

    public static final long PLAY_MUSIC = 1L;
    public static final long STOP_MUSIC = 2L;
    public static final long PAUSE_MUSIC = 3L;
    public static final long NEXT_MUSIC = 4L;
    public static final long PREV_MUSIC = 5L;
    public static final long CUSTOM_MUSIC = 6L;
    public static final long CUSTOM_MUSIC_BY_NAME = 7L;
    public static final long PLAY_MUSIC_BY_NAME = 8L;
    public static final long SET_VOLUME = 9L;
    public static final long SET_VOLUME_REMOTE = 10L;
    public static final long REFRESH_PLAYLIST = 11L;
    public static final long APP_CLOSED = 12L;


    private static HashMap<Long, String> actionNames = new HashMap<>();

    static {
        actionNames.put(PLAY_MUSIC, "playMusic");
        actionNames.put(STOP_MUSIC, "stopMusic");
        actionNames.put(PAUSE_MUSIC, "pauseMusic");
        actionNames.put(NEXT_MUSIC, "nextMusic");
        actionNames.put(PREV_MUSIC, "prevMusic");
        actionNames.put(CUSTOM_MUSIC, "customMusic");
        actionNames.put(CUSTOM_MUSIC_BY_NAME, "customMusicByName");
        actionNames.put(PLAY_MUSIC_BY_NAME, "playMusicByName");
        actionNames.put(SET_VOLUME, "setVolume");
        actionNames.put(SET_VOLUME_REMOTE, "setVolumeRemote");
        actionNames.put(REFRESH_PLAYLIST, "refreshPlaylist");
        actionNames.put(APP_CLOSED, "appClosed");
    }

    public static String resolveActionName(long actionId) {
        return actionNames.get(actionId);
    }

}
