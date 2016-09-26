package cs.android.lang;

import android.os.Handler;

import static cs.java.lang.Lang.exception;

public class DoLaterProcess {
    private static Handler handler;
    private final Runnable _runnable;

    public DoLaterProcess(Runnable runnable, int delayMilliseconds) {
        _runnable = runnable;
        if (!handler.postDelayed(_runnable, delayMilliseconds)) throw exception();
    }

    public DoLaterProcess(Runnable runnable) {
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
