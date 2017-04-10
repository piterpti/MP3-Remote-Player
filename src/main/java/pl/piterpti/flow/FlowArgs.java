package pl.piterpti.flow;

import java.util.HashMap;

/**
 * Created by piter on 10.04.17.
 */
public class FlowArgs {

    private HashMap<String, Object> args = new HashMap<>();

    public FlowArgs() {

    }

    public FlowArgs(String s,Object o) {
        args.put(s, o);
    }

    public void addArg(String s, Object o) {
        args.put(s, o);
    }

    public HashMap<String, Object> getArgs() {
        return args;
    }

    @Override
    public String toString() {
        return "FlowArgs{" +
                "args=" + args +
                '}';
    }
}
