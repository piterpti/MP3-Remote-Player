package pl.piterpti.tools;

import java.io.File;
import java.util.ArrayList;

/**
 * Mp3 player basic functionality
 */
public interface Mp3Player {

    /**
     * Play current mp3
     */
    void play();

    /**
     * Stop playing mp3
     */
    void stop();

    /**
     * Pause current playing mp3
     */
    void pause();

    /**
     * Skip to next mp3
     */
    void next();

    /**
     * Back to previous mp3
     */
    void prev();

    /**
     * Add mp3 file to current playlist
     * @param f mp3 file
     */
    void add(File f);

    /**
     * Change played mp3 to number from list
     * @param currentSong number
     */
    void setCurrentSong(int currentSong);

    /**
     * Get current mp3 number
     * @return mp3 number
     */
    int getCurrentSong();

    /**
     * Get all mp3 files
     * @return mp3 files list
     */
    ArrayList<File> getSongsFileList();

    /**
     * Set current song to filename
     * @param song song filename
     */
    boolean findSongByName(String song);

    /**
     * Set player volume
     * @param volume volume
     */
    void setVolume(double volume);

    /**
     *
     * @return current volume value
     */
    double getVolume();

    /**
     * @return song duration in seconds
     */
    int getCurrentSongDuration();

    /**
     *
     * @param currentSongDuration
     */
    void setCurrentSongDuration(int currentSongDuration);

    /**
     *
     * @return
     */
    int getCurrentTime();

    /**
     *
     * @param duration
     */
    void rewindTrackTo(int duration);

}
