package pl.piterpti.tools;

import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.view.controller.component.Song;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Mp3PlayerJLayer implements Mp3Player{

    private PausablePlayer player;
    private Logger logger = Logger.getLogger(this.getClass());
    private ArrayList<Song> songsFileList = new ArrayList<>();
    private int currentSong = 0;
    private boolean paused;
    private boolean playing;
    private Object lockMP3 = new Object();
    private Controller controller;

    public Mp3PlayerJLayer(Controller controller) {
        Thread listener = new Thread(new EndSongListener());
        listener.setDaemon(true);
        listener.start();
        this.controller = controller;
    }

    @Override
    public void play() {
        if (getSongsFileList().size() < 1) {
            logger.info("There is no mp3's");
            return;
        }
        if (player != null && paused) {
            player.resume();
            paused = false;
            return;
        }
        try {
            if (!playing) {
                FileInputStream fis = new FileInputStream(songsFileList.get(currentSong).getFile());
                player = new PausablePlayer(fis, lockMP3);
                player.play();
                playing = true;
                paused = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        if (player != null) {
            player.pause();
            player.close();
            player = null;
            playing = false;
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            player.pause();
            paused = true;
            playing = false;
        }
    }

    public void next() {
        if (getCurrentSong() >= songsFileList.size() - 1) {
            setCurrentSong(0);
        } else {
            incCurrentSong();
        }

        stop();
        if (!paused) {
            play();
        }
    }

    public void prev() {
        if (getCurrentSong() <= 0) {
            setCurrentSong(songsFileList.size() - 1);
        } else {
            decCurrentSong();
        }
        stop();
        if (!paused) {
            play();
        }
    }

    @Override
    public void add(File f) {
        if (f.exists()) {
            songsFileList.add(new Song());
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
    public ArrayList<Song> getSongsFileList() {
        return songsFileList;
    }

    @Override
    public boolean findSongByName(String song) {
        song = song.replaceAll("mp3/", "");
        for (int i = 0; i < songsFileList.size(); i++) {
            if (songsFileList.get(i).getFile().getName().equals(song)) {
                setCurrentSong(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void setVolume(double volume) {

    }

    @Override
    public double getVolume() {
        return 0;
    }

    @Override
    public int getCurrentSongDuration() {
        return 0;
    }

    @Override
    public void setCurrentSongDuration(int currentSongDuration) {

    }

    @Override
    public int getCurrentTime() {
        return 0;
    }

    @Override
    public void rewindTrackTo(int duration) {

    }

    @Override
    public void sortPlaylistAlphabetically(boolean ignoreCase) {

    }

    class EndSongListener implements Runnable {

        @Override
        public void run() {
            while (true) {
                synchronized (lockMP3) {
                    try {
                        lockMP3.wait();
                        controller.doAction(Actions.NEXT_MUSIC);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}