package cs.android.lang;

import cs.android.viewbase.CSViewController;
import cs.java.callback.CSRun;

import static cs.java.lang.CSLang.doLater;

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
