package pl.piterpti.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Args fot actions
 */
public class FlowArgs implements Serializable {

    private static final long serialVersionUID = 1L;

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

    public Object getFirstOfType(Class<?> aClass) {
        for (Map.Entry<String, Object> el : args.entrySet()) {
            if (aClass.isAssignableFrom(el.getValue().getClass())) {
                return el.getValue();
            }
        }

        return null;
    }
}
