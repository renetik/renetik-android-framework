package cs.android.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ProgressBar;

import cs.android.rpc.Response;
import cs.android.view.adapter.OnClick;
import cs.android.viewbase.CSViewController;
import cs.java.callback.Run;

import static android.graphics.Color.TRANSPARENT;
import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;
import static cs.java.lang.Lang.set;

public class CSProgressController extends CSViewController {

    private int _cancelId;
    private int _determinateBarId;
    private int _labelId;
    private Response<?> _response;
    private int _indeterminateProgressBarId;
    private Dialog _dialog;
    private boolean _barVisible;

    public CSProgressController(CSViewController parent, int layout, int labelId, int determinateBarId, int indeterminateBarId) {
        super(parent, layout(layout));
        _labelId = labelId;
        _determinateBarId = determinateBarId;
        _indeterminateProgressBarId = indeterminateBarId;
    }

    public CSProgressController(CSViewController parent, int layout, int labelId, int determinateBarId, int indeterminateBarId, int cancelId) {
        this(parent, layout, labelId, determinateBarId, indeterminateBarId);
        _cancelId = cancelId;
    }

    public ProgressBar showBar() {
        _barVisible = YES;
        updateBars();
        return view(_determinateBarId, ProgressBar.class).asView();
    }

    public void setResponse(final Response<?> response) {
        if (no(response)) return;
        if (response.isDone()) return;
        if (is(_response)) return;
        response.onSend(new Run() {
            public void run() {
                showProgress(response);
            }
        }).onDone(new Run() {
            public void run() {
                hideProgress();
            }
        });
        update(response);
    }

    public CSProgressController showProgress() {
        return showProgress(null);
    }

    private CSProgressController showProgress(final Response response) {
        _response = response;
        if (isProgressVisible()) return this;
        if (is(response)) view(_labelId).text(response.title());
        updateBars();
        if (set(_cancelId)) {
            if (is(response))
                view(_cancelId).onClick(new OnClick() {
                    public void onClick(View v) {
                        response.cancel();
                        hideProgress();
                    }
                }).show();
            else view(_cancelId).hide();
        }
        _dialog = new Dialog(context());
        if (is(_dialog.getWindow()))
            _dialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        _dialog.setContentView(asView());
        _dialog.setCancelable(NO);
        _dialog.setCanceledOnTouchOutside(NO);
        _dialog.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                response.cancel();
            }
        });
        _dialog.show();
        return this;
    }

    private void onDismiss() {
        setView(null);
        _barVisible = NO;
        _dialog = null;
        _response = null;
    }

    private void updateBars() {
        view(_indeterminateProgressBarId).visible(!_barVisible);
        view(_determinateBarId).visible(_barVisible);
    }

    private void update(Response response) {
        if (is(response) && response.isSending())
            showProgress(response);
        else hideProgress();
    }

    public void hideProgress() {
        if (!isProgressVisible()) return;
        _dialog.dismiss();
        onDismiss();
    }

    private boolean isProgressVisible() {
        return is(_dialog);
    }
}
