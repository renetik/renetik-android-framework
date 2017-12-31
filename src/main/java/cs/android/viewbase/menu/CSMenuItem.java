package cs.android.viewbase.menu;

import cs.android.viewbase.CSViewController;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.callback.CSRunWithWith;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.is;

/**
 * Created by Rene Dohan on 18/1/17.
 */
public class CSMenuItem {
    private String _title;
    private CSViewController _controller;
    private int _id;
    private CSRun _run;
    private boolean _visible = YES;
    private CSRunWith<Boolean> _runCheckable;
    private boolean _isChecked;
    private CSRunWithWith<CSMenuItem, Boolean> _runWith;

    public CSMenuItem(CSViewController controller, int id) {
        _controller = controller;
        _id = id;
    }

    public CSMenuItem(CSViewController controller, String title) {
        _controller = controller;
        _title = title;
    }

    public CSMenuItem onClick(CSRun run) {
        _run = run;
        return this;
    }

    public CSMenuItem onClick(CSRunWith<Boolean> run) {
        _runCheckable = run;
        return this;
    }

    public CSMenuItem onClick(CSRunWithWith<CSMenuItem, Boolean> run) {
        _runWith = run;
        return this;
    }

    public int id() {
        return _id;
    }

    public void run() {
        if (is(_run)) _run.run();
        if (is(_runWith)) _runWith.run(this, null);
    }

    public void run(boolean isChecked) {
        if (is(_runCheckable)) _runCheckable.run(isChecked);
        if (is(_runWith)) _runWith.run(this, isChecked);
    }

    public CSMenuItem hide() {
        visible(NO);
        return this;
    }

    public boolean isVisible() {
        return _visible;
    }

    public CSMenuItem show() {
        visible(YES);
        return this;
    }

    public CSMenuItem visible(boolean visible) {
        _visible = visible;
        if (_controller.isCreated()) _controller.invalidateOptionsMenu();
        return this;
    }

    public void checked(boolean checked) {
        _isChecked = checked;
    }

    public boolean isChecked() {
        return _isChecked;
    }

    public void onChecked(CSOnMenuItem onItem) {
        checked(!isChecked());
        onItem.checked(isChecked());
        run(isChecked());
    }
}
