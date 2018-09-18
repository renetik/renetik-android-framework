package cs.android.viewbase;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map.Entry;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import cs.android.viewbase.menu.CSMenuItem;
import cs.android.viewbase.menu.CSOnMenu;
import cs.android.viewbase.menu.CSOnMenuItem;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.collections.CSList;
import cs.java.collections.CSMap;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.CSEventRegistration;
import cs.java.event.CSEventRegistrations;
import cs.java.event.CSTask;
import cs.java.lang.CSValue;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.debug;
import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.equal;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.event;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.fire;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.run;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.warn;
import static cs.java.lang.CSMath.randomInt;

public abstract class CSViewController<ViewType extends View> extends CSView<ViewType> implements CSIViewController {


    private static boolean _startingActivity;
    private static CSViewController _root;
    public final CSEvent<Bundle> onBeforeCreate = event();
    public final CSEvent<Bundle> onCreate = event();
    public final CSEvent<Bundle> onSaveInstanceState = event();
    public final CSEvent<Void> onStart = event();
    public final CSEvent<Void> onResume = event();
    public final CSEvent<Void> onPause = event();
    public final CSEvent<Void> onStop = event();
    public final CSEvent<Void> onDestroy = event();
    public final CSEvent<CSValue<Boolean>> onBack = event();
    public final CSEvent<Configuration> onConfigurationChanged = event();
    public final CSEvent<Void> onLowMemory = event();
    public final CSEvent<Void> onUserLeaveHint = event();
    public final CSEvent<CSOnMenu> onPrepareOptionsMenu = event();
    public final CSEvent<CSOnMenuItem> onOptionsItemSelected = event();
    public final CSEvent<CSOnMenu> onCreateOptionsMenu = event();
    public final CSEvent<CSActivityResult> onActivityResult = event();
    public final CSEvent<CSOnKeyDownResult> onKeyDown = event();
    public final CSEvent<Intent> onNewIntent = event();
    public final CSEvent<CSViewController> onInViewControllerShow = event();
    public final CSEvent<CSViewController> onInViewControllerHide = event();
    public final CSEvent<CSRequestPermissionResult> onRequestPermissionsResult = event();
    public final CSEvent<Boolean> onViewVisibilityChanged = event();
    private final CSInViewController _inView;
    private CSEventRegistrations _isVisibleEventRegistrations = new CSEventRegistrations();
    private CSEventRegistrations _whileShowingEventRegistrations = new CSEventRegistrations();
    private CSEventRegistrations _eventRegistrations = new CSEventRegistrations();
    private boolean _isCreated;
    private boolean _isResumed;
    private boolean _isPaused;
    private boolean _isDestroyed;
    private boolean _isStarted;
    private CSViewController _parent;
    private AppCompatActivity _activity;
    private Bundle _state;
    private CSEventRegistrations _parentEventsTask;
    private int _viewId;
    private CSInViewController _parentInView;
    private CSList<CSMenuItem> _menuItems = list();
    private boolean _isResumeFirstTime = YES;
    private Boolean _showingInContainer;
    private boolean _isBeforeCreate;
    private boolean _isShowing = NO;
    private boolean _onViewShowingCalled;

    public CSViewController(CSInViewController parentInView, CSLayoutId layoutId) {
        super(parentInView, layoutId);
        _inView = createInView();
        _parent = parentInView.parent();
        _parentInView = parentInView;
    }

    public CSViewController(AppCompatActivity activity, CSLayoutId layoutId) {
        super(() -> activity, layoutId);
        _inView = createInView();
        _startingActivity = NO;
        _parent = null;
    }

    public CSViewController(CSViewController<ViewType> parent) {
        super(parent);
        _inView = null;
        _parent = parent;
        parent.onBeforeCreate.add((registration, argument) -> onBeforeCreate(argument));
    }

    public CSViewController(CSViewController<?> parent, int viewId) {
        super(parent);
        _inView = null;
        _parent = parent;
        _viewId = viewId;
        parent.onBeforeCreate.add((registration, argument) -> onBeforeCreate(argument));
    }

    public CSViewController(CSViewController<?> parent, CSLayoutId layoutId) {
        super(parent, layoutId);
        _inView = null;
        _parent = parent;
        parent.onBeforeCreate.add((registration, argument) -> onBeforeCreate(argument));
    }

    public CSViewController(CSViewController<?> parent, ViewGroup viewGroup, CSLayoutId layout) {
        super(viewGroup, layout);
        _inView = null;
        _parent = parent;
        parent.onBeforeCreate.add((registration, argument) -> onBeforeCreate(argument));
    }

    public static boolean isStartingActivity() {
        return _startingActivity;
    }

    public static CSViewController rootController() {
        return _root;
    }

    public static AppCompatActivity rootActivity() {
        return is(_root) ? _root.activity() : null;
    }

    protected void onBeforeCreate(Bundle state) {
        if (_isBeforeCreate)
            throw exception("Already before created");
        updateRoot();
        if (is(parent())) {
            setActivity(parent().activity());
            initializeListeners();
        } else if (!isRoot())
            throw exception("No parent, No root");
        _isBeforeCreate = YES;
        fire(onBeforeCreate, state);
    }

    protected void onCreate(Bundle state) {
        if (!_isBeforeCreate)
            throw exception("Before create not called");
        if (_isCreated)
            throw exception("Already created");
        _state = state;
        updateRoot();
        if (no(state)) onCreateFirstTime();
        else onCreateRestore();
        onCreate();
        _isCreated = YES;
        _isResumed = NO;
        _isStarted = NO;
        _isPaused = NO;
        fire(onCreate, state);
    }

    protected void onCreateFirstTime() {
    }

    protected void onCreateRestore() {
    }

    protected void onCreate() {
    }

    protected void onStart() {
        _isStarted = true;
        updateRoot();
        fire(onStart);
    }

    protected void onResume() {
        updateRoot();
        _isResumed = true;
        _isPaused = false;
        if (_isResumeFirstTime) onResumeFirstTime();
        else onResumeRestore();
        updateVisibilityChanged();
        _isResumeFirstTime = NO;
        fire(onResume);
    }

    protected void onResumeFirstTime() {
    }

    protected void onResumeRestore() {
    }

    public void onPause() {
        _isResumed = NO;
        _isPaused = YES;
        updateVisibilityChanged();
        fire(onPause);
    }

    protected void onStop() {
        _isStarted = NO;
        _state = null;
        fire(onStop);
    }

    public void onDestroy() {
        super.onDestroy();
        if (_isDestroyed)
            throw exception("Already destroyed");
        if (is(_parentEventsTask)) _parentEventsTask.cancel();
        _parentInView = null;
        _parent = null;
        if (isRoot()) _root = null;
        _activity = null;
        _isDestroyed = true;
        _whileShowingEventRegistrations.cancel();
        _isVisibleEventRegistrations.cancel();
        _eventRegistrations.cancel();
        fire(onDestroy);
        System.gc();
    }

    public void initialize(Bundle state) {
        onBeforeCreate(state);
        onCreate(state);
        onStart();
        if (_parent.isResumed()) onResume();
    }

    public void onDeinitialize(Bundle state) {
        if (isResumed() && !isPaused()) onPause();
        onStop();
    }

    public CSViewController asController() {
        return this;
    }

    public CSInViewController inView() {
        return no(_inView) && is(parent()) ? parent().inView() : _inView;
    }

    public void requestPermissions(List<String> permissions, final CSRun onGranted) {
        requestPermissions(permissions, onGranted, null);
    }

    public ContextThemeWrapper context(int theme) {
        return new ContextThemeWrapper(context(), theme);
    }

    public void requestPermissionsWithForce(List<String> permissions, final CSRun onGranted) {
        requestPermissions(permissions, onGranted, () -> requestPermissionsWithForce(permissions, onGranted));
    }

    public void requestPermissions(List<String> permissions, CSRun onGranted, CSRun onNotGranted) {
        if (SDK_INT < 23) {
            run(onGranted);
            return;
        }
        String[] deniedPermissions = getDeniedPermissions(permissions);
        if (set(deniedPermissions)) {
            final int MY_PERMISSIONS_REQUEST = randomInt(0, 999);
            ActivityCompat.requestPermissions(activity(), deniedPermissions, MY_PERMISSIONS_REQUEST);
            onRequestPermissionsResult.add((registration, arg) -> {
                if (arg.requestCode == MY_PERMISSIONS_REQUEST) {
                    registration.cancel();
                    for (int result : arg.grantResults)
                        if (PERMISSION_GRANTED != result) {
                            run(onNotGranted);
                            return;
                        }
                    run(onGranted);
                }
            });
        } else run(onGranted);
    }

    public void startActivityForUri(Uri uri, CSRunWith<ActivityNotFoundException> onActivityNotFound) {
        startActivityForUriAndType(uri, null, onActivityNotFound);
    }

    public void startActivityForUriAndType(Uri uri, String type, CSRunWith<ActivityNotFoundException> onActivityNotFound) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, type);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            warn(e);
            if (is(onActivityNotFound)) onActivityNotFound.run(e);
        }
    }

    protected CSEventRegistrations initializeListeners() {
        if (is(_parentEventsTask))
            throw exception("Already initialized with parent");
        CSViewController<?> parent = set(_parentInView) ? _parentInView : _parent;
        return _parentEventsTask = new CSEventRegistrations(
                parent.onCreate.add((registration, argument) -> onCreate(argument)),
                parent.onStart.add((registration, argument) -> onStart()),
                parent.onResume.add((registration, argument) -> onResume()),
                parent.onPause.add((registration, argument) -> onPause()),
                parent.onStop.add((registration, argument) -> onStop()),
                parent.onDestroy.add((registration, argument) -> onDestroy()),
                parent.onBack.add((registration, argument) -> onBack(argument)),
                parent.onActivityResult.add((registration, argument) -> onActivityResult(argument)),
                parent.onCreateOptionsMenu.add((registration, argument) -> onCreateOptionsMenu(argument)),
                parent.onOptionsItemSelected.add((registration, argument) -> onOptionsItemSelectedImpl(argument)),
                parent.onPrepareOptionsMenu.add((registration, argument) -> onPrepareOptionsMenuImpl(argument)),
                parent.onKeyDown.add((registration, argument) -> onKeyDown(argument)),
                parent.onNewIntent.add((registration, argument) -> onNewIntent(argument)),
                parent.onUserLeaveHint.add((registration, argument) -> onUserLeaveHint()),
                parent.onLowMemory.add((registration, argument) -> onLowMemory()),
                parent.onConfigurationChanged.add((registration, argument) -> onConfigurationChanged(argument)),
                parent.onRequestPermissionsResult.add((registration, argument) -> onRequestPermissionsResult(argument)),
                parent.onInViewControllerShow.add((registration, argument) -> onInViewControllerShow(argument)),
                parent.onInViewControllerHide.add((registration, argument) -> onInViewControllerHide(argument)),
                parent.onSaveInstanceState.add((registration, argument) -> onSaveInstanceState(argument)),
                parent.onViewVisibilityChanged.add((registration, argument) -> updateVisibilityChanged())
        );
    }

    protected void onLowMemory() {
        fire(onLowMemory);
    }

    public boolean isRoot() {
        return _root == this;
    }

    protected void onBack(CSValue<Boolean> goBack) {
        fire(onBack, goBack);
        if (goBack.getValue() && isShowing()) goBack.setValue(onGoBack());
    }

    protected void onActivityResult(CSActivityResult result) {
        fire(onActivityResult, result);
    }

    protected void onCreateOptionsMenu(CSOnMenu menu) {
        fire(onCreateOptionsMenu, menu);
    }

    void onOptionsItemSelectedImpl(CSOnMenuItem onItem) {
        if (!onItem.consumed()) {
            fire(onOptionsItemSelected, onItem);
            if (!onItem.consumed()) onOptionsItemSelected(onItem);
        }
    }

    protected void onOptionsItemSelected(CSOnMenuItem onItem) {
        if (isMenuVisible()) {
            for (CSMenuItem item : _menuItems)
                if (onItem.consume(item))
                    if (onItem.isCheckable()) item.onChecked(onItem);
                    else item.run();
            invalidateOptionsMenu();
        }
    }

    void onPrepareOptionsMenuImpl(CSOnMenu menu) {
        fire(onPrepareOptionsMenu, menu);
        onPrepareOptionsMenu(menu);
    }

    protected void onPrepareOptionsMenu(CSOnMenu menu) {
        if (isMenuVisible())
            for (CSMenuItem item : _menuItems) if (item.isVisible()) menu.show(item);
    }

    protected boolean isMenuVisible() {
        return isShowing();
    }

    protected void onKeyDown(CSOnKeyDownResult onKey) {
        fire(onKeyDown, onKey);
    }

    protected void onNewIntent(Intent intent) {
        fire(onNewIntent, intent);
    }

    protected void onUserLeaveHint() {
        fire(onUserLeaveHint);
    }

    protected Boolean onGoBack() {
        return YES;
    }

    public ViewType getView() {
        if (isDestroyed()) return null;
        if (is(super.getView())) return super.getView();
        else if (set(_viewId))
            return setView((ViewType) parent().findViewRecursive(_viewId));
        else if (is(parent())) return setView((ViewType) parent().getView());
        throw exception("This should not happen man ;)");
    }

    public CSViewController parent() {
        return _parent;
    }

    public CSView<ViewType> hide() {
        if (is(_parentInView)) _parentInView.closeController();
        else super.hide();
        return this;
    }

    public AppCompatActivity activity() {
        return _activity;
    }

    public Bundle getState() {
        return _state;
    }

    public ActionBar getActionBar() {
        return ((CSActivity) activity()).getSupportActionBar();
    }

    public void goBack() {
        if (is(parent())) parent().goBack();
        else activity().onBackPressed();
    }

    protected <Argument> CSTask<Argument> listen(CSEvent<Argument> event) {
        return new CSTask<>(this, event);
    }

    public Intent intent() {
        return activity().getIntent();
    }

    public boolean isCreated() {
        return _isCreated;
    }

    public boolean isDestroyed() {
        return _isDestroyed;
    }

    public boolean isPaused() {
        return _isPaused;
    }

    public boolean isResumed() {
        return _isResumed;
    }

    public boolean isStarted() {
        return _isStarted;
    }

    public void startActivity(Class<? extends AppCompatActivity> activityClass) {
        startActivity(new Intent(activity(), activityClass));
    }

    public void startActivity(Class<? extends AppCompatActivity> activityClass, CSMap<String, String> extras) {
        Intent intent = new Intent(activity(), activityClass);
        for (Entry<String, String> entry : extras.entrySet())
            intent.putExtra(entry.getKey(), entry.getValue());
        startActivity(intent);
    }

    public void startActivity(Intent intent) {
        _startingActivity = YES;
        activity().startActivity(intent);
    }

    public void startActivityForResult(Class<? extends AppCompatActivity> activityClass, int requestCode) {
        startActivityForResult(new Intent(activity(), activityClass), requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        _startingActivity = YES;
        activity().startActivityForResult(intent, requestCode);
    }

    public void switchActivity(Class<? extends AppCompatActivity> activityClass) {
        switchActivity(new Intent(activity(), activityClass));
    }

    public void switchActivity(Intent intent) {
        activity().finish();
        startActivity(intent);
    }

    public void switchActivity(Class<? extends AppCompatActivity> activityClass, int resultCode) {
        activity().setResult(resultCode);
        switchActivity(new Intent(activity(), activityClass));
    }

    protected CSInViewController createInView() {
        return null;
    }

    protected void goHome() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    protected void onHideByInViewController() {
    }

    protected void setActivity(AppCompatActivity activity) {
        if (no(activity)) throw new NullPointerException();
        _activity = activity;
        setContext(activity);
    }

    private void updateRoot() {
        if (no(_parent)) _root = this;
    }

    protected void onInViewControllerShow(CSViewController controller) {
        updateVisibilityChanged();
        fire(onInViewControllerShow, controller);
    }

    protected void onInViewControllerHide(CSViewController controller) {
        updateVisibilityChanged();
        fire(onInViewControllerHide, controller);
    }

    public boolean isShowing() {
        return _isShowing;
    }

    public void setShowingInContainer(boolean showingInContainer) {
        if (equal(_showingInContainer, showingInContainer)) return;
        _showingInContainer = showingInContainer;
        updateVisibilityChanged();
    }

    protected void updateVisibilityChanged() {
        if (checkIfIsShowing()) {
            if (!_isShowing) onViewVisibilityChanged(YES);
        } else if (_isShowing) onViewVisibilityChanged(NO);
    }

    protected boolean checkIfIsShowing() {
        if (!isResumed()) return NO;
        if (is(_showingInContainer) && !_showingInContainer) return NO;
        if (is(_parentInView)) {
            if (inViewVisible()) return parent().isShowing();
            return YES;
        }
        if (inViewVisible()) return NO;
        if (is(parent()) && !parent().isShowing()) return NO;
        return YES;
    }

    private boolean inViewVisible() {
        return is(_inView) && _inView.isControllerOpened();
    }

    private void onViewVisibilityChanged(boolean showing) {
        if (_isShowing = showing) {
            debug("onViewShowing", this);
            _isVisibleEventRegistrations.setActive(YES);
            invalidateOptionsMenu();
            onViewShowing();
        } else {
            debug("onViewHiding", this);
            _isVisibleEventRegistrations.setActive(NO);
            invalidateOptionsMenu();
            onViewHiding();
            _whileShowingEventRegistrations.cancel();
        }
        fire(onViewVisibilityChanged, _isShowing);
    }

    protected void onViewShowing() {
        if (!_onViewShowingCalled) {
            onViewShowingFirstTime();
            _onViewShowingCalled = YES;
        } else onViewShowingAgain();
    }

    protected void onViewShowingFirstTime() {
    }

    private void onViewShowingAgain() {
    }

    protected void onViewHiding() {
        if (!_onViewShowingCalled) {
            onViewHidingFirstTime();
            _onViewShowingCalled = YES;
        } else onViewHidingAgain();
    }

    protected void onViewHidingFirstTime() {
    }

    protected void onViewHidingAgain() {
    }

    protected CSEventRegistration ifVisible(CSEventRegistration eventRegistration) {
        return _isVisibleEventRegistrations.add(eventRegistration);
    }

    protected CSEventRegistration register(CSEventRegistration eventRegistration) {
        if (no(eventRegistration)) return null;
        return _eventRegistrations.add(eventRegistration);
    }

    protected CSEventRegistration whileShowing(CSEventRegistration eventRegistration) {
        if (no(eventRegistration)) return null;
        return _whileShowingEventRegistrations.add(eventRegistration);
    }

    public void invalidateOptionsMenu() {
        if (is(activity())) activity().supportInvalidateOptionsMenu();
        else error("invalidateOptionsMenu, activity is null");
    }

    private CSMenuItem addMenuItem(CSMenuItem item) {
        _menuItems.put(item);
        invalidateOptionsMenu();
        return item;
    }

    protected CSMenuItem menu(int id) {
        return addMenuItem(new CSMenuItem(this, id));
    }

    protected CSMenuItem menu(String title) {
        return addMenuItem(new CSMenuItem(this, title));
    }

    protected CSMenuItem menu(String title, int iconResource) {
        return addMenuItem(new CSMenuItem(this, title)).setIconResourceId(iconResource);
    }

    protected void restartActivity() {
        doLater((CSRun) () -> {
            Intent intent = activity().getIntent();
            activity().finish();
            startActivity(intent);
        });
    }

    public void onConfigurationChanged(Configuration newConfig) {
        fire(onConfigurationChanged, newConfig);
    }

    public void onRequestPermissionsResult(CSRequestPermissionResult requestPermissionResult) {
        fire(onRequestPermissionsResult, requestPermissionResult);
    }

    public void startApplication(String packageName) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = activity().getPackageManager().queryIntentActivities(intent, 0);
            for (ResolveInfo info : resolveInfoList)
                if (info.activityInfo.packageName.equalsIgnoreCase(packageName)) {
                    launchComponent(info.activityInfo.packageName, info.activityInfo.name);
                    return;
                }
            showInMarket(packageName);
        } catch (Exception e) {
            showInMarket(packageName);
        }
    }

    private void launchComponent(String packageName, String name) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setComponent(new ComponentName(packageName, name));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void showInMarket(String packageName) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onSaveInstanceState(Bundle outState) {
        onSaveInstanceState.fire(outState);
    }
}
