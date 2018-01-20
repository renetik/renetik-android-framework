package cs.android.viewbase;

import android.view.ViewGroup;

import cs.android.viewbase.menu.CSOnMenuItem;
import cs.java.lang.CSValue;

import static android.support.v7.appcompat.R.anim;
import static android.view.animation.AnimationUtils.loadAnimation;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.is;

public class CSInViewController extends CSViewController<ViewGroup> {

    private CSViewController _controller;

    public CSInViewController(CSViewController parent, int viewGroupId) {
        super(parent, viewGroupId);
    }

    public CSViewController openedController() {
        return _controller;
    }

    protected void onBack(CSValue<Boolean> goBack) {
        super.onBack(goBack);
        if (isControllerOpened() && goBack.getValue()) {
            goBack.set(NO);
            closeController();
        }
    }

    protected void onOptionsItemSelected(CSOnMenuItem event) {
        super.onOptionsItemSelected(event);
        if (isControllerOpened() && event.consume(android.R.id.home)) goBack();
    }

    public CSView<ViewGroup> closeController() {
        closeController(YES);
        return this;
    }

    public void closeController(boolean animation) {
        if (!isControllerOpened()) return;
        CSViewController controller = _controller;
        _controller = null;
        controller.onHideByInViewController();
        if (animation)
            controller.asView().startAnimation(loadAnimation(context(), anim.abc_shrink_fade_out_from_bottom));
        controller.onDeinitialize(getState());
        asView().removeView(controller.asView());
        parentController().onInViewControllerHide(controller);
        controller.onDestroy();
    }

    public boolean isControllerOpened() {
        return is(openedController());
    }

    public void openController(final CSViewController controller) {
        if (isControllerOpened()) closeController(NO);
        openControllerImpl(controller);
    }

    private void openControllerImpl(CSViewController controller) {
        _controller = controller;
        asView().addView(controller.asView());
        parentController().onInViewControllerShow(controller);
        controller.initialize();
        controller.asView().startAnimation(loadAnimation(context(), anim.abc_grow_fade_in_from_bottom));
    }

}
