package cs.java.event;

import java.util.Objects;

/**
 * Created by Rene Dohan on 22/10/17.
 */

public class CSListenerUnchecked<T> implements CSListener<T> {
    public void onEvent(CSEvent.EventRegistration registration, T arg) {

    }
}
