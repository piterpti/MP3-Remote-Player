package pl.piterpti.message;

import java.io.Serializable;

/**
 * Created by piter on 13.04.17.
 */

public class MessagePlayerControl implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_CONTROL;
    }

    public MessagePlayerControl(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
