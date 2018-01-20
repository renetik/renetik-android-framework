package cs.java.event;

import cs.java.collections.CSList;
import cs.java.lang.Base;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;

public class CSEventImpl<T> extends Base implements CSEvent<T> {

    private CSList<EventRegistrationImpl> _registrations = list();
    private CSList<EventRegistrationImpl> _toRemove = list();
    private CSList<EventRegistrationImpl> _toAdd = list();
    private boolean _running;

    public CSEventImpl() {
    }

    public CSEventRegistration add(final CSListener listener) {
        EventRegistrationImpl registration = new EventRegistrationImpl(listener);
        if (_running) registrationsToAdd().add(registration);
        else registrations().add(registration);
        return registration;
    }

    public void fire(T argument) {
        if (_running) error(exception("Event run while _running"));
        if (registrations().isEmpty()) return;
        _running = true;
        for (EventRegistrationImpl registration : registrations()) registration.onEvent(argument);
        for (EventRegistrationImpl registration : registrationsToRemove())
            registrations().delete(registration);
        registrationsToRemove().clear();
        registrations().addAll(registrationsToAdd());
        registrationsToAdd().clear();
        _running = false;
    }

    public void clear() {
        registrations().clear();
    }

    public CSList<EventRegistrationImpl> registrations() {
        return is(_registrations) ? _registrations : (_registrations = list());
    }

    public CSList<EventRegistrationImpl> registrationsToRemove() {
        return is(_toRemove) ? _toRemove : (_toRemove = list());
    }

    public CSList<EventRegistrationImpl> registrationsToAdd() {
        return is(_toAdd) ? _toAdd : (_toAdd = list());
    }

    class EventRegistrationImpl implements CSEventRegistration {
        private CSListener _listener;
        private boolean _active = YES;

        public EventRegistrationImpl(CSListener listener) {
            _listener = listener;
        }

        public void cancel() {
            int index = registrations().index(this);
            if (index >= 0) {
                if (_running) registrationsToRemove().add(this);
                else registrations().remove(index);
            } else error(exception("Listener not found"));
        }

        public CSEvent<?> event() {
            return CSEventImpl.this;
        }

        public CSEventRegistration setActive(boolean active) {
            _active = active;
            return this;
        }

        public boolean isActive() {
            return _active;
        }

        public void onEvent(T argument) {
            if (_active) _listener.onEvent(this, argument);
        }
    }

}