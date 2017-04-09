package pl.piterpti.tools;

import jaco.mp3.player.MP3Player;

import java.io.File;

/**
 * Created by piter on 09.04.17.
 */
public class Mp3Player {

    private MP3Player mp3Player;

    public Mp3Player() {
        mp3Player = new MP3Player();
    }

    public void play() {
        mp3Player.addToPlayList(new File("a.mp3"));
        mp3Player.play();
    }

}
