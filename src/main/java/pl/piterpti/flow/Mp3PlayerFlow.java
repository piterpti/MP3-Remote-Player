package pl.piterpti.flow;

import javafx.stage.Stage;
import pl.piterpti.communication.ConnectedHosts;
import pl.piterpti.communication.RemoteHost;
import pl.piterpti.controller.Action;
import pl.piterpti.controller.Actions;
import pl.piterpti.message.FlowArgs;
import pl.piterpti.tools.Mp3Player;
import pl.piterpti.tools.Mp3PlayerFx;
import pl.piterpti.tools.Toolkit;
import pl.piterpti.view.controller.Mp3PlayerController;
import pl.piterpti.view.controller.OpenScene;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Properties;

/**
 * Flow player'a mp3
 */
public class Mp3PlayerFlow extends Flow {

    public static final String ARG_SONG_LIST = "songList";
    public static final String ARG_CURRENT_SONG = "currentSong";
    public static final String ARG_CUSTOM_SONG = "customSong";
    public static final String ARG_HOST_SONG = "hostSong";
    public static final String ARG_ADD_SONG = "addSong";
    public static final String ARG_PLAY_SONG_BY_NAME = "playSongByName";
    public static final String ARG_VOLUME = "volume";
    public static final String ARG_REWIND = "rewindTo";


    private Mp3PlayerController viewController;
    @SuppressWarnings("unused")
    public static String FLOW_NAME = "Mp3PlayerFlow";
    private Mp3Player mp3Player;
    private RemoteHost host;
    private final Object flowLock = new Object();

    private String playlistHost = "";
    private int playlistPort = 8889;

    public Mp3PlayerFlow() {
        super();
        SCREEN_NAME = "MainScreen";
        host = new RemoteHost(8888);
        host.setDaemon(true);
        loadProperties();
    }

    private void loadProperties() {
        File propertiesFile = new File("client.properties");
        if (propertiesFile.exists()) {
                Properties prop = new Properties();
            try {
                InputStream is = new FileInputStream(propertiesFile);
                prop.load(is);

                playlistHost = prop.getProperty("client_ip");
                playlistPort = Integer.valueOf(prop.getProperty("client_port"));
                logger.info("Loaded config: " + playlistHost + ":" + playlistPort);
            } catch (Exception e) {
                logger.info("Problem when load playlist properties: " + e.getMessage());
            }


        } else {
                logger.info("Properties file not found");
                logger.info(propertiesFile.getAbsolutePath());
            }
    }

    @Override
    public void run() {

        while (runFlag) {

            Action action;

            while ((action = flowActions.poll()) != null) {
                try {
                    long actionId = action.getId();

                    if (actionId == Actions.PLAY_MUSIC) {
                        mp3Player.play();
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
                        playCustomMusic(action);
                    } else if (actionId == Actions.CUSTOM_MUSIC_BY_NAME) {
                        playSongFromHost(action);
                    } else if (actionId == Actions.PLAY_MUSIC_BY_NAME) {
                        playSongByName(action);
                    } else if (actionId == Actions.SET_VOLUME) {
                        setVolume(action);
                    } else if (actionId == Actions.SET_VOLUME_REMOTE) {
                        setVolumeRemote(action);
                    } else if (actionId == Actions.REFRESH_PLAYLIST) {
                        refreshList();
                    } else if (actionId == Actions.REFRESH_DURATION_TIME) {
                        refreshUI();
                    } else if (actionId == Actions.REWIND_TRACK_TO) {
                        rewindTrack(action);
                    }

                }catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }


            synchronized (flowLock) {
                try {
                    flowLock.wait(1000);
                } catch (InterruptedException e) {
                    logger.error("Error when trying thread wait: " + e.getMessage());
                }
            }
        }
    }

    private void rewindTrack(Action action) {
        if (mp3Player.getCurrentSongDuration() < 1) {
            viewController.setUISongPosition("00:00:00", 0, 0);
            return;
        }

        Integer rewind = (Integer) action.getArg().getArgs().get(ARG_REWIND);
        if (rewind == null) {
            mp3Player.rewindTrackTo((Integer) action.getArg().getFirstOfType(Integer.class));
        } else {
            int currTime = mp3Player.getCurrentTime() + rewind;
            int totalDuration = mp3Player.getCurrentSongDuration();
            currTime = currTime < 0 ? 0 : currTime;
            currTime = currTime > totalDuration ? totalDuration : currTime;
            mp3Player.rewindTrackTo(currTime);
        }
    }

    private void playCustomMusic(Action action) {
        int currSong = (int) action.getArg().getArgs().get(ARG_CUSTOM_SONG);
        mp3Player.setCurrentSong(currSong);
        mp3Player.stop();
        mp3Player.play();
        refreshList();
    }

    private void playSongFromHost(Action action) {
        String path = (String) action.getArg().getArgs().get(ARG_HOST_SONG);
        mp3Player.add(new File(path));
        mp3Player.setCurrentSong(mp3Player.getSongsFileList().size() - 1);
        controller.doAction(Actions.STOP_MUSIC);
        controller.doAction(Actions.PLAY_MUSIC);
        FlowArgs args = new FlowArgs();
        args.addArg(ARG_ADD_SONG, path);
        viewController.refreshPlaylist(mp3Player.getSongsFileList());
        refreshList();
    }

    private void playSongByName(Action action) {
        String name = (String) action.getArg().getArgs().get(ARG_PLAY_SONG_BY_NAME);
        if (name == null) {
            logger.info("Can not find null msg");
        } else {
            if (mp3Player.findSongByName(name)) {
                mp3Player.stop();
                mp3Player.play();
            } else {
                logger.info("Cant find song: " + name);
            }
        }
        refreshList();
    }

    @Override
    public void handleAction(Action action) {
        super.handleAction(action);
        synchronized (flowLock) {
            flowLock.notify();
        }
    }

    @Override
    public void closeFlow() {
        host.setAppClosing();
    }

    private void setVolume(Action action) {
        float valF = (int) action.getArg().getArgs().get(ARG_VOLUME);
        valF /= 100;
        logger.debug("Volume set to " + valF);
        mp3Player.setVolume(valF);
    }

    private void setVolumeRemote(Action action) {
        float valF = (int) action.getArg().getArgs().get(ARG_VOLUME);
        valF /= 100;
        double currentVolume = mp3Player.getVolume();
        currentVolume += valF;
        currentVolume = currentVolume > 1 ? 1 : currentVolume;
        currentVolume = currentVolume < 0 ? 0 : currentVolume;
        mp3Player.setVolume(currentVolume);
        viewController.setVolumeSliderValue(currentVolume * 100);
    }

    private void refreshList() {
        FlowArgs args = new FlowArgs();
        args.addArg(ARG_CURRENT_SONG, mp3Player.getCurrentSong());
        viewController.refresh(args);
        refreshPlaylist();
    }

    private void refreshUI() {
        int duration = mp3Player.getCurrentSongDuration();
        int currentTime = mp3Player.getCurrentTime();
        String time = Toolkit.secondsToTimeString(currentTime) + " / ";
        time +=Toolkit.secondsToTimeString(duration);
        viewController.setUISongPosition(time, duration, currentTime);
    }

    @Override
    public void runScreen(Stage stage) {
        super.runScreen(stage);
        loadMP3Files();
        OpenScene scene = new OpenScene();
        try {
            viewController = (Mp3PlayerController) scene.start(stage, "Mp3Player.fxml");
            viewController.setController(controller);
            viewController.refreshPlaylist(mp3Player.getSongsFileList());

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Problem with starting screen: " + e.getMessage());
        }
    }

    @Override
    protected void init() {
        mp3Player = new Mp3PlayerFx(controller);
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

        refreshPlaylist();
    }

    private void listFiles(String directoryName, LinkedList<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        if (fList != null) {
            for (File file : fList) {
                if (file.isFile() && file.getName().endsWith("mp3")) {
                    files.add(file);
                } else if (file.isDirectory()) {
                    listFiles(file.getAbsolutePath(), files);
                }
            }
        }
    }

    private void refreshPlaylist() {
        Thread refresher = new Thread(new ConnectedHosts(playlistHost,
                playlistPort, mp3Player.getSongsFileList() ,mp3Player.getCurrentSong()));
        refresher.setDaemon(true);
        refresher.setName("PlaylistRefresher");
        refresher.start();
    }
}
