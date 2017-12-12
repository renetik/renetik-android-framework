package cs.android.viewbase;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import cs.android.CSContextInterface;
import cs.android.view.adapter.CSTextWatcherAdapter;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.collections.CSList;
import cs.java.common.CSPoint;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.squareup.picasso.NetworkPolicy.OFFLINE;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.as;
import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.stringf;

public class CSView<T extends View> extends CSContextController implements CSViewInterface {

    private CSView<View> _viewField;
    private View _view;

    //Needed for CSViewController constructor
    CSView() {
    }

    public CSView(CSContextInterface context, CSLayoutId layoutId) {
        this(context);
        setInflateView(layoutId.id);
    }

    public CSView(CSContextInterface context) {
        super(context);
    }

    public CSView(Context context) {
        super(context);
    }

    public CSView(final ViewGroup parent, CSLayoutId layoutId) {
        super(new CSContextInterface() {
            public Context context() {
                return parent.getContext();
            }
        });
        setView(inflateLayout(parent, layoutId.id));
    }

    public CSView(CSView<?> parent, int viewId) {
        this(parent.findView(viewId));
    }

    public CSView(final View view) {
        super(view.getContext());
        setView(view);
    }

    public CSView(CSLayoutId layoutId) {
        setInflateView(layoutId.id);
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
        return new CSView<>(view);
    }

    protected void setInflateView(int layoutId) {
        setView(inflateLayout(layoutId));
    }

    public View inflateLayout(int layout_id) {
        return LayoutInflater.from(context()).inflate(layout_id, null);
    }

    public View inflateLayout(ViewGroup parent, int layout_id) {
        return LayoutInflater.from(context()).inflate(layout_id, parent, NO);
    }

    public View findView(int id) {
        return asView().findViewById(id);
    }

    @SuppressWarnings("unchecked")
    public T asView() {
        return (T) _view;
    }

    public int getLayoutWidth() {
        return asView().getLayoutParams().width;
    }

    public AbsListView asAbsListView() {
        return (AbsListView) asView();
    }

    public GridView asGridView() {
        return (GridView) asView();
    }

    @SuppressWarnings("unchecked")
    public <A extends Adapter> AdapterView<A> asAdapterView() {
        return (AdapterView<A>) asView();
    }

    public FrameLayout asFrame() {
        return (FrameLayout) asView();
    }

    public ViewGroup asGroup() {
        return (ViewGroup) asView();
    }

    public ListView asListView() {
        return (ListView) asView();
    }

    public CSPoint center() {
        return new CSPoint(getLeft() + width() / 2, getTop() + getHeight() / 2);
    }

    public int getLeft() {
        return asView().getLeft();
    }

    public int width() {
        return asView().getWidth();
    }

    public int getTop() {
        return asView().getTop();
    }

    public int getHeight() {
        return asView().getHeight();
    }

    public CSView<T> width(int width) {
        setSize(YES, width, YES);
        return this;
    }

    private void setSize(boolean width, int value, boolean dip) {
        LayoutParams lp = asView().getLayoutParams();
        if (value > 0 && dip) value = (int) toPixel(value);
        if (width) lp.width = value;
        else lp.height = value;
        asView().setLayoutParams(lp);
    }

    public void fade(boolean fadeIn) {
        if (fadeIn)
            fadeIn();
        else fadeOut();
    }

    protected SwipeRefreshLayout getPull(int id) {
        return getView(id, SwipeRefreshLayout.class);
    }

    public AlphaAnimation fadeIn() {
        return fadeIn(asView());
    }

    public AlphaAnimation fadeOut() {
        return fadeOut(asView());
    }

    public AlphaAnimation fadeIn(final View view) {
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);

        if (isShown(view) && hasParent()) return animation;

        if (is(view.getAnimation())) {
            view.getAnimation().cancel();
            view.clearAnimation();
        }
        animation.setDuration(150);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                show(view);
            }

            public void onAnimationRepeat(Animation animation) {
            }


        });
        view.startAnimation(animation);
        return animation;
    }

    public AlphaAnimation fadeOut(final View view) {
        return fadeOut(view, null);
    }

    public boolean isShown(View view) {
        if (no(view)) return NO;
        return view.isShown();
    }

    public boolean hasParent() {
        return is(asView().getParent());
    }

    public void show(View view) {
        view.setVisibility(VISIBLE);
    }

    public AlphaAnimation fadeOut(final View view, final CSRunWith<View> onDone) {
        AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);

        if (is(view.getAnimation())) view.getAnimation().cancel();

        if (isHidden(view) && no(view.getAnimation())) {
            if (is(onDone)) onDone.run(view);
            return animation;
        }

        animation.setDuration(300);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationEnd(Animation animation) {
                hide(view);
                if (is(onDone)) doLater(new CSRun() {
                    public void run() {
                        onDone.run(view);
                    }
                });
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationStart(Animation animation) {
            }
        });
        view.startAnimation(animation);
        return animation;
    }

    private boolean isHidden(View view) {
        return !isShown(view);
    }

    public void hide(View view) {
        view.setVisibility(GONE);
    }

    public void invisible() {
        asView().setVisibility(View.INVISIBLE);
    }

    public CSView parent() {
        ViewParent parent = asView().getParent();
        return view((View) parent);
    }

    public CSView<View> view(View view) {
        return _viewField = new CSView<>(this).setView(view);
    }

    public CSView<View> view(CSLayoutId layout) {
        return view(inflateLayout(layout.id));
    }

    public CSView<View> view(CSLayoutId layout, ViewGroup parent) {
        CSView<View> view = view(service(LAYOUT_INFLATER_SERVICE, LayoutInflater.class).inflate(layout.id, parent, false));
        parent.addView(view.asView());
        return view;
    }

    public CSView<View> view() {
        if (no(_viewField)) view(asView());
        return _viewField;
    }

    public <V extends View> CSView<V> view(Class<V> clazz) {
        return (CSView<V>) _viewField;
    }

    public AlphaAnimation fadeIn(int view) {
        return fadeIn(findView(view));
    }

    public AlphaAnimation fadeOut(int view) {
        return fadeOut(findView(view));
    }

    protected Date getDate(int picker) {
        return getDate(getDatePicker(picker));
    }

    protected Date getDate(DatePicker picker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(picker.getYear(), picker.getMonth(), picker.getDayOfMonth());
        return calendar.getTime();
    }

    protected Date getTime(int picker) {
        return getTime(getTimePicker(picker));
    }

    protected Date getTime(TimePicker picker) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
        calendar.set(Calendar.MINUTE, picker.getCurrentMinute());
        return calendar.getTime();
    }

    public DatePicker getDatePicker(int id) {
        return (DatePicker) findView(id);
    }

    public EditText getEditText(int id) {
        return (EditText) findView(id);
    }

    public FrameLayout getFrame(int id) {
        return (FrameLayout) findView(id);
    }

    public LinearLayout getLinear(int id) {
        return (LinearLayout) findView(id);
    }

    public CSPoint getLocationOnScreen() {
        int[] location = new int[2];
        asView().getLocationOnScreen(location);
        return new CSPoint(location[0], location[1]);
    }

    public Spinner getSpinner(int id) {
        return (Spinner) findView(id);
    }

    protected void initSpinner(int id, CSList<String> values, String value) {
        setSpinnerData(getSpinner(id), values);
        getSpinner(id).setSelection(values.index(value), NO);
    }

    public TextView textView(int id) {
        return (TextView) findView(id);
    }

    public TimePicker getTimePicker(int id) {
        return (TimePicker) findView(id);
    }

    public int getTopRelativeTo(View view) {
        return getTopRelativeTo(asView(), view);
    }

    public View getView() {
        return _view;
    }

    public CSView<T> setView(View view) {
        if (is(view)) view.setTag(this);
        if (is(_view)) _view.setTag(null);
        _view = view;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V getView(int id, Class<V> clazz) {
        return (V) findView(id);
    }

    public ViewGroup getViewGroup(int id) {
        return (ViewGroup) findView(id);
    }

    public ViewPager asPager() {
        return (ViewPager) asView();
    }

    public WebView getWebView(int id) {
        return (WebView) findView(id);
    }

    public CSView<T> hide() {
        hide(asView());
        return this;
    }

    public void hide(boolean hide, int viewId, int... viewIds) {
        if (hide)
            hide(viewId, viewIds);
        else show(viewId, viewIds);
    }

    public void hide(int viewId, int... viewIds) {
        hide(findView(viewId));
        for (int id : viewIds)
            hide(findView(id));
    }

    public void hideKeyboard() {
        hideKeyboard(asView().getWindowToken());
    }

    public CompoundButton getCompound(int id) {
        return (CompoundButton) findView(id);
    }

    public boolean isHidden() {
        return !isVisible();
    }

    public boolean isVisible() {
        return isShown(asView());
    }

    public boolean isPortrait() {
        return context().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public boolean isLandscape() {
        return !isPortrait();
    }

    public boolean isVisible(int id) {
        return isShown(findView(id));
    }

    public Button getButton(int id) {
        return (Button) findView(id);
    }

    public CSView<T> onClick(OnClickListener onClickListener) {
        asView().setOnClickListener(onClickListener);
        return this;
    }

    public void setPercentAspectWidth(int viewId, int percent, int minimal, int maximal) {
        setPercentAspectWidth(findView(viewId), percent, minimal, maximal);
    }

    public void setPercentAspectWidth(View view, int percent, int minimal, int maximal) {
        float onePercent = getDisplayWidth() / (float) 100;
        float wantedWidth = onePercent * percent;
        if (set(minimal) && wantedWidth < minimal)
            wantedWidth = minimal;
        else if (set(maximal) && wantedWidth > maximal) wantedWidth = maximal;

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

    protected void setSpinnerData(Spinner spinner, Collection<String> strings) {
        setSpinnerData(spinner, android.R.layout.simple_spinner_item, android.R.layout.simple_spinner_dropdown_item, strings);
    }

    protected void setSpinnerData(Spinner spinner, int itemLayout, int dropDownItemLayout, Collection<String> strings) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context(), itemLayout, list(strings));
        adapter.setDropDownViewResource(dropDownItemLayout);
        spinner.setAdapter(adapter);
    }

    public void setText(int viewId, String text) {
        textView(viewId).setText(text);
    }

    public CSView<T> show() {
        show(asView());
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

    public void showSoftInput(View view, int flag) {
        service(Context.INPUT_METHOD_SERVICE, InputMethodManager.class).showSoftInput(view, flag);
    }

    public String text() {
        return asTextView().getText() + "";
    }

    public TextView asTextView() {
        return (TextView) asView();
    }

    public CSView<T> text(int string) {
        asTextView().setText(string);
        return this;
    }

    public CSView<T> text(CharSequence string) {
        asTextView().setText(string);
        return this;
    }

    public float toDp(float pixel) {
        return pixel / (getDisplayMetrics().densityDpi / 160f);
    }

    protected DisplayMetrics getDisplayMetrics() {
        return asView().getResources().getDisplayMetrics();
    }

    public int toPixelInt(float dp) {
        return (int) toPixel(dp);
    }

    public float toPixel(float dp) {
        return dp * (getDisplayMetrics().densityDpi / 160f);
    }

    public CSView<View> view(int id) {
        return view(findView(id));
    }

    public <V extends View> CSView<V> view(int id, Class<V> clazz) {
        return (CSView<V>) view(findView(id));
    }

    public CSView<T> image(String url) {
        if (empty(url)) return this;
        picassoImage(url).into(asImageView());
        return this;
    }

    private RequestCreator picassoImage(String url) {
        RequestCreator creator = Picasso.with(context()).load(url).fit().centerCrop();
        if (!isNetworkConnected()) creator.networkPolicy(OFFLINE);
        return creator;
    }

    public CSView<T> image(Drawable drawable) {
        asImageView().setImageDrawable(drawable);
        return this;
    }

    public CSView<T> image(int resId) {
        Picasso.with(context()).load(resId).into(asImageView());
        return this;
    }

    public CSView<T> image(String url, int width) {
        if (empty(url)) return this;
        RequestCreator creator = Picasso.with(context()).load(url);
        if (!isNetworkConnected()) creator.networkPolicy(OFFLINE);
        creator.resize(width, 0).centerInside().into(asImageView());
        return this;
    }

    public void image(File file, int width) {
        Picasso.with(context()).load(file).resize(width, 0).centerInside().into(asImageView());
    }

    public CSView<T> image(File file) {
        Picasso.with(context()).load(file).into(asImageView());
        return this;
    }

    public void focusable(boolean focusable) {
        asView().setFocusable(focusable);
    }

    public CSView<T> image(File file, boolean memCache, int targetWidth) {
        RequestCreator creator = Picasso.with(context()).load(file).resize(targetWidth, 0).centerInside();
        if (!memCache)
            creator.memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE);
        creator.into(asImageView());
        return this;
    }

    public CSView<T> text(int resId, Object... formatArgs) {
        asTextView().setText(stringf(getString(resId), formatArgs));
        return this;
    }

    public void gone(boolean gone) {
        visible(!gone);
    }

    public CSView<T> visible(boolean visible) {
        if (visible) asView().setVisibility(VISIBLE);
        else asView().setVisibility(GONE);
        return this;
    }

    public boolean isChecked() {
        return as(asView(), CompoundButton.class).isChecked();
    }

    public CSView<T> setChecked(boolean checked) {
        as(asView(), CompoundButton.class).setChecked(checked);
        return this;
    }

    public CSView<T> onChecked(OnCheckedChangeListener listener) {
        as(asView(), CompoundButton.class).setOnCheckedChangeListener(listener);
        return this;
    }

    public void enabled(boolean enabled) {
        asView().setEnabled(enabled);
    }

    public boolean enabled() {
        return asView().isEnabled();
    }

    public CSView<T> onTextChange(final CSRunWith<String> runWith) {
        asTextView().addTextChangedListener(new CSTextWatcherAdapter() {
            public void afterTextChanged(Editable editable) {
                runWith.run(editable.toString());
            }
        });
        return this;
    }

    public void toggleVisibility() {
        if (isVisible()) hide();
        else show();
    }

    public CSView<T> backgroundColor(int backgroundColor) {
        asView().setBackgroundResource(backgroundColor);
        return this;
    }

    public CSView<T> textColor(int color) {
        asTextView().setTextColor(color);
        return this;
    }

    public CSView<T> height(int height) {
        setSize(false, height, YES);
        return this;
    }

    protected void onDestroy() {
        super.onDestroy();
        _viewField = null;
        if (is(_view)) _view.setTag(null);
        _view = null;
    }

    public CSView<T> hideIfEmpty() {
        visible(set(text()));
        return this;
    }

    public CSView firstChild() {
        return view(as(asView(), ViewGroup.class).getChildAt(0));
    }

    public ImageView asImageView() {
        return (ImageView) asView();
    }

    public ProgressBar asProgressBar() {
        return (ProgressBar) asView();
    }

    public SeekBar asSeekBar() {
        return (SeekBar) asView();
    }

    public RadioGroup asRadioGroup() {
        return (RadioGroup) asView();
    }

    public ScrollView asScroll() {
        return (ScrollView) asView();
    }


}
