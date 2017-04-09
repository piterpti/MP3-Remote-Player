package pl.piterpti.flow;

/**
 * Created by piter on 09.04.17.
 */
public class TestFlow extends Flow {

    @SuppressWarnings("unused")
    public static String FLOW_NAME = "TestFlow";

    public TestFlow() {
        super();
        SCREEN_NAME = "MainScreen";
    }

    @Override
    public void run() {
        logger.info("Run method");
        while (runFlow) {
            break;
        }
    }
}
