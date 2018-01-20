package cs.java.event;

import cs.java.collections.CSList;
import cs.java.event.CSEvent.CSEventRegistration;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.list;

/**
 * Created by renetik on 26/12/17.
 */

public class CSEventRegistrations {

    private CSList<CSEventRegistration> _registrations = list();
    private boolean _active = YES;

    public CSEventRegistrations() {
    }

    public CSEventRegistrations(CSEventRegistration... registrations) {
        _registrations.append(registrations);
    }

    public void cancel() {
        for (CSEventRegistration reg : _registrations) reg.cancel();
        _registrations.clear();
    }

    public CSEventRegistrations addAll(CSEventRegistration... registrations) {
        _registrations.append(registrations);
        return this;
    }

    public CSEventRegistration add(CSEventRegistration registration) {
        registration.setActive(_active);
        return _registrations.put(registration);
    }

    public void setActive(boolean active) {
        _active = active;
        for (CSEventRegistration registration : _registrations) registration.setActive(_active);
    }
}
