package cs.android.view;

import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import cs.android.rpc.CSResponse;
import cs.android.viewbase.CSViewController;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.animation.AnimationUtils.loadAnimation;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.set;

public class CSProgressInViewController extends CSViewController {

    private int _cancelId;
    private int _determinateBarId;
    private int _labelId;
    private CSResponse<?> _response;
    private int _indeterminateProgressBarId;
    private boolean _barVisible;
    private int _inViewGroupId;
    private boolean _isControllerOpened;

    public CSProgressInViewController(CSViewController parent, int inViewGroupId, int layout, int labelId, int determinateBarId, int indeterminateBarId) {
        super(parent, layout(layout));
        _inViewGroupId = inViewGroupId;
        _labelId = labelId;
        _determinateBarId = determinateBarId;
        _indeterminateProgressBarId = indeterminateBarId;
    }

    public CSProgressInViewController(CSViewController parent, int inViewGroupId, int layout, int labelId, int determinateBarId, int indeterminateBarId, int cancelId) {
        this(parent, inViewGroupId, layout, labelId, determinateBarId, indeterminateBarId);
        _cancelId = cancelId;
    }

    public ProgressBar showBar() {
        _barVisible = YES;
        updateBars();
        return view(_determinateBarId).asProgressBar();
    }

    public CSProgressInViewController setResponse(final CSResponse<?> response) {
        if (no(response)) return null;
        if (response.isDone()) return null;
        if (is(_response)) return null;
        response.onSend(() -> showProgress(response)).onDone(() -> hideProgress());
        update(response);
        return this;
    }


    public CSProgressInViewController showProgress() {
        return showProgress(null);
    }

    private CSProgressInViewController showProgress(final CSResponse response) {
        _response = response;
        if (_isControllerOpened) return this;
        if (is(response)) view(_labelId).text(response.title());
        updateBars();
        updateCancelButton(response);
        view(parentController().findView(_inViewGroupId)).add(this, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        asView().startAnimation(loadAnimation(context(), android.support.v7.appcompat.R.anim.abc_grow_fade_in_from_bottom));
        _isControllerOpened = YES;
        return this;
    }

    private void updateCancelButton(final CSResponse response) {
        if (set(_cancelId)) {
            if (is(response))
                view(_cancelId).onClick(v -> {
                    try {
                        response.cancel();
                    } catch (Exception e) {
                        error(e);
                    }
                    hideProgress();
                }).show();
            else view(_cancelId).hide();
        }
    }

    private void onDismiss() {
        _barVisible = NO;
        _response = null;
    }

    private void updateBars() {
        view(_indeterminateProgressBarId).visible(!_barVisible);
        view(_determinateBarId).visible(_barVisible);
    }

    private void update(CSResponse response) {
        if (is(response) && response.isSending()) showProgress(response);
        else hideProgress();
    }

    public void hideProgress() {
        if (!_isControllerOpened) return;
        view(parentController().findView(_inViewGroupId)).removeView(this);
        onDismiss();
    }

}
