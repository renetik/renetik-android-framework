package cs.java.event;

import cs.java.callback.CSRun;

/**
 * Created by renedohan on 23/07/15.
 */
public abstract class CSListenerArg<Arg> implements CSListener<Arg>, CSRun {

    private Arg _arg;

    public void onEvent(CSEvent.EventRegistration registration, Arg arg) {
        _arg = arg;
        run();
    }

    public Arg arg() {
        return _arg;
    }
}
