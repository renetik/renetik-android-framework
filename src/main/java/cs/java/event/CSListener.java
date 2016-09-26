package cs.java.event;

import cs.java.event.CSEvent.EventRegistration;

public interface CSListener<T> {
    void onEvent(EventRegistration registration, T arg);
}
