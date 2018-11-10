package renetik.android.lang;

import renetik.android.viewbase.CSViewController;
import renetik.android.java.callback.CSRun;

import static renetik.android.lang.CSLang.doLater;

public class CSIfResumedAfter {

    private CSRun _run;

    public CSIfResumedAfter(final CSViewController parent, int milliseconds, CSRun run) {
        _run = run;
        doLater(milliseconds, (CSRun) () -> {
            if (parent.isResumed()) _run.run();
            onFinally();
        });
    }

    protected void onFinally() {
    }
}
