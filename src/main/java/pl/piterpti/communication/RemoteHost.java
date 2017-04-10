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

/**
 * Created by piter on 10.04.17.
 */
public class RemoteHost extends Thread {

    private Logger logger = Logger.getLogger(this.getClass());

    private Object connectionLock = new Object();

    private boolean clientConnected = false;

    private final int port;
    private ServerSocket hostServer;
    private Socket socket;

    private ObjectInputStream streamIn = null;
    private ObjectOutputStream streamOut;

    private Message receivedMsg;

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
//            hostServer.setSoTimeout(1000 * 600);
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
//                streamOut  = new ObjectOutputStream(socket.getOutputStream());
//                streamIn = new ObjectInputStream((socket.getInputStream()));

                InputStream is = socket.getInputStream();
                ObjectInputStream ois = new ObjectInputStream(is);
                String path = ois.readUTF();
                logger.info(path);

                Path p = Paths.get(path);
                String file = p.getFileName().toString();
                file = "mp3/" + file;
                FileOutputStream out = new FileOutputStream( file);



                int count;
                byte[] buffer = new byte[4096]; // or 4096, or more
                while ((count = is.read(buffer)) > 0)
                {
                    out.write(buffer, 0, count);
                }
                out.close();

                playMP3(file);

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

            if (streamOut != null) {
                streamOut.close();
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
}
