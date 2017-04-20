package pl.piterpti.tools;

/**
 * Created by piter on 20.04.17.
 */
public class Toolkit {

    public static String secondsToTimeString(int totalSecs) {
        int hours = totalSecs / 3600;
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
