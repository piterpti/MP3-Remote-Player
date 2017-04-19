package pl.piterpti.controller;

import pl.piterpti.message.FlowArgs;

/**
 * Action definition
 */
public class Action {

    private long id;
    private FlowArgs arg;

    Action(long id) {
        this.id = id;
    }

    Action(long id, FlowArgs arg) {
        this.id = id;
        this.arg = arg;
    }

    public long getId() {
        return id;
    }

    public FlowArgs getArg() {
        return arg;
    }
}