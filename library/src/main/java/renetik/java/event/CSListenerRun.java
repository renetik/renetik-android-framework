package renetik.java.event;

import renetik.java.callback.CSRun;

/**
 * Created by renedohan on 23/07/15.
 */
public abstract class CSListenerRun implements CSListener<Void>, CSRun {
    public void onEvent(CSEvent.CSEventRegistration registration, Void arg) {
        run();
    }

}
