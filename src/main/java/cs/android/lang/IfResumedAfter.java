package cs.android.lang;

import cs.android.viewbase.CSViewController;
import cs.java.callback.Run;

import static cs.java.lang.Lang.doLater;

public abstract class IfResumedAfter implements Run {

    public IfResumedAfter(final CSViewController parent, int milliseconds) {
        doLater(milliseconds, new Run() {
            @Override
            public void run() {
                if (parent.isResumed()) IfResumedAfter.this.run();
                onFinally();
            }
        });
    }

    protected void onFinally() {
    }
}
