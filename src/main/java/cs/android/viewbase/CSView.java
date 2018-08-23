package cs.android.viewbase;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import cs.android.CSContextInterface;
import cs.android.view.list.CSListController;
import cs.java.common.CSPoint;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.as;
import static cs.java.lang.CSLang.exceptionf;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.stringf;

public class CSView<V extends View> extends CSContextController implements CSViewInterface {

    private CSView _viewField;
    private V _view;
    private CSLayoutId _layoutId;
    private ViewGroup _parentContainer;

    public CSView(CSContextInterface context) {
        super(context);
    }

    public CSView(Context context) {
        super(context);
    }

    public CSView(final ViewGroup parentContainer, CSLayoutId layoutId) {
        this(parentContainer.getContext());
        _parentContainer = parentContainer;
        _layoutId = layoutId;
    }

    public CSView(CSContextInterface parent, CSLayoutId layoutId) {
        this(parent);
        _layoutId = layoutId;
    }

    public CSView(CSListController parent, CSLayoutId layoutId) {
        this(parent.asGroup(), layoutId);
    }

    public CSView(CSView<?> parent, int viewId) {
        this((V) parent.findView(viewId));
    }

    public CSView(final V view) {
        super(view.getContext());
        setView(view);
    }

    public static int getTopRelativeTo(View view, View relativeTo) {
        if (view.getParent() == relativeTo) return view.getTop();
        return view.getTop() + getTopRelativeTo((View) view.getParent(), relativeTo);
    }

    protected static CSLayoutId layout(int id) {
        return new CSLayoutId(id);
    }

    public static <T extends View> CSView<T> wrap(View view) {
        if (view.getTag() instanceof CSView) return (CSView) view.getTag();
        return new CSView(view);
    }

    public boolean hasLayout() {
        return is(_layoutId);
    }

    public View inflate(int layout_id) {
        return LayoutInflater.from(context()).inflate(layout_id, null);
    }

    public View inflate(ViewGroup parent, int layoutId) {
        return LayoutInflater.from(context()).inflate(layoutId, parent, NO);
    }

    public View findView(int id) {
        return getView().findViewById(id);
    }

    public View findViewRecursive(int id) {
        View view = findView(id);
        return no(view) && hasParent() ? parentView().findViewRecursive(id) : view;
    }

    public V getView() {
        return no(_view) && hasLayout() ?
                setView((V) (is(_parentContainer) ?
                        inflate(_parentContainer, _layoutId.id) : inflate(_layoutId.id)))
                : _view;
    }

    public int getLayoutWidth() {
        return getView().getLayoutParams().width;
    }

    public AbsListView asAbsListView() {
        return (AbsListView) getView();
    }

    public <A extends Adapter> AdapterView<A> asAdapterView() {
        return (AdapterView<A>) getView();
    }

    public ViewGroup asGroup() {
        return (ViewGroup) getView();
    }

    public ListView asListView() {
        return (ListView) getView();
    }

    public CSPoint center() {
        return new CSPoint(getLeft() + width() / 2, getTop() + height() / 2);
    }

    public int getLeft() {
        return getView().getLeft();
    }

    public int width() {
        return getView().getWidth();
    }

    public int getTop() {
        return getView().getTop();
    }

    public int height() {
        return getView().getHeight();
    }

    public CSView<V> width(int width) {
        setSize(YES, width, YES);
        return this;
    }

    private void setSize(boolean width, int value, boolean dip) {
        LayoutParams params = getView().getLayoutParams();
        if (value > 0 && dip) value = (int) toPixel(value);
        if (width) params.width = value;
        else params.height = value;
        getView().setLayoutParams(params);
    }

    public void fade(boolean fadeIn) {
        if (fadeIn)
            fadeIn();
        else fadeOut();
    }

    public ViewPropertyAnimator fadeIn() {
        return fadeIn(getView());
    }

    public ViewPropertyAnimator fadeOut() {
        return fadeOut(getView());
    }

    public ViewPropertyAnimator fadeIn(final View view) {
        if (isVisible(view)) return null;
        show(view);
        view.setAlpha(0);
        return view.animate().alpha(1.0F).setDuration(150)
                .setInterpolator(new AccelerateDecelerateInterpolator()).setListener(null);
    }

    public ViewPropertyAnimator fadeOut(final View view) {
        if (!isVisible(view) || view.getAlpha() == 0) return null;
        return view.animate().alpha(0F).setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .setListener(new AnimatorListener() {
                    public void onAnimationStart(Animator animation) {
                    }

                    public void onAnimationEnd(Animator animation) {
                        hide(view);
                    }

                    public void onAnimationCancel(Animator animation) {
                    }

                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    public boolean isVisible(View view) {
        if (no(view)) return NO;
        return view.getVisibility() == VISIBLE;
    }

    public boolean isShown(View view) {
        if (no(view)) return NO;
        return view.isShown();
    }

    public boolean hasParent() {
        return is(getView().getParent());
    }

    public void show(View view) {
        view.setVisibility(VISIBLE);
    }

    public void hide(View view) {
        view.setVisibility(GONE);
    }

    public void invisible(View view) {
        view.setVisibility(INVISIBLE);
    }

    public void invisible() {
        invisible(getView());
    }

    public CSView parentView() {
        ViewParent parent = getView().getParent();
        return is(parent) ? item((View) parent) : null;
    }

    public <ViewType extends View> CSView<ViewType> item(ViewType view) {
        return _viewField = new CSView<>(view);
    }

    public CSView<View> item(CSLayoutId layout) {
        return new CSView(this, layout);
    }

    public CSView<View> item() {
        if (no(_viewField)) item(getView());
        return _viewField;
    }

    public void playClick() {
        getView().playSoundEffect(SoundEffectConstants.CLICK);
    }

    public <V extends View> CSView<V> item(Class<V> clazz) {
        return (CSView<V>) _viewField;
    }

    public ViewPropertyAnimator fadeIn(int view) {
        return fadeIn(findView(view));
    }

    public ViewPropertyAnimator fadeOut(int view) {
        return fadeOut(findView(view));
    }

    public CSPoint getLocationOnScreen() {
        int[] location = new int[2];
        getView().getLocationOnScreen(location);
        return new CSPoint(location[0], location[1]);
    }

    public int getTopRelativeTo(View view) {
        return getTopRelativeTo(getView(), view);
    }

    protected V setView(V view) {
        if (view == null)
            throw exceptionf("View is null for %s, so not found in parent or ? context is %s", this, context());
        if (is(view)) view.setTag(this);
        if (is(_view)) _view.setTag(null);
        _view = view;
        return view;
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int id, Class<V> clazz) {
        return (V) findView(id);
    }

    public CSView<V> hide() {
        hide(getView());
        return this;
    }

    public void hide(boolean hide, int viewId, int... viewIds) {
        if (hide) hide(viewId, viewIds);
        else show(viewId, viewIds);
    }

    public void hide(int viewId, int... viewIds) {
        hide(findView(viewId));
        for (int id : viewIds)
            hide(findView(id));
    }

    public void invisible(int viewId, int... viewIds) {
        invisible(findView(viewId));
        for (int id : viewIds)
            invisible(findView(id));
    }

    public void hideKeyboard() {
        hideKeyboard(getView().getWindowToken());
    }

    public boolean isShown() {
        return isShown(getView());
    }

    public CSView<V> onClick(OnClickListener onClickListener) {
        getView().setOnClickListener(onClickListener);
        return this;
    }

    public void setPercentAspectWidth(int viewId, int percent) {
        setPercentAspectWidth(findView(viewId), percent);
    }

    public void setPercentAspectWidth(View view, int percent) {
        float onePercent = getDisplayWidth() / (float) 100;
        float wantedWidth = onePercent * percent;

        float scalingFactor = wantedWidth / view.getLayoutParams().width;
        int scaledHeight = (int) (view.getLayoutParams().height * scalingFactor);

        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int) wantedWidth;
        layoutParams.height = scaledHeight;

        view.setLayoutParams(layoutParams);
    }

    public int getDisplayWidth() {
        return getDefaultDisplay().getWidth();
    }

    public int getDisplayHeight() {
        return getDefaultDisplay().getHeight();
    }

    public void setPercentWidth(int viewId, int percent, int minimal, int maximal) {
        setPercentWidth(findView(viewId), percent, minimal, maximal);
    }

    public void setPercentWidth(View view, int percent, int minimal, int maximal) {
        double onePercent = getDisplayWidth() / 100;
        int wantedSize = (int) (onePercent * percent);
        if (set(minimal) && wantedSize < minimal)
            wantedSize = minimal;
        else if (set(maximal) && wantedSize > maximal) wantedSize = maximal;
        LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = wantedSize;
        view.setLayoutParams(layoutParams);
    }

    public void setPercentWidth(View view, int percent) {
        setPercentWidth(view, percent, 0, 0);
    }

    public CSView<V> show() {
        show(getView());
        return this;
    }

    public void show(boolean visible, int viewId) {
        if (visible)
            show(viewId);
        else hide(viewId);
    }

    public void show(int viewId, int... viewIds) {
        show(findView(viewId));
        for (int id : viewIds)
            show(findView(id));
    }

    public void showKeyboard(View view, int flag) {
        service(Context.INPUT_METHOD_SERVICE, InputMethodManager.class).showSoftInput(view, flag);
    }

    public String text() {
        return asTextView().getText() + "";
    }

    public TextView asTextView() {
        return (TextView) getView();
    }

    public CSView<V> text(CharSequence string) {
        asTextView().setText(string);
        return this;
    }

    public float toDp(float pixel) {
        return pixel / (getDisplayMetrics().densityDpi / 160f);
    }

    protected DisplayMetrics getDisplayMetrics() {
        return getView().getResources().getDisplayMetrics();
    }

    public int toPixelInt(float dp) {
        return (int) toPixel(dp);
    }

    public float toPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160f);
    }

    public <T extends View> CSView<T> item(int id) {
        return (CSView<T>) item(findView(id));
    }

    public CSView<View> itemOld(int id) {
        return item(id);
    }

//    public CSView<V> image(String url) {
//        if (empty(url)) return this;
//        picassoImage(url).into(asImageView());
//        return this;
//    }

//    private RequestCreator picassoImage(String url) {
//        RequestCreator creator = Picasso.with(context()).load(url).fit().centerCrop();
//        if (!isNetworkConnected()) creator.networkPolicy(OFFLINE);
//        return creator;
//    }

//    public CSView<V> image(Drawable drawable) {
//        asImageView().setImageDrawable(drawable);
//        return this;
//    }

//    public CSView<V> image(int resId) {
//        if (set(resId)) Picasso.with(context()).load(resId).into(asImageView());
//        return this;
//    }

//    public CSView<V> image(String url, int width) {
//        if (empty(url)) return this;
//        RequestCreator creator = Picasso.with(context()).load(url);
//        if (!isNetworkConnected()) creator.networkPolicy(OFFLINE);
//        creator.resize(width, 0).centerInside().into(asImageView());
//        return this;
//    }

//    public void image(File file, int width) {
//        Picasso.with(context()).load(file).resize(width, 0).centerInside().into(asImageView());
//    }

//    public CSView<V> image(File file) {
//        Picasso.with(context()).load(file).into(asImageView());
//        return this;
//    }

    public void focusable(boolean focusable) {
        getView().setFocusable(focusable);
    }

//    public CSView<V> image(File file, boolean memCache, int targetWidth) {
//        RequestCreator creator = Picasso.with(context()).load(file).resize(targetWidth, 0).centerInside();
//        if (!memCache)
//            creator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
//        creator.into(asImageView());
//        return this;
//    }

    public CSView<V> text(int resId, Object... formatArgs) {
        asTextView().setText(stringf(stringRes(resId), formatArgs));
        return this;
    }

    public void gone(boolean gone) {
        visible(!gone);
    }

    public CSView<V> visible(boolean visible) {
        if (visible) getView().setVisibility(VISIBLE);
        else getView().setVisibility(GONE);
        return this;
    }

    public boolean isChecked() {
        return as(getView(), CompoundButton.class).isChecked();
    }

    public CSView<V> setChecked(boolean checked) {
        as(getView(), CompoundButton.class).setChecked(checked);
        return this;
    }

    public CSView<V> onChecked(OnCheckedChangeListener listener) {
        as(getView(), CompoundButton.class).setOnCheckedChangeListener(listener);
        return this;
    }

    public void enabled(boolean enabled) {
        getView().setEnabled(enabled);
    }

    public boolean enabled() {
        return getView().isEnabled();
    }

    public void toggleVisibility() {
        if (isShown()) hide();
        else show();
    }

    public CSView<V> setBackgroundColor(int backgroundColorResource) {
        getView().setBackgroundResource(backgroundColorResource);
        return this;
    }

    public CSView<V> textColor(int color) {
        asTextView().setTextColor(color(color));
        return this;
    }

    public CSView<V> height(int height) {
        setSize(false, height, YES);
        return this;
    }

    protected void onDestroy() {
        super.onDestroy();
        _viewField = null;
        if (is(_view)) {
            _view.setTag(null);
            _view = null;
        }
    }

    public CSView firstChild() {
        return item(as(getView(), ViewGroup.class).getChildAt(0));
    }

    public ImageView asImageView() {
        return (ImageView) getView();
    }

    public ProgressBar asProgressBar() {
        return (ProgressBar) getView();
    }

    public SeekBar asSeekBar() {
        return (SeekBar) getView();
    }

    public RadioGroup asRadioGroup() {
        return (RadioGroup) getView();
    }

    public ScrollView asScroll() {
        return (ScrollView) getView();
    }

    public CSView<V> add(CSView view, LayoutParams layoutParams) {
        asGroup().addView(view.getView(), layoutParams);
        return this;
    }

    public CSView<V> add(View view, LayoutParams layoutParams) {
        asGroup().addView(view, layoutParams);
        return this;
    }

    public CSView<V> add(CSView view) {
        if (no(view)) return this;
        if (view.hasParent()) view.parentView().removeView(view);
        asGroup().addView(view.getView());
        return this;
    }

    public CSView<V> removeView(CSView view) {
        asGroup().removeView(view.getView());
        return this;
    }

    public CSView<V> add(View view) {
        asGroup().addView(view);
        return this;
    }

    public <V extends View> CSView<V> lastSubview() {
        return item((V) asGroup().getChildAt(subviewsCount() - 1));
    }

    public int subviewsCount() {
        return asGroup().getChildCount();
    }

    public CSView<V> clearSubviews() {
        asGroup().removeAllViews();
        return this;
    }

    public boolean hasSubviews() {
        return subviewsCount() > 0;
    }

    public CSView<V> removeLastView() {
        asGroup().removeViewAt(subviewsCount() - 1);
        return this;
    }

    public void layoutParams(LinearLayout.LayoutParams params) {
        getView().setLayoutParams(params);
    }

}
