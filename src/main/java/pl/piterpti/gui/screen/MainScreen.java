package pl.piterpti.gui.screen;

import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.FlowArgs;
import pl.piterpti.flow.Mp3PlayerFlow;
import pl.piterpti.gui.screen.componenet.Audio;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import static pl.piterpti.flow.Mp3PlayerFlow.*;

/**
 * Created by piter on 09.04.17.
 */
public class MainScreen extends EmptyScreen {

    private JButton playBtn;
    private JButton stopBtn;
    private JButton pauseBtn;
    private JButton nextBtn;
    private JButton prevBtn;

    private JList<String> songsJList;
    private DefaultListModel<String> dlm;
    private ArrayList<File> songsList;

    private int currentNum = 1;


    public MainScreen(String s) {
        super(s, null);
        initUI();
    }

    public MainScreen(String s, Controller controller) {
        super(s, controller);
        initUI();
    }

    private void initUI() {
        setResizable(true);
        mainPanel.setLayout(new GridLayout(1, 2));
        JPanel btnPanel = new JPanel(new GridLayout(3, 2));
        JPanel listPanel = new JPanel(new BorderLayout());
        playBtn = new JButton("Play");
        stopBtn = new JButton("Stop");
        pauseBtn = new JButton("Pause");
        nextBtn = new JButton("Next");
        prevBtn = new JButton("Prev");

        JSlider volumeSlider = new JSlider(1, 100);

        JScrollPane scrollPane = new JScrollPane();
        dlm = new DefaultListModel<>();
        songsJList = new JList<>(dlm);
        scrollPane.setViewportView(songsJList);
        songsJList.ensureIndexIsVisible(songsJList.getSelectedIndex());

        btnPanel.add(stopBtn);
        btnPanel.add(playBtn);
        btnPanel.add(pauseBtn);

        btnPanel.add(prevBtn);
        btnPanel.add(volumeSlider);
        btnPanel.add(nextBtn);

        listPanel.add(scrollPane);

        mainPanel.add(btnPanel);
        mainPanel.add(listPanel);

        playBtn.addActionListener(e -> {
            controller.doAction(Actions.PLAY_MUSIC);
        });

        stopBtn.addActionListener(e ->{
            controller.doAction(Actions.STOP_MUSIC);
        });

        pauseBtn.addActionListener(e -> {
            controller.doAction(Actions.PAUSE_MUSIC);
        });

        prevBtn.addActionListener(e -> {
            controller.doAction(Actions.PREV_MUSIC);
        });

        nextBtn.addActionListener(e -> {
            controller.doAction(Actions.NEXT_MUSIC);
        });

        songsJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() == 2) {
                    controller.doAction(Actions.CUSTOM_MUSIC,
                            new FlowArgs(ARG_CUSTOM_SONG, songsJList.getSelectedIndex()));
                }
            }
        });

        volumeSlider.addChangeListener(changeEvent ->
                controller.doAction(Actions.SET_VOLUME, new FlowArgs(Mp3PlayerFlow.ARG_VOLUME, volumeSlider.getValue())));
    }

    @Override
    public void refresh(FlowArgs args) {
        songsList = (ArrayList<File>) args.getArgs().get(ARG_SONG_LIST);
        if (songsList != null && !songsList.isEmpty()) {
            refreshSongList();
        }
        Integer currentSong = (Integer) args.getArgs().get(ARG_CURRENT_SONG);
        if (currentSong != null) {
            songsJList.clearSelection();
            songsJList.addSelectionInterval(currentSong, currentSong);
            songsJList.ensureIndexIsVisible(songsJList.getSelectedIndex());
        }

        String song = (String) args.getArgs().get(ARG_ADD_SONG);
        if (song != null) {
            songsJList.clearSelection();
            dlm.addElement(currentNum++ + " " + song.replaceAll("mp3/", ""));
            songsJList.addSelectionInterval(dlm.getSize(), dlm.getSize());
            songsJList.ensureIndexIsVisible(songsJList.getSelectedIndex());
        }

    }

    private void refreshSongList() {
        dlm.clear();

        for (File f : songsList) {
            dlm.addElement(currentNum++ + ". " + f.getName());
        }
    }

}
