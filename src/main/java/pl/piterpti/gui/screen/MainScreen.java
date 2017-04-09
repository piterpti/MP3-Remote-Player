package pl.piterpti.gui.screen;

import pl.piterpti.controller.Actions;
import pl.piterpti.controller.Controller;

import javax.swing.*;
import java.awt.*;

/**
 * Created by piter on 09.04.17.
 */
public class MainScreen extends EmptyScreen {

    private JButton playBtn;
    private JButton stopBtn;

    public MainScreen(String s) {
        super(s, null);
        initUI();
    }

    public MainScreen(String s, Controller controller) {
        super(s, controller);
        initUI();
    }

    private void initUI() {
        playBtn = new JButton("Play");
        stopBtn = new JButton("Stop");

        mainPanel.add(playBtn, BorderLayout.CENTER);
        mainPanel.add(stopBtn, BorderLayout.NORTH);

        playBtn.addActionListener(e -> {
            controller.doAction(Actions.PLAY_MUSIC);
        });

        stopBtn.addActionListener(e ->{
            controller.doAction(Actions.STOP_MUSIC);
        });

    }
}
