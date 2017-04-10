package pl.piterpti.communication;

import org.apache.log4j.Logger;

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
    private Object lock = new Object();

    private boolean clientConnected = false;

    private final int port;
    private ServerSocket hostServer;
    private Socket socket;

    private ObjectInputStream streamIn = null;
    private ObjectOutputStream streamOut;

    private Message receivedMsg;

    private boolean appClosing = false;

    public RemoteHost(int aPort) {
        port = aPort;
        configureServer();
    }

    private void configureServer() {

        try {
            hostServer = new ServerSocket(port);
            hostServer.setSoTimeout(1000 * 600);
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
                FileOutputStream out = new FileOutputStream(file);



                int count;
                byte[] buffer = new byte[4096]; // or 4096, or more
                while ((count = is.read(buffer)) > 0)
                {
                    out.write(buffer, 0, count);
                }
                out.close();


//                while (isClientConnected()) {
//                    synchronized (lock) {
//                        receivedMsg = (Message) streamIn.readObject();
//                        logger.info("Received from host: " + receivedMsg.toString());
//                    }
//
//                    receivedMsg = null;
//                }


            } catch (Exception e) {
                logger.warn("Communication problem: " + e.getMessage());
                e.printStackTrace();
            }

        }

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
}
