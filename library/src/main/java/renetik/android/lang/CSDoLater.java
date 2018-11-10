package renetik.android.lang;

import android.os.Handler;

import static renetik.android.lang.CSLang.error;
import static renetik.android.lang.CSLang.exception;

public class CSDoLater {

    private static Handler handler;
    private final Runnable _runnable;

    public CSDoLater(Runnable runnable, int delayMilliseconds) {
        _runnable = runnable;
        if (!handler.postDelayed(_runnable, delayMilliseconds))
            error(exception("Runnable not run"));
    }

    public CSDoLater(Runnable runnable) {
        _runnable = runnable;
        if (!handler.post(_runnable)) error(exception("Runnable not run"));
    }

    public static void initializeHandler() {
        handler = new Handler();
    }

    public void stop() {
        handler.removeCallbacks(_runnable);
    }

}
