package pl.piterpti.view.controller.component;

import pl.piterpti.tools.Toolkit;

import java.io.File;

/**
 * Created by piter on 27.04.17.
 */
public class Song {


    private String title;
    private File file;
    private int number;
    private int duration;

    public Song() {
    }

    public Song(String title, int number, int duration) {
        this.title = title;
        this.number = number;
        setDuration(duration);
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public synchronized int getDuration() {
        return duration;
    }

    public synchronized void setDuration(int duration) {
        this.duration = duration;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return number + ". " + title + " " + Toolkit.secondsToTimeString(getDuration());
    }
}
