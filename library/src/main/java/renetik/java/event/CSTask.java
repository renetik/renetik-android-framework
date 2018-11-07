package renetik.java.event;

import renetik.android.viewbase.CSViewController;
import renetik.java.event.CSEvent.CSEventRegistration;

import static renetik.android.lang.CSLang.is;

public class CSTask<Argument> extends CSEventRegistrations implements CSListener<Argument> {

    private CSListener<Argument> _listener;
    private CSViewController _parent;

    public CSTask(CSViewController parent, CSEvent<Argument> event) {
        _parent = parent;
        _parent.onPause.add((r, arg) -> {
            r.cancel();
            cancel();
        });
        add(event.add(this));
    }

    public CSTask onEvent(CSListener<Argument> listener) {
        _listener = listener;
        return this;
    }

    public void onEvent(CSEventRegistration registration, Argument argument) {
        if (is(_parent) && _parent.isPaused()) return;
        _listener.onEvent(registration, argument);
    }

}