package cs.java.event;

import cs.java.collections.CSList;
import cs.java.lang.Base;

import static cs.java.lang.CSLang.*;

public class CSEventImpl<T> extends Base implements CSEvent<T> {

    private final CSList<EventRegistrationImpl> _registrations = list();
    private final CSList<EventRegistrationImpl> _toRemove = list();
    private final CSList<EventRegistrationImpl> _toAdd = list();
    private boolean running;

    public CSEventImpl() {
    }

    @Override
    public EventRegistration add(final CSListener listener) {
        EventRegistrationImpl registration = new EventRegistrationImpl(listener);
        if (running) _toAdd.add(registration);
        else _registrations.add(registration);
        return registration;
    }

    @Override
    public void fire(T argument) {
        if (running) throw exception("Event run while running");
        if (_registrations.isEmpty()) return;

        running = true;
        for (EventRegistrationImpl registration : _registrations)
            registration._listener.onEvent(registration, argument);
        for (EventRegistrationImpl registration : _toRemove)
            _registrations.delete(registration);
        _toRemove.clear();
        for (EventRegistrationImpl registration : _toAdd)
            _registrations.add(registration);
        _toAdd.clear();
        running = false;
    }

    public void clear() {
        _registrations.clear();
    }

    class EventRegistrationImpl implements EventRegistration {
        private CSListener _listener;

        public EventRegistrationImpl(CSListener listener) {
            this._listener = listener;
        }

        @Override
        public void cancel() {
            int index = _registrations.index(this);
            if (index < 0) throw exception("Listener not found");
            if (running) _toRemove.add(this);
            else _registrations.remove(index);
        }

        @Override
        public CSEvent<?> event() {
            return CSEventImpl.this;
        }
    }

}