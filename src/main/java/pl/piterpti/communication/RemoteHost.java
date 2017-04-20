package pl.piterpti.communication;

import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.Mp3PlayerFlow;
import pl.piterpti.message.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static pl.piterpti.message.Messages.*;

public class RemoteHost extends Thread {

    private Logger logger = Logger.getLogger(this.getClass());

    private final int port;
    private ServerSocket hostServer;
    private Socket socket;

    private ObjectInputStream streamIn = null;

    private ArrayList<File> songsList;

    private boolean appClosing = false;

    private Controller controller;

    public RemoteHost(int aPort) {
        port = aPort;
        configureServer();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void configureServer() {

        try {
            hostServer = new ServerSocket(port);
        } catch (IOException e) {
            logger.warn("Error when creating server:" + e.toString());
        }
    }

    @Override
    public void run() {
        if (hostServer == null) {
            logger.warn("Host server is null..");
            return;
        }

        while (!appClosing) {
            try {
                socket = hostServer.accept();

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                Object recObj = ois.readObject();

                if (recObj instanceof Message) {

                    Message recMsg = (Message) recObj;
                    switch (recMsg.getMessageType()) {
                        case SEND_FILE:
                            MessageSendFile msf = (MessageSendFile) recMsg;
                            retrieveFile(is, msf);
                            break;

                        case PLAYER_CONTROL:
                            MessagePlayerControl mpc = (MessagePlayerControl) recMsg;
                            doAction(mpc.getMsg(), mpc.getArgs());
                            break;

                        default:
                            logger.error("Unknown message type: " + recMsg.getMessageType().toString());
                            break;
                    }
                } else {
                    logger.warn("Unknown message: " + recObj.toString());
                }

                is.close();
                ois.close();
                socket.close();
            } catch (Exception e) {
                logger.warn("Communication problem: " + e.getMessage());
                e.printStackTrace();
            }
        }

        close();
        logger.info("Host thread ended work");
    }

    private void retrieveFile(InputStream is, MessageSendFile msf) throws Exception {
        Path p = Paths.get(msf.getFileName());
        String file = p.getFileName().toString();
        file = "mp3/" + file;

        OutputStream os = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);

        if (checkMp3Exist(file)) { // mp3 exist
            playExistMP3(file);
            objectOutputStream.writeObject(new MessagePlayerControl(Messages.MSG_EXIST));
            objectOutputStream.flush();
        } else {
            objectOutputStream.writeObject(new MessagePlayerControl(Messages.MSG_SEND_MP3));
            objectOutputStream.flush();
            FileOutputStream out = new FileOutputStream(file);

            int count;
            byte[] buffer = new byte[4096];
            while ((count = is.read(buffer)) > 0) {
                out.write(buffer, 0, count);
            }

            out.close();

            playMP3(file);
        }
        objectOutputStream.close();
        os.close();
    }

    private void close()  {
        logger.info("Closing connection");
        try {
            if (socket != null) {
                socket.close();
            }
            if (hostServer != null) {
                hostServer.close();
            }

            if (streamIn != null) {
                streamIn.close();
            }

        } catch (IOException e) {
            logger.warn("Problem with closing resources");
        }
    }

    @SuppressWarnings("unused")
    public void setAppClosing() {
        this.appClosing = true;
    }

    private void playMP3(String name) {
        FlowArgs args = new FlowArgs(Mp3PlayerFlow.ARG_HOST_SONG, name);
        controller.doAction(Actions.CUSTOM_MUSIC_BY_NAME, args);
    }

    private void playExistMP3(String name) {
        FlowArgs args = new FlowArgs(Mp3PlayerFlow.ARG_PLAY_SONG_BY_NAME, name);
        controller.doAction(Actions.PLAY_MUSIC_BY_NAME, args);
    }

    private void doAction(String msg, FlowArgs args) {
        switch (msg) {
            case MSG_PLAY:
                controller.doAction(Actions.PLAY_MUSIC);
                break;
            case MSG_PAUSE:
                controller.doAction(Actions.PAUSE_MUSIC);
                break;
            case MSG_NEXT:
                controller.doAction(Actions.NEXT_MUSIC);
                break;
            case MSG_PREV:
                controller.doAction(Actions.PREV_MUSIC);
                break;
            case MSG_STOP:
                controller.doAction(Actions.STOP_MUSIC);
                break;
            case MSG_SET_VOLUME:
                controller.doAction(Actions.SET_VOLUME_REMOTE, args);
                break;
            case MSG_CURRENT_SONG:
                controller.doAction(Actions.PLAY_MUSIC_BY_NAME, args);
                break;
            case MSG_REFRESH_PLAYLIST:
                controller.doAction(Actions.REFRESH_PLAYLIST);
                break;
            case MSG_CLOSE_APP:
                controller.doAction(Actions.APP_CLOSED);
                break;
            case MSG_REV_FORWARD:
                controller.doAction(Actions.REWIND_TRACK_TO, new FlowArgs(Mp3PlayerFlow.ARG_REWIND, 20));
                break;
            case MSG_REV_BACKWARD:
                controller.doAction(Actions.REWIND_TRACK_TO, new FlowArgs(Mp3PlayerFlow.ARG_REWIND, -20));
                break;
            default:
                logger.warn("Unhandled msg from client: " + msg);
                break;
        }
    }

    private boolean checkMp3Exist(String fileName) {
        fileName = fileName.replaceAll("mp3/", "");
        for (File f : songsList) {
            if (f.getName().equals(fileName)) {
                return true;
            }
        }

        return false;
    }

    public void setSongsList(ArrayList<File> songsList) {
        this.songsList = songsList;
    }
}
