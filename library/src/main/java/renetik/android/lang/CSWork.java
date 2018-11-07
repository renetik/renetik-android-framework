package renetik.android.lang;

import renetik.java.callback.CSRun;

import static renetik.android.lang.CSLang.YES;
import static renetik.android.lang.CSLang.doLater;
import static renetik.android.lang.CSLang.is;

public class CSWork {
    private int _delay_milliseconds;
    private final CSRun _workToInvoke;
    private boolean _stop = YES;
    private CSDoLater _doLater;

    public CSWork(int delay_milliseconds, CSRun workToInvoke) {
        _delay_milliseconds = delay_milliseconds;
        _workToInvoke = workToInvoke;
    }

    public CSWork run() {
        _workToInvoke.run();
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

    public CSWork start() {
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
        _doLater = doLater(_delay_milliseconds, new CSRun() {
            public void run() {
                if (!_stop) {
                    _workToInvoke.run();
                    process();
                }
            }
        });
    }
}
