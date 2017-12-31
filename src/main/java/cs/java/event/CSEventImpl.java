package cs.java.event;

import cs.java.collections.CSList;
import cs.java.lang.Base;

import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.list;

public class CSEventImpl<T> extends Base implements CSEvent<T> {

    private final CSList<EventRegistrationImpl> _registrations = list();
    private final CSList<EventRegistrationImpl> _toRemove = list();
    private final CSList<EventRegistrationImpl> _toAdd = list();
    private boolean _running;

    public CSEventImpl() {
    }

    @Override
    public EventRegistration add(final CSListener listener) {
        EventRegistrationImpl registration = new EventRegistrationImpl(listener);
        if (_running) _toAdd.add(registration);
        else _registrations.add(registration);
        return registration;
    }

    @Override
    public void fire(T argument) {
        if (_running) error(exception("Event run while _running"));
        if (_registrations.isEmpty()) return;
        _running = true;
        for (EventRegistrationImpl registration : _registrations)
            registration._listener.onEvent(registration, argument);
        for (EventRegistrationImpl registration : _toRemove)
            _registrations.delete(registration);
        _toRemove.clear();
        _registrations.addAll(_toAdd);
        _toAdd.clear();
        _running = false;
    }

    public void clear() {
        _registrations.clear();
    }

    class EventRegistrationImpl implements EventRegistration {
        private CSListener _listener;

        public EventRegistrationImpl(CSListener listener) {
            _listener = listener;
        }

        public void cancel() {
            int index = _registrations.index(this);
            if (index >= 0) {
                if (_running) _toRemove.add(this);
                else _registrations.remove(index);
            } else error(exception("Listener not found"));
        }

        public CSEvent<?> event() {
            return CSEventImpl.this;
        }
    }

}