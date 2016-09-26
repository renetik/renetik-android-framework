package cs.android.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cs.android.viewbase.ContextController;
import cs.java.callback.RunWith;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;
import static cs.java.lang.Lang.*;

public class Reachability extends ContextController {

    private final CSEvent _eventOnConnected = event();
    private final CSEvent _eventOnDisConnected = event();
    private final CSEvent _eventOnStateChanged = event();

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            asString(intent);
            onNetworkStateChange();
        }
    };
    private boolean _started;

    public Reachability() {
    }

    public Reachability start() {
        if (_started) return this;
        context().registerReceiver(receiver, new IntentFilter(CONNECTIVITY_ACTION));
        _started = YES;
        return this;
    }

    public Reachability stop() {
        if (!_started) return this;
        _started = NO;
        unregisterReceiver(receiver);
        return this;
    }

    private void onNetworkStateChange() {
        fire(_eventOnStateChanged);
        if (isNetworkConnected())
            onNetworkConnected();
        else onNetworkDisconnected();
    }

    protected void onNetworkConnected() {
        fire(_eventOnConnected);
    }

    protected void onNetworkDisconnected() {
        fire(_eventOnDisConnected);
    }

    public Reachability onConnected(final RunWith<Reachability> runWith) {
        _eventOnConnected.add(new CSListener() {
            public void onEvent(EventRegistration registration, Object arg) {
                runWith.run(Reachability.this);
            }
        });
        return this;
    }

    public Reachability onDisconnected(final RunWith<Reachability> runWith) {
        _eventOnDisConnected.add(new CSListener() {
            public void onEvent(EventRegistration registration, Object arg) {
                runWith.run(Reachability.this);
            }
        });
        return this;
    }

    public Reachability onStateChanged(final RunWith<Reachability> runWith) {
        _eventOnStateChanged.add(new CSListener() {
            public void onEvent(EventRegistration registration, Object arg) {
                runWith.run(Reachability.this);
            }
        });
        return this;
    }

    public boolean started() {
        return _started;
    }
}
