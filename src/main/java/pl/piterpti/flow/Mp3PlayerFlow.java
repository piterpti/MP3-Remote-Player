package pl.piterpti.flow;

import pl.piterpti.communication.RemoteHost;
import pl.piterpti.controller.Action;
import pl.piterpti.controller.Actions;
import pl.piterpti.gui.screen.EmptyScreen;
import pl.piterpti.gui.screen.componenet.Audio;
import pl.piterpti.tools.Mp3Player;
import pl.piterpti.tools.Mp3PlayerJLayer;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3PlayerFlow extends Flow {

    public static final String ARG_SONG_LIST = "songList";
    public static final String ARG_CURRENT_SONG = "currentSong";
    public static final String ARG_CUSTOM_SONG = "customSong";
    public static final String ARG_HOST_SONG = "hostSong";
    public static final String ARG_ADD_SONG = "addSong";
    public static final String ARG_PLAY_SONG_BY_NAME = "playSongByName";
    public static final String ARG_VOLUME = "volume";

    @SuppressWarnings("unused")
    public static String FLOW_NAME = "Mp3PlayerFlow";
    private Mp3Player mp3Player;
    private RemoteHost host;

    public Mp3PlayerFlow() {
        super();
        SCREEN_NAME = "MainScreen";
        host = new RemoteHost(8888);
        host.setDaemon(true);
    }

    @Override
    public void run() {

    }

    @Override
    public void handleAction(Action action) {
        super.handleAction(action);
        long actionId = action.getId();
        if (actionId == Actions.PLAY_MUSIC) {
            mp3Player.play(true);
            refreshList();
        } else if (actionId == Actions.STOP_MUSIC) {
            mp3Player.stop();
        } else if (actionId == Actions.PAUSE_MUSIC) {
            mp3Player.pause();
        } else if (actionId == Actions.NEXT_MUSIC) {
            mp3Player.next();
            refreshList();
        } else if (actionId == Actions.PREV_MUSIC) {
            mp3Player.prev();
            refreshList();
        } else if (actionId == Actions.CUSTOM_MUSIC) {
            int currSong = (int) action.getArg().getArgs().get(ARG_CUSTOM_SONG);
            mp3Player.setCurrentSong(currSong);
            mp3Player.stop();
            mp3Player.play(true);
        } else if (actionId == Actions.CUSTOM_MUSIC_BY_NAME) {
            String path = (String) action.getArg().getArgs().get(ARG_HOST_SONG);
            mp3Player.add(new File(path));
            mp3Player.setCurrentSong(mp3Player.getSongsFileList().size() - 1);
            controller.doAction(Actions.STOP_MUSIC);
            controller.doAction(Actions.PLAY_MUSIC);
            FlowArgs args = new FlowArgs();
            args.addArg(ARG_ADD_SONG, path);
            screen.refresh(args);
        } else if (actionId == Actions.PLAY_MUSIC_BY_NAME) {
            String name = (String) action.getArg().getArgs().get(ARG_PLAY_SONG_BY_NAME);
            mp3Player.findSongByName(name);
            mp3Player.stop();
            mp3Player.play(true);
            refreshList();
        } else if (actionId == Actions.SET_VOLUME) {
            int val = (int) action.getArg().getArgs().get(ARG_VOLUME);
            float valF = val;
            valF /= 100;
            logger.info("Volume set to " + valF);
            Audio.setMasterOutputVolume(valF);
        }
    }

    private void refreshList() {
        FlowArgs args = new FlowArgs();
        args.addArg(ARG_CURRENT_SONG, mp3Player.getCurrentSong());
        screen.refresh(args);
    }

    @Override
    public void runScreen(EmptyScreen screen) {
        super.runScreen(screen);
        FlowArgs args = new FlowArgs();
        loadMP3Files();
        args.addArg(ARG_SONG_LIST, mp3Player.getSongsFileList());
        screen.refresh(args);
    }

    @Override
    protected void init() {
        mp3Player = new Mp3PlayerJLayer(controller);
        host.setController(controller);
        host.setSongsList(mp3Player.getSongsFileList());
        host.start();
    }

    private void loadMP3Files() {
        LinkedList<File> mp3Files = new LinkedList<>();
        listFiles("mp3", mp3Files);

        for (File mp3: mp3Files) {
            logger.info("Adding: " + mp3.getName());
            mp3Player.add(mp3);
        }
    }

    private void listFiles(String directoryName, LinkedList<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile() && file.getName().endsWith("mp3")) {
                files.add(file);
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }
    }


}
