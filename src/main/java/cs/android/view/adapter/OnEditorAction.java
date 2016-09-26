package cs.android.view.adapter;

import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import cs.java.callback.RunWith;

import static cs.java.lang.Lang.empty;
import static cs.java.lang.Lang.equalOne;
import static cs.java.lang.Lang.is;

public abstract class OnEditorAction implements OnEditorActionListener, RunWith<TextView> {

    private int _action;

    public OnEditorAction() {
    }

    public OnEditorAction(int action) {
        _action = action;
    }

    protected boolean isSubmitAction(int actionId, KeyEvent event) {
        return equalOne(actionId, EditorInfo.IME_ACTION_SEARCH, EditorInfo.IME_ACTION_DONE) ||
                is(event) && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER;
    }

    public boolean onEditorAction(TextView view, int action, KeyEvent event) {
        if (empty(_action)) {
            if (isSubmitAction(action, event)) {
                run(view);
                return true;
            }
        } else if (action == _action) {
            run(view);
            return true;
        }
        return false;
    }
}
