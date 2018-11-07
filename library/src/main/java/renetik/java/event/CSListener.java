package renetik.java.event;

import renetik.java.event.CSEvent.CSEventRegistration;

public interface CSListener<T> {
    void onEvent(CSEventRegistration registration, T arg);
}
