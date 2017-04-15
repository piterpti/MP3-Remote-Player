package pl.piterpti.view.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import pl.piterpti.controller.Actions;
import pl.piterpti.message.FlowArgs;

import java.io.File;
import java.util.ArrayList;

import static pl.piterpti.flow.Mp3PlayerFlow.*;

/**
 * Created by piter on 15.04.17.
 */
public class Mp3PlayerController extends MyController {

    private ArrayList<File> songsList;

    @FXML
    private Button playBtn;

    @FXML
    private Button pauseBtn;

    @FXML
    private Button stopBtn;

    @FXML
    private Button prevBtn;

    @FXML
    private Button nextBtn;

    @FXML
    private Button customBtn;

    @FXML
    private ListView<String> playlistListView;

    private ObservableList<String> playlist;


    @FXML
    @SuppressWarnings("unused")
    private void initialize() {
        setButtonSize();

        playlistListView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        playBtn.setOnAction(actionEvent -> controller.doAction(Actions.PLAY_MUSIC));
        pauseBtn.setOnAction(actionEvent -> controller.doAction(Actions.PAUSE_MUSIC));
        stopBtn.setOnAction(actionEvent -> controller.doAction(Actions.STOP_MUSIC));
        prevBtn.setOnAction(actionEvent -> controller.doAction(Actions.PREV_MUSIC));
        nextBtn.setOnAction(actionEvent -> controller.doAction(Actions.NEXT_MUSIC));

        playlistListView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                controller.doAction(Actions.CUSTOM_MUSIC,
                        new FlowArgs(ARG_CUSTOM_SONG, playlistListView.getFocusModel().getFocusedIndex()));
            }
        });
    }

    private void setButtonSize() {
        playBtn.setMaxWidth(Double.MAX_VALUE);
        stopBtn.setMaxWidth(Double.MAX_VALUE);
        pauseBtn.setMaxWidth(Double.MAX_VALUE);
        nextBtn.setMaxWidth(Double.MAX_VALUE);
        prevBtn.setMaxWidth(Double.MAX_VALUE);
        customBtn.setMaxWidth(Double.MAX_VALUE);
    }

    public void refreshPlaylist(ArrayList<File> filePlaylist) {
        this.playlist = FXCollections.observableArrayList();
        for (File f : filePlaylist) {
            this.playlist.add(f.getName());
        }
        Platform.runLater(() -> playlistListView.setItems(playlist));
    }

    public void refresh(FlowArgs args) {
        songsList = (ArrayList<File>) args.getArgs().get(ARG_SONG_LIST);
        if (songsList != null && !songsList.isEmpty()) {
            refreshPlaylist(songsList);
        }

        Integer currentSong = (Integer) args.getArgs().get(ARG_CURRENT_SONG);
        if (currentSong != null) {
            Platform.runLater(() -> {
                playlistListView.getSelectionModel().clearAndSelect(currentSong);
                playlistListView.scrollTo(currentSong);
            });
        }

        String song = (String) args.getArgs().get(ARG_ADD_SONG);
        if (song != null) {
            Platform.runLater(() -> {
                playlistListView.getSelectionModel().clearSelection();
                String title = song.replaceAll("mp3/", "");
                System.out.println(title);
                playlist.add(title);
                playlistListView.getSelectionModel().selectLast();
                playlistListView.scrollTo(playlist.size());
            });
        }
    }
}
