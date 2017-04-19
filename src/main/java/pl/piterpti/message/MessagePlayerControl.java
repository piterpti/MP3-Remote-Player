package pl.piterpti.message;

import java.io.Serializable;

/**
 * Message to control mp3 player
 */
public class MessagePlayerControl implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    private String msg;

    private FlowArgs args;

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

    public FlowArgs getArgs() {
        return args;
    }

    public void setArgs(FlowArgs args) {
        this.args = args;
    }
}
