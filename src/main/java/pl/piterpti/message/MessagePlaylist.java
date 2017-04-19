package pl.piterpti.message;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Message to send playlist to remote device
 */
public class MessagePlaylist implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<String> playlist;
    private int current;

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYLIST;
    }

    public MessagePlaylist(ArrayList<String> playlist, int current) {
        this.playlist = playlist;
        this.current = current;
    }

    public ArrayList<String> getPlaylist() {
        return playlist;
    }

    public int getCurrent() {
        return current;
    }
}
