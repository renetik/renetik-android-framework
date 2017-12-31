package cs.java.event;

import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;
import cs.java.event.CSEvent.EventRegistration;

import static cs.java.lang.CSLang.list;

/**
 * Created by renetik on 26/12/17.
 */

public class CSEventRegistrations {

    private CSList<EventRegistration> _registrations = list();

    public CSEventRegistrations() {
    }

    public CSEventRegistrations(EventRegistration... registrations) {
        _registrations.append(registrations);
    }

    public void cancel() {
        for (EventRegistration reg : _registrations) reg.cancel();
        _registrations.clear();
    }

    public CSEventRegistrations register(EventRegistration... registrations) {
        _registrations.append(registrations);
        return this;
    }
}
