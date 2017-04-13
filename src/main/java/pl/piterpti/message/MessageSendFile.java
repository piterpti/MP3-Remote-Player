package pl.piterpti.message;

import java.io.Serializable;

/**
 * Created by piter on 13.04.17.
 */

public class MessageSendFile implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;

    @Override
    public MessageType getMessageType() {
        return MessageType.SEND_FILE;
    }

    public MessageSendFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
