package pl.piterpti.view.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import pl.piterpti.controller.Actions;
import pl.piterpti.flow.Mp3PlayerFlow;
import pl.piterpti.message.FlowArgs;
import pl.piterpti.view.controller.component.PlaylistView;
import pl.piterpti.view.controller.component.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import static pl.piterpti.flow.Mp3PlayerFlow.*;

/**
 * Controller for mp3 player view
 */
public class Mp3PlayerController extends MyController {

    private ArrayList<Song> songsList;

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
    private ListView<String> playlistListView;

    @FXML
    private Slider volumeSlider;

    @FXML
    private Slider songPosition;

    @FXML
    private Label positionLabel;

    private PlaylistView playlist;

    private int currTime;

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

        volumeSlider.setMin(0);
        volumeSlider.setMax(100);
        volumeSlider.setValue(100);
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> controller.doAction(Actions.SET_VOLUME, new FlowArgs(Mp3PlayerFlow.ARG_VOLUME, newValue.intValue())));

        songPosition.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (currTime != newValue.intValue()) {
                    controller.doAction(Actions.REWIND_TRACK_TO, new FlowArgs("rewind", newValue.intValue()));
                }
            }
        });

    }

    private void setButtonSize() {
        playBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        stopBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pauseBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        nextBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        prevBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        playBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

    }

    public void refreshPlaylist(ArrayList<Song> filePlaylist) {
        playlist = new PlaylistView();
        playlist.addAll(filePlaylist);
        Platform.runLater(() -> {
            playlistListView.setItems(playlist);
            playlistListView.refresh();
        });
    }

    public void refresh(FlowArgs args) {
        songsList = (ArrayList<Song>) args.getArgs().get(ARG_SONG_LIST);
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
                playlist.add(title);
                playlistListView.getSelectionModel().selectLast();
                playlistListView.scrollTo(playlist.size());
            });
        }
    }

    public void setUISongPosition(String textDuration, int duration, int currTime) {
        this.currTime = currTime;
        Platform.runLater(() -> {
            positionLabel.setText(textDuration);
            songPosition.setMax(duration);
            songPosition.setValue(currTime);
        });
    }

    public void setVolumeSliderValue(double newVal) {
        Platform.runLater(() -> volumeSlider.setValue(newVal));
    }

}
