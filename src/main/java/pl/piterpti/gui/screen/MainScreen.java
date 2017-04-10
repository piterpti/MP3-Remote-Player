package pl.piterpti.gui.screen;

import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;
import pl.piterpti.flow.FlowArgs;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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
    private LinkedList<String> songsList;

    public MainScreen(String s) {
        super(s, null);
        initUI();
    }

    public MainScreen(String s, Controller controller) {
        super(s, controller);
        initUI();
    }

    private void initUI() {
        mainPanel.setLayout(new GridLayout(3, 3));
        playBtn = new JButton("Play");
        stopBtn = new JButton("Stop");
        pauseBtn = new JButton("Pause");
        nextBtn = new JButton("Next");
        prevBtn = new JButton("Prev");

        dlm = new DefaultListModel<>();
        songsJList = new JList<>(dlm);

        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());
        mainPanel.add(new JLabel());

        mainPanel.add(stopBtn);
        mainPanel.add(playBtn);
        mainPanel.add(pauseBtn);

        mainPanel.add(prevBtn);
        mainPanel.add(songsJList);
        mainPanel.add(nextBtn);

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
    }

    @Override
    public void refresh(FlowArgs args) {
        songsList = (LinkedList<String>) args.getArgs().get(ARG_SONG_LIST);
        if (songsList != null && !songsList.isEmpty()) {
            refreshSongList();
        }
    }

    private void refreshSongList() {
        dlm.clear();
        for (String s : songsList) {
            dlm.addElement(s);
        }
    }
}
