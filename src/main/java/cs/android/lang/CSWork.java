package cs.android.lang;

import cs.java.callback.CSRun;

import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.is;

public class CSWork {
    private int _delay_milliseconds;
    private final CSRun _runnable;
    private boolean _stop = true;
    private CSDoLater _doLater;

    public CSWork(int delay_milliseconds, CSRun runnable) {
        _delay_milliseconds = delay_milliseconds;
        _runnable = runnable;
    }

    public CSWork run() {
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
                    _runnable.run();
                    process();
                }
            }
        });
    }
}
