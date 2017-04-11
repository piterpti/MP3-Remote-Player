package pl.piterpti.tools;

import jaco.mp3.player.MP3Player;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3PlayerJACO implements Mp3Player {

    private Logger logger = Logger.getLogger(this.getClass());
    private ArrayList<File> songsFileList = new ArrayList<>();
    private MP3Player mp3Player;
    private int currentSong = 0;

    public Mp3PlayerJACO() {
        mp3Player = new MP3Player();
    }

    public void add(File f) {
        if (f.exists()) {
            songsFileList.add(f);
        } else {
            logger.warn("Can not add not existing file: " + f.getAbsolutePath());
        }
    }

    public void play(boolean start) {
        mp3Player.stop();
        mp3Player.removeAll();
        mp3Player = new MP3Player();
        for (int i = currentSong; i < songsFileList.size(); i++) {
            mp3Player.addToPlayList(songsFileList.get(i));
        }
        for (int i = 0; i < currentSong; i++) {
            mp3Player.addToPlayList(songsFileList.get(i));
        }

        if (start) {
            mp3Player.play();
        } else {
            mp3Player.pause();
        }
    }

    public void pause() {
        mp3Player.pause();
    }

    public void stop() {
        mp3Player.stop();
    }

    public void next() {
        if (currentSong >= songsFileList.size() - 1) {
            currentSong = 0;
        } else {
            currentSong++;
        }
        if (mp3Player.isPaused() || mp3Player.isStopped()) {
            play(false);
        } else {
            play(true);
        }
    }

    public void prev() {
        if (currentSong <= 0) {
            currentSong = songsFileList.size() - 1;
        } else {
            currentSong--;
        }
        if (mp3Player.isPaused() || mp3Player.isStopped()) {
            play(false);
        } else {
            play(true);
        }
    }

    public ArrayList<File> getSongsFileList() {
        return songsFileList;
    }

    @Override
    public void findSongByName(String song) {

    }

    public int getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(int currentSong) {
        this.currentSong = currentSong;
    }
}
