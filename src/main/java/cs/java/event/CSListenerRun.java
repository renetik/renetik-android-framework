package cs.java.event;

import cs.java.callback.Run;

/**
 * Created by renedohan on 23/07/15.
 */
public abstract class CSListenerRun implements CSListener<Void>, Run {
    public void onEvent(CSEvent.EventRegistration registration, Void arg) {
        run();
    }

}
