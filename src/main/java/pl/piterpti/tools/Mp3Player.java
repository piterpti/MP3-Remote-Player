package pl.piterpti.tools;

import jaco.mp3.player.MP3Player;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3Player {

    private Logger logger = Logger.getLogger(this.getClass());
    private MP3Player mp3Player;

    public Mp3Player() {
        mp3Player = new MP3Player();
    }

    public void addFile(File f) {
        if (f.exists()) {
            mp3Player.addToPlayList(f);
        } else {
            logger.warn("Trying add not exist file to playlist: " + f.getName());
        }
    }

    public void play() {
        mp3Player.play();
    }

    public void pause() {
        mp3Player.pause();
    }

    public void stop() {
        mp3Player.stop();
    }

    public void next() {
        mp3Player.skipForward();
    }

    public void prev() {
        mp3Player.skipBackward();
    }

}
