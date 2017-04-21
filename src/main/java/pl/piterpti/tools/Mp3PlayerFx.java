package pl.piterpti.tools;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;

import java.io.File;
import java.util.ArrayList;

/**
 * Mp3 player based on JavaFX (MediaPlayer)
 */
public class Mp3PlayerFx implements Mp3Player {

    private MediaPlayer player;

    private Logger logger = Logger.getLogger(this.getClass());
    private ArrayList<File> songsFileList = new ArrayList<>();
    private int currentSong = 0;
    private Controller controller;
    private boolean paused;
    private EndSongListener endSongListener;
    private double currentVolume = 1;
    private boolean playing = false;

    private int currentSongDuration;
    private int currentTime = 0;
    private int prevCurrentTime = 0;


    public Mp3PlayerFx(Controller controller) {
        this.controller = controller;

        TrackDurationUpdater tdu = new TrackDurationUpdater();
        Thread t = new Thread(tdu);
        t.setDaemon(true);
        t.start();

    }


    @Override
    public void play() {
        if (playing) {
            return;
        }
        if (!paused) {
            setCurrentSongDuration(0);
            setCurrentTime(0);
            Media media = new Media(songsFileList.get(getCurrentSong()).getAbsoluteFile().toURI().toString());
            player = new MediaPlayer(media);

            endSongListener = new EndSongListener();
            player.setOnEndOfMedia(endSongListener);
            player.setVolume(currentVolume);


            player.setOnReady(() -> {
                setCurrentSongDuration((int) media.getDuration().toSeconds());
                controller.doAction(Actions.REFRESH_DURATION_TIME);
                player.play();
            });


        } else {
            player.setVolume(currentVolume);
            player.setOnReady(() -> player.play());
        }
        playing = true;
        paused = false;
    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }

        playing = false;
    }

    @Override
    public void pause() {
        if (player != null) {
            player.pause();
        }
        paused = true;
        playing = false;
    }

    @Override
    public void next() {
        if (getCurrentSong() >= songsFileList.size() - 1) {
            setCurrentSong(0);
        } else {
            incCurrentSong();
        }

        stop();
        play();
    }

    @Override
    public void prev() {
        if (getCurrentSong() <= 0) {
            setCurrentSong(songsFileList.size() - 1);
        } else {
            decCurrentSong();
        }
        stop();
        play();
    }

    public void setVolume(double volume) {
        currentVolume = volume;
        if (player != null) {
            player.setVolume(currentVolume);
        }
    }

    @Override
    public double getVolume() {
        return currentVolume;
    }

    @Override
    public void add(File f) {
        if (f.exists()) {
            songsFileList.add(f);
        } else {
            logger.warn("Can not add not existing file: " + f.getAbsolutePath());
        }
    }

    private synchronized void incCurrentSong() {
        currentSong++;
    }

    private synchronized void decCurrentSong() {
        currentSong--;
    }

    public synchronized int getCurrentTime() {
        return currentTime;
    }

    private synchronized void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    private synchronized int getPrevCurrentTime() {
        return prevCurrentTime;
    }

    private synchronized void setPrevCurrentTime(int prevCurrentTime) {
        this.prevCurrentTime = prevCurrentTime;
    }

    @Override
    public synchronized void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    public synchronized int getCurrentSong() {
        return currentSong;
    }

    @Override
    public ArrayList<File> getSongsFileList() {
        return songsFileList;
    }

    @Override
    public boolean findSongByName(String song) {
        song = song.replaceAll("mp3/", "");
        for (int i = 0; i < songsFileList.size(); i++) {
            if (songsFileList.get(i).getName().equals(song)) {
                setCurrentSong(i);
                return true;
            }
        }
        return false;
    }

    class EndSongListener implements Runnable {

        @Override
        public void run() {
            setCurrentTime(0);
            controller.doAction(Actions.REFRESH_DURATION_TIME);
            controller.doAction(Actions.NEXT_MUSIC);
        }
    }

    public synchronized int getCurrentSongDuration() {
        return currentSongDuration;
    }

    public synchronized void setCurrentSongDuration(int currentSongDuration) {
        this.currentSongDuration = currentSongDuration;
    }



    class TrackDurationUpdater implements Runnable {

        @Override
        public void run() {
            while (true) {
                if (player != null) {
                    setCurrentTime((int) player.getCurrentTime().toSeconds());
                    if (getCurrentTime() != getPrevCurrentTime()) {
                        controller.doAction(Actions.REFRESH_DURATION_TIME);
                        setPrevCurrentTime(getCurrentTime());
                    }
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void rewindTrackTo(int duration) {
        player.seek(new Duration(duration * 1000));
        setCurrentTime(getCurrentTime() + duration);
    }

}
