package pl.piterpti.controller;

/**
 * Created by piter on 10.04.17.
 */

import pl.piterpti.flow.FlowArgs;

public class Action {

    private long id;
    private FlowArgs arg;

    public Action(long id) {
        this.id = id;
    }

    public Action(long id, FlowArgs arg) {
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