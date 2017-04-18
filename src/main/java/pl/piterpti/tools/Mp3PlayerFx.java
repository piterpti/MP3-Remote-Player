package pl.piterpti.tools;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by piter on 16.04.17.
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

    public Mp3PlayerFx(Controller controller) {
        this.controller = controller;
    }


    @Override
    public void play(boolean start) {
        if (!paused) {
            Media media = new Media(songsFileList.get(getCurrentSong()).getAbsoluteFile().toURI().toString());
            player = new MediaPlayer(media);
            endSongListener = new EndSongListener();
            player.setOnEndOfMedia(endSongListener);
            player.setVolume(currentVolume);
            player.play();

        } else {
            player.setVolume(currentVolume);
            player.play();
        }
        paused = false;
    }

    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            player.pause();
        }
        paused = true;
    }

    @Override
    public void next() {
        if (getCurrentSong() >= songsFileList.size() - 1) {
            setCurrentSong(0);
        } else {
            incCurrentSong();
        }

        stop();
        play(true);
    }

    @Override
    public void prev() {
        if (getCurrentSong() <= 0) {
            setCurrentSong(songsFileList.size() - 1);
        } else {
            decCurrentSong();
        }
        stop();
        play(true);
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
    public void findSongByName(String song) {
        song = song.replaceAll("mp3/", "");
        for (int i = 0; i < songsFileList.size(); i++) {
            if (songsFileList.get(i).getName().equals(song)) {
                setCurrentSong(i);
                return;
            }
        }
    }

    class EndSongListener implements Runnable {

        @Override
        public void run() {
            controller.doAction(Actions.NEXT_MUSIC);
        }
    }
}