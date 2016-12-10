package cs.java.event;

import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;
import cs.java.event.CSEvent.EventRegistration;

import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.list;

public class CSTask<Argument> implements CSListener<Argument> {

    protected Object _argument;
    protected EventRegistration _registration;
    protected CSList<EventRegistration> _registrations = list();
    private CSListener _listener;
    private CSViewController _parent;

    @SafeVarargs
    public CSTask(CSEvent<Argument>... events) {
        for (CSEvent<Argument> event : events) _registrations.add(event.add(this));
    }

    @SafeVarargs
    public CSTask(CSViewController parent, CSEvent<Argument>... events) {
        this._parent = parent;
        for (CSEvent<Argument> event : events) _registrations.add(event.add(this));
        parent.onPause.add(new CSListener<Void>() {
            public void onEvent(EventRegistration r, Void arg) {
                r.cancel();
                cancel();
            }
        });
    }

    public void cancel() {
        for (EventRegistration reg : _registrations) reg.cancel();
        _registrations.clear();
    }

    public CSTask add(CSListener listener) {
        _listener = listener;
        return this;
    }

    public void onEvent(EventRegistration registration, Argument argument) {
        this._registration = registration;
        this._argument = argument;
        if (is(_parent) && _parent.isPaused())
            return;
        _listener.onEvent(registration, argument);
    }


}