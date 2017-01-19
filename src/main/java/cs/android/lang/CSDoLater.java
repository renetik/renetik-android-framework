package cs.android.lang;

import android.os.Handler;

import static cs.java.lang.CSLang.exception;

public class CSDoLater {
    private static Handler handler;
    private final Runnable _runnable;

    public CSDoLater(Runnable runnable, int delayMilliseconds) {
        _runnable = runnable;
        if (!handler.postDelayed(_runnable, delayMilliseconds)) throw exception();
    }

    public CSDoLater(Runnable runnable) {
        _runnable = runnable;
        if (!handler.post(_runnable)) throw exception();
    }

    public static void initialize() {
        handler = new Handler();
    }

    public void stop() {
        handler.removeCallbacks(_runnable);
    }

}
