package pl.piterpti.communication;

import org.apache.log4j.Logger;
import pl.piterpti.message.MessagePlaylist;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

 /**
 * Thread to send playlist to remote device
 */
public class ConnectedHosts implements Runnable {

    private Logger logger = Logger.getLogger(this.getClass());

    private String[] hosts;
    private int port;
    private MessagePlaylist mpl;

    public ConnectedHosts(String[] hosts, int port, ArrayList<File> files, int current) {
        this.hosts = hosts;
        this.port = port;
        ArrayList<String> playlist = new ArrayList<>();
        for (File f : files) {
            playlist.add(f.getName());
        }
        mpl = new MessagePlaylist(playlist, current);
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            for (String host : hosts) {
                socket = new Socket(host, port);
                socket.setSoTimeout(1000);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                oos.writeObject(mpl);
                oos.flush();
                logger.debug("Playlist sent to " + hosts);

                oos.close();
            }
        } catch (Exception e) {
            // playlist not sent
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
