package pl.piterpti.communication;

import org.apache.log4j.Logger;
import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.FlowArgs;
import pl.piterpti.flow.Mp3PlayerFlow;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by piter on 10.04.17.
 */
public class RemoteHost extends Thread {

    public static final String MSG = "MSG:";
    public static final String MSG_PLAY = "PLAY";
    public static final String MSG_PAUSE = "PAUSE";
    public static final String MSG_STOP = "STOP";
    public static final String MSG_NEXT = "NEXT";
    public static final String MSG_PREV = "PREV";
    public static final String MSG_EXIST = "EXIST";
    public static final String MSG_SEND_MP3 = "SEND_MP3";

    private Logger logger = Logger.getLogger(this.getClass());

    private Object connectionLock = new Object();

    private boolean clientConnected = false;

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

        while (true && !appClosing) {
            try {
                socket = hostServer.accept();
                setClientConnected(true);

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String path = ois.readUTF();
                logger.info("Received: " + path);
                if (path.startsWith(MSG)) { // control instruction
                    doAction(path);
                } else { // file retrieve
                    Path p = Paths.get(path);
                    String file = p.getFileName().toString();
                    file = "mp3/" + file;

                    OutputStream os = socket.getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(os);

                    if (checkMp3Exist(file)) { // mp3 exist
                        playExistMP3(file);
                        objectOutputStream.writeUTF(MSG + MSG_EXIST);
                        objectOutputStream.flush();
                    } else {
                        objectOutputStream.writeUTF(MSG + MSG_SEND_MP3);
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
                }

            } catch (Exception e) {
                logger.warn("Communication problem: " + e.getMessage());
                e.printStackTrace();
            }
        }

        close();
        logger.info("Host thread ended work");
    }

    public void close()  {
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

    private void setClientConnected(boolean aClientConnected) {
        synchronized (connectionLock) {
            clientConnected = aClientConnected;
        }
    }

    public boolean isClientConnected() {
        synchronized (connectionLock) {
            return clientConnected;
        }
    }

    public void setAppClosing(boolean appClosing) {
        this.appClosing = appClosing;
    }

    private void playMP3(String name) {
        FlowArgs args = new FlowArgs(Mp3PlayerFlow.ARG_HOST_SONG, name);
        controller.doAction(Actions.CUSTOM_MUSIC_BY_NAME, args);
    }

    private void playExistMP3(String name) {
        FlowArgs args = new FlowArgs(Mp3PlayerFlow.ARG_PLAY_SONG_BY_NAME, name);
        controller.doAction(Actions.PLAY_MUSIC_BY_NAME, args);
    }

    private void doAction(String msg) {
        if (msg.indexOf(":") > -1) {
            String msgContent = msg.split(":")[1];

            switch (msgContent) {
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
            }
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
