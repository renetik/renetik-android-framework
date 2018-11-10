package renetik.android.java.event;

import renetik.android.java.event.CSEvent.CSEventRegistration;

public interface CSListener<T> {
    void onEvent(CSEventRegistration registration, T arg);
}
