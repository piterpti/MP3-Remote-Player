package pl.piterpti.flow;

import pl.piterpti.communication.RemoteHost;
import pl.piterpti.controller.Action;
import pl.piterpti.controller.Actions;
import pl.piterpti.gui.screen.EmptyScreen;
import pl.piterpti.tools.Mp3Player;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3PlayerFlow extends Flow {

    public static final String ARG_SONG_LIST = "songList";
    public static final String ARG_CURRENT_SONG = "currentSong";
    public static final String ARG_CUSTOM_SONG = "customSong";

    @SuppressWarnings("unused")
    public static String FLOW_NAME = "Mp3PlayerFlow";
    private Mp3Player mp3Player;
    private RemoteHost host;

    public Mp3PlayerFlow() {
        super();
        SCREEN_NAME = "MainScreen";
        mp3Player = new Mp3Player();
        host = new RemoteHost(8888);
        host.start();
    }

    @Override
    public void run() {
        logger.info("Run method");
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
            mp3Player.play(true);
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

    private void loadMP3Files() {
        LinkedList<File> mp3Files = new LinkedList<>();
        listFiles("mp3", mp3Files);

        for (File mp3: mp3Files) {
            logger.info("Adding: " + mp3.getName());
            mp3Player.addFile(mp3);
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
