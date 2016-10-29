package cs.android.rpc;

import cs.android.lang.IfResumedAfter;
import cs.android.util.Reachability;
import cs.android.viewbase.CSViewController;
import cs.java.callback.Run;

import static cs.java.lang.Lang.SECOND;
import static cs.java.lang.Lang.no;

public abstract class SuccessRequestManager<T> extends CSViewController {

    private Reachability reachability;

    public SuccessRequestManager(CSViewController parent) {
        super(parent);
    }

    public void start() {
        if (isNetworkConnected()) process();
        else reachability = new Reachability() {
            @Override
            protected void onNetworkConnected() {
                new IfResumedAfter(SuccessRequestManager.this, 3 * SECOND) {
                    public void run() {
                        process();
                    }
                };
                stopReachability();
            }
        }.start();
    }

    private void process() {
        createRequest().onFailed(new Run() {
            public void run() {
                new IfResumedAfter(SuccessRequestManager.this, 15 * SECOND) {
                    public void run() {
                        start();
                    }
                };
            }
        });
    }

    protected abstract Response<T> createRequest();

    public void onPause() {
        super.onPause();
        stopReachability();
    }

    private void stopReachability() {
        if (no(reachability)) return;
        reachability.stop();
        reachability = null;
    }
}
