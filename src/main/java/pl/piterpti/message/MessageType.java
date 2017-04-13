package pl.piterpti.message;

import java.io.Serializable;

/**
 * Created by piter on 13.04.17.
 */
public enum MessageType implements Serializable {

    SEND_FILE,
    PLAYER_CONTROL;

    private static final long serialVersionUID = 1L;

}
