package pl.piterpti.gui.screen;

import pl.piterpti.controller.Controller;
import pl.piterpti.flow.FlowArgs;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Created by piter on 09.04.17.
 */
public abstract class EmptyScreen extends JFrame {

    protected Controller controller;

    protected static final Dimension DEFAULT_DIM = new Dimension(400, 400);
    protected JPanel mainPanel;

    public EmptyScreen(String s, Controller controller) {
        super(s);
        this.controller = controller;
        init();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        mainPanel = new JPanel();
        mainPanel.setSize(DEFAULT_DIM);
        mainPanel.setMaximumSize(DEFAULT_DIM);
        mainPanel.setMinimumSize(DEFAULT_DIM);
        mainPanel.setPreferredSize(DEFAULT_DIM);
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);
        pack();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Controller getController() {
        return controller;
    }

    public void refresh(FlowArgs args) {

    }
}
