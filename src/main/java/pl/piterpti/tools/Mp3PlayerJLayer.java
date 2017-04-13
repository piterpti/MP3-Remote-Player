package pl.piterpti.tools;

import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class Mp3PlayerJLayer implements Mp3Player{

    private PausablePlayer player;
    private Logger logger = Logger.getLogger(this.getClass());
    private ArrayList<File> songsFileList = new ArrayList<>();
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
    public void play(boolean start) {
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
                FileInputStream fis = new FileInputStream(songsFileList.get(currentSong));
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
            play(true);
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
            play(true);
        }
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