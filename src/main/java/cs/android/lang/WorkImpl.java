package cs.android.lang;

import cs.java.callback.Run;
import cs.java.lang.Lang.*;

import static cs.java.lang.Lang.*;

public class WorkImpl implements Work {
    private int _delay_milliseconds;
    private final Run _runnable;
    private boolean _stop = true;
    private DoLaterProcess _doLater;

    public WorkImpl(int delay_milliseconds, Run runnable) {
        _delay_milliseconds = delay_milliseconds;
        _runnable = runnable;
    }

    public Work run() {
        _runnable.run();
        return this;
    }

    public boolean isStarted() {
        return !_stop;
    }

    public void start(boolean start) {
        if (start) start();
        else stop();
    }

    public void interval(int interval) {
        _delay_milliseconds = interval;
    }

    public int interval() {
        return _delay_milliseconds;
    }

    public Work start() {
        if (_stop) {
            _stop = false;
            process();
        }
        return this;
    }

    public void stop() {
        _stop = true;
        if (is(_doLater)) _doLater.stop();
    }

    private void process() {
        _doLater = doLater(_delay_milliseconds, new Run() {
            public void run() {
                if (!_stop) {
                    _runnable.run();
                    process();
                }
            }
        });
    }
}
