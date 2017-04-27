package pl.piterpti.view.controller.component;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by piter on 27.04.17.
 */
public class PlaylistView extends SimpleListProperty {

    public PlaylistView() {
        super(FXCollections.observableArrayList());
    }

    @Override
    public boolean add(Object element) {
        if (element instanceof Song) {
            return super.add(element);
        } else {
            return false;
        }
    }

    @Override
    public boolean addAll(Collection elements) {
        List<Object> songs = new ArrayList<>();
        for (Object obj : elements) {
            if (obj instanceof Song) {
                ((Song) obj).setTitle(cutSong((Song) obj, 25));
                songs.add(obj);
            }
        }
        return super.addAll(songs);
    }

    private String cutSong(Song song, int index) {
        String str = song.getTitle().trim();
        index = index - String.valueOf(song.getNumber()).length() + 1;
        if (index > str.length()) {
            int spaces = index + 2 - str.length();
            for (int i = 0; i < spaces; i++) {
                str += " ";
            }
            return str;
        } else {
            return str.substring(0, index) + "..";
        }
    }
}
