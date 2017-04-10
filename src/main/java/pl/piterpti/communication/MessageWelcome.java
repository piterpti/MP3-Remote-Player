package pl.piterpti.communication;

/**
 * Created by piter on 10.04.17.
 */
public class MessageWelcome implements Message{

    @Override
    public MessageType getMessageType() {
        return MessageType.WELCOME;
    }

    @Override
    public String toString() {
        return MessageType.WELCOME.toString();
    }
}
