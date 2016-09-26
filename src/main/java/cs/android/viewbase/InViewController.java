package cs.android.viewbase;

import android.view.animation.AnimationUtils;

import cs.java.lang.Value;

import static android.support.v7.appcompat.R.*;
import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;
import static cs.java.lang.Lang.is;

public class InViewController extends CSViewController {

    private int _frameId;
    private CSViewController _controller;
    private CSViewController _parent;

    public InViewController(CSViewController parent, int frameId) {
        super(parent);
        _parent = parent;
        _frameId = frameId;
    }

    public CSViewController controller() {
        return _controller;
    }

    public void onBackPressed(Value<Boolean> goBack) {
        super.onBackPressed(goBack);
        if (isVisible() && goBack.get()) {
            goBack.set(NO);
            hide();
        }
    }

    protected void onOptionsItemSelected(OnMenuItem event) {
        super.onOptionsItemSelected(event);
        if (isVisible() && event.consume(android.R.id.home)) {
            goBack();
        }
    }

    public void hide() {
        hideController(YES);
    }

    public void hideController(boolean animation) {
        if (!isVisible()) return;
        CSViewController controller = _controller;
        _controller = null;
        controller.onHideByInViewController();
        if (animation)
            controller.asView().startAnimation(AnimationUtils.loadAnimation(context(), anim.abc_shrink_fade_out_from_bottom));
        controller.onDeinitialize(getState());
        getViewGroup(_frameId).removeView(controller.asView());
        controller.onDestroy();
        _parent.onInViewControllerHide(controller);
    }

    public boolean isHidden() {
        return !isVisible();
    }

    public boolean isVisible() {
        return is(_controller);
    }

    public void showController(final CSViewController controller) {
        if (isVisible()) hideController(NO);
        showControllerImpl(controller);
    }

    private void showControllerImpl(CSViewController controller) {
        _controller = controller;
        getViewGroup(_frameId).addView(controller.asView());
        _parent.onInViewControllerShow(controller);
        controller.onInitialize();
        controller.asView().startAnimation(AnimationUtils.loadAnimation(context(), anim.abc_grow_fade_in_from_bottom));
    }

}
