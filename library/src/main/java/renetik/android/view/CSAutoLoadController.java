package renetik.android.view;

import renetik.android.lang.CSWork;
import renetik.android.rpc.CSResponse;
import renetik.android.viewbase.CSViewController;
import renetik.android.java.callback.CSReturn;

import static renetik.android.lang.CSLang.MINUTE;
import static renetik.android.lang.CSLang.schedule;

/**
 * Created by renetik on 29/12/17.
 */

public class CSAutoLoadController<RowType> extends CSViewController {

    private final CSWork _autoReload;

    public CSAutoLoadController(CSViewController parent, CSReturn<CSResponse> runWith) {
        super(parent);
        _autoReload = schedule(MINUTE, runWith::invoke);
    }

    protected void onViewShowing() {
        super.onViewShowing();
        _autoReload.start();
    }

    protected void onViewHiding() {
        super.onViewHiding();
        _autoReload.stop();
    }

}
