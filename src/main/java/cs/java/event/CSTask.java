package cs.java.event;

import cs.android.viewbase.CSViewController;
import cs.java.event.CSEvent.EventRegistration;

import static cs.java.lang.CSLang.is;

public class CSTask<Argument> extends CSEventRegistrations implements CSListener<Argument> {

    private CSListener<Argument> _listener;
    private CSViewController _parent;

    public CSTask(CSEvent<Argument>... events) {
        register(events);
    }

    public CSTask(CSViewController parent, CSEvent<Argument>... events) {
        (_parent = parent).onPause.add((r, arg) -> {
            r.cancel();
            cancel();
        });
        register(events);
    }

    public CSTask onEvent(CSListener<Argument> listener) {
        _listener = listener;
        return this;
    }

    public CSTask register(CSEvent<Argument>... events) {
        for (CSEvent<Argument> event : events) register(event.add(this));
        return this;
    }

    public void onEvent(EventRegistration registration, Argument argument) {
        if (is(_parent) && _parent.isPaused()) return;
        _listener.onEvent(registration, argument);
    }

}