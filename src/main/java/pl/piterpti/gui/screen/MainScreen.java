package pl.piterpti.gui.screen;

import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.FlowArgs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static pl.piterpti.flow.Mp3PlayerFlow.ARG_CURRENT_SONG;
import static pl.piterpti.flow.Mp3PlayerFlow.ARG_CUSTOM_SONG;
import static pl.piterpti.flow.Mp3PlayerFlow.ARG_SONG_LIST;

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


    public MainScreen(String s) {
        super(s, null);
        initUI();
    }

    public MainScreen(String s, Controller controller) {
        super(s, controller);
        initUI();
    }

    private void initUI() {
        mainPanel.setLayout(new GridLayout(1, 2));
        JPanel btnPanel = new JPanel(new GridLayout(3, 2));
        JPanel listPanel = new JPanel(new BorderLayout());
        playBtn = new JButton("Play");
        stopBtn = new JButton("Stop");
        pauseBtn = new JButton("Pause");
        nextBtn = new JButton("Next");
        prevBtn = new JButton("Prev");

        JScrollPane scrollPane = new JScrollPane();
        dlm = new DefaultListModel<>();
        songsJList = new JList<>(dlm);
        scrollPane.setViewportView(songsJList);
        songsJList.ensureIndexIsVisible(songsJList.getSelectedIndex());

        btnPanel.add(stopBtn);
        btnPanel.add(playBtn);
        btnPanel.add(pauseBtn);

        btnPanel.add(prevBtn);
        btnPanel.add(new JLabel());
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
    }

    private void refreshSongList() {
        dlm.clear();

        int counter = 1;
        for (File f : songsList) {
            dlm.addElement(counter++ + ". " + f.getName());
        }
    }

}
