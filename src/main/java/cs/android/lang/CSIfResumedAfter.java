package cs.android.lang;

import cs.android.viewbase.CSViewController;
import cs.java.callback.CSRun;

import static cs.java.lang.CSLang.doLater;

public abstract class CSIfResumedAfter implements CSRun {

    public CSIfResumedAfter(final CSViewController parent, int milliseconds) {
        doLater(milliseconds, new CSRun() {
            @Override
            public void run() {
                if (parent.isResumed()) CSIfResumedAfter.this.run();
                onFinally();
            }
        });
    }

    protected void onFinally() {
    }
}
