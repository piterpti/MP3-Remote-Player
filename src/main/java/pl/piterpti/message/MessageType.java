package pl.piterpti.message;

import java.io.Serializable;

/**
 * Message types
 */
public enum MessageType implements Serializable {

    SEND_FILE,
    PLAYER_CONTROL,
    PLAYLIST;

    private static final long serialVersionUID = 1L;

}
