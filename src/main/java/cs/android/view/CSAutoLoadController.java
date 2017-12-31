package cs.android.view;

import cs.android.lang.CSWork;
import cs.android.rpc.CSResponse;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSReturn;

import static cs.java.lang.CSLang.MINUTE;
import static cs.java.lang.CSLang.schedule;

/**
 * Created by renetik on 29/12/17.
 */

public class CSAutoLoadController<RowType> extends CSViewController {

    private final CSWork _autoReload;

    public CSAutoLoadController(CSViewController parent, CSReturn<CSResponse> runWith) {
        super(parent);
        _autoReload = schedule(MINUTE, runWith::invoke);
    }

    public void onResume() {
        super.onResume();
        _autoReload.start().run();
    }

    public void onPause() {
        super.onPause();
        _autoReload.stop();
    }

}
