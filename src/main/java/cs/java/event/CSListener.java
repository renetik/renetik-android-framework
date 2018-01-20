package cs.java.event;

import cs.java.event.CSEvent.CSEventRegistration;

public interface CSListener<T> {
    void onEvent(CSEventRegistration registration, T arg);
}
