package renetik.android.viewbase;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.List;
import java.util.Map.Entry;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import kotlin.Unit;
import renetik.android.java.callback.CSRun;
import renetik.android.java.callback.CSRunWith;
import renetik.android.java.collections.CSList;
import renetik.android.java.collections.CSMap;
import renetik.android.java.event.CSEvent;
import renetik.android.java.event.CSEvent.CSEventRegistration;
import renetik.android.java.event.CSEventRegistrations;
import renetik.android.java.lang.CSValue;
import renetik.android.view.CSNavigationController;
import renetik.android.viewbase.menu.CSMenuItem;
import renetik.android.viewbase.menu.CSOnMenu;
import renetik.android.viewbase.menu.CSOnMenuItem;

import static kotlin.Unit.INSTANCE;
import static renetik.android.java.collections.CSListKt.list;
import static renetik.android.java.event.CSEventKt.fire;
import static renetik.android.lang.CSLang.NO;
import static renetik.android.lang.CSLang.YES;
import static renetik.android.lang.CSLang.doLater;
import static renetik.android.lang.CSLang.equal;
import static renetik.android.lang.CSLang.event;
import static renetik.android.lang.CSLang.exception;
import static renetik.android.lang.CSLang.is;
import static renetik.android.lang.CSLang.no;
import static renetik.android.lang.CSLang.set;
import static renetik.android.lang.CSLang.warn;

public abstract class CSViewController<ViewType extends View> extends CSView<ViewType> {

    private static boolean _startingActivity;
    private static CSViewController _root;
    public final CSEvent<Bundle> onBeforeCreate = event();
    public final CSEvent<Bundle> onCreate = event();
    public final CSEvent<Bundle> onSaveInstanceState = event();
    public final CSEvent<Unit> onStart = event();
    public final CSEvent<Unit> onResume = event();
    public final CSEvent<Unit> onPause = event();
    public final CSEvent<Unit> onStop = event();
    public final CSEvent<Unit> onDestroy = event();
    public final CSEvent<CSValue<Boolean>> onBack = event();
    public final CSEvent<Configuration> onConfigurationChanged = event();
    public final CSEvent<Unit> onLowMemory = event();
    public final CSEvent<Unit> onUserLeaveHint = event();
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
    private CSList<CSMenuItem> _menuItems = list();
    private boolean _isResumeFirstTime = NO;
    private Boolean _showingInContainer;
    private boolean _isBeforeCreate;
    private boolean _isShowing = NO;
    private boolean _onViewShowingCalled;

    public CSViewController(AppCompatActivity activity) {
        this(activity, null);
    }

    public CSViewController(AppCompatActivity activity, CSLayoutId layoutId) {
        super(activity, layoutId);
        _activity = activity;
        _startingActivity = NO;
        _parent = null;
    }

    public CSViewController(CSViewController<?> parent) {
        super(parent);
        _parent = parent;
        _activity = _parent.activity();
        parent.onBeforeCreate.run((registration, argument) -> {
            onBeforeCreate(argument);
            return INSTANCE;
        });
    }

    public CSViewController(CSViewController<?> parent, int viewId) {
        super(parent);
        _parent = parent;
        _activity = _parent.activity();
        _viewId = viewId;
        parent.onBeforeCreate.run((registration, argument) -> {
            onBeforeCreate(argument);
            return INSTANCE;
        });
    }

    public CSViewController(CSViewController<?> parent, CSLayoutId layoutId) {
        super(parent, layoutId);
        _parent = parent;
        _activity = _parent.activity();
        parent.onBeforeCreate.run((registration, argument) -> {
            onBeforeCreate(argument);
            return INSTANCE;
        });
    }

    public CSViewController(CSNavigationController parent, CSLayoutId layoutId) {
        super(parent.getView(), layoutId);
        _parent = parent;
        _activity = _parent.activity();
        parent.onBeforeCreate.run((registration, argument) -> {
            onBeforeCreate(argument);
            return INSTANCE;
        });
    }


    public static boolean isStartingActivity() {
        return _startingActivity;
    }

    public static CSViewController rootController() {
        return _root;
    }

    @Nullable
    public static AppCompatActivity rootActivity() {
        return is(_root) ? _root.activity() : null;
    }

    protected void onBeforeCreate(Bundle state) {
        if (_isBeforeCreate) throw exception("Already before created");
        updateRoot();
        if (is(parent())) initializeListeners();
        else if (!isRoot()) throw exception("No parent, No root");
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
        if (_isResumed) warn("already resumed", this);
        updateRoot();
        _isResumed = true;
        _isPaused = false;
        if (!_isResumeFirstTime) onResumeFirstTime();
        else onResumeRestore();
        _isResumeFirstTime = YES;
        fire(onResume);
        updateVisibilityChanged();
    }

    protected void onResumeFirstTime() {
    }

    protected void onResumeRestore() {
    }

    public void onPause() {
        if (!_isResumed)
            warn(new Throwable(), "Not Resumed while paused, should be resumed first", this);
        _isResumed = NO;
        _isPaused = YES;
        updateVisibilityChanged();
        fire(onPause);
    }

    protected void onStop() {
        if (!_isPaused)
            warn(new Throwable(), "Not paused while stopped, should be paused first", this);
        _isStarted = NO;
        _state = null;
        updateVisibilityChanged();
        fire(onStop);
    }

    public void onDestroy() {
        super.onDestroy();
        if (_isStarted) warn(new Throwable(), "Started while destroyed, should be stopped first");
        if (_isDestroyed)
            throw exception("Already destroyed");
        if (is(_parentEventsTask)) _parentEventsTask.cancel();
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

    public ContextThemeWrapper context(int theme) {
        return new ContextThemeWrapper(context(), theme);
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
        CSViewController<?> parent = _parent;
        return _parentEventsTask = new CSEventRegistrations(
                parent.onCreate.run((registration, argument) -> {
                    onCreate(argument);
                    return INSTANCE;
                }),
                parent.onStart.run((registration, argument) -> {
                    onStart();
                    return INSTANCE;
                }),
                parent.onResume.run((registration, argument) -> {
                    onResume();
                    return INSTANCE;
                }),
                parent.onPause.run((registration, argument) -> {
                    onPause();
                    return INSTANCE;
                }),
                parent.onStop.run((registration, argument) -> {
                    onStop();
                    return INSTANCE;
                }),
                parent.onDestroy.run((registration, argument) -> {
                    onDestroy();
                    return INSTANCE;
                }),
                parent.onBack.run((registration, argument) -> {
                    onBack(argument);
                    return INSTANCE;
                }),
                parent.onActivityResult.run((registration, argument) -> {
                    onActivityResult(argument);
                    return INSTANCE;
                }),
                parent.onCreateOptionsMenu.run((registration, argument) -> {
                    onCreateOptionsMenu(argument);
                    return INSTANCE;
                }),
                parent.onOptionsItemSelected.run((registration, argument) -> {
                    onOptionsItemSelectedImpl(argument);
                    return INSTANCE;
                }),
                parent.onPrepareOptionsMenu.run((registration, argument) -> {
                    onPrepareOptionsMenuImpl(argument);
                    return INSTANCE;
                }),
                parent.onKeyDown.run((registration, argument) -> {
                    onKeyDown(argument);
                    return INSTANCE;
                }),
                parent.onNewIntent.run((registration, argument) -> {
                    onNewIntent(argument);
                    return INSTANCE;
                }),
                parent.onUserLeaveHint.run((registration, argument) -> {
                    onUserLeaveHint();
                    return INSTANCE;
                }),
                parent.onLowMemory.run((registration, argument) -> {
                    onLowMemory();
                    return INSTANCE;
                }),
                parent.onConfigurationChanged.run((registration, argument) -> {
                    onConfigurationChanged(argument);
                    return INSTANCE;
                }),
                parent.onRequestPermissionsResult.run((registration, argument) -> {
                    onRequestPermissionsResult(argument);
                    return INSTANCE;
                }),
                parent.onInViewControllerShow.run((registration, argument) -> {
                    onInViewControllerShow(argument);
                    return INSTANCE;
                }),
                parent.onInViewControllerHide.run((registration, argument) -> {
                    onInViewControllerHide(argument);
                    return INSTANCE;
                }),
                parent.onSaveInstanceState.run((registration, argument) -> {
                    onSaveInstanceState(argument);
                    return INSTANCE;
                }),
                parent.onViewVisibilityChanged.run((registration, argument) -> {
                    updateVisibilityChanged();
                    return INSTANCE;
                })
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
        if (!onItem.getConsumed().getValue()) {
            fire(onOptionsItemSelected, onItem);
            if (!onItem.getConsumed().getValue()) onOptionsItemSelected(onItem);
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

    protected void onPrepareOptionsMenu(CSOnMenu onMenu) {
        if (isMenuVisible()) onMenu.onPrepareItems(_menuItems);
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

    public AppCompatActivity activity() {
        return _activity;
    }

    public Bundle getState() {
        return _state;
    }

    public ActionBar getActionBar() {
        return activity().getSupportActionBar();
    }

    public void goBack() {
        if (is(parent())) parent().goBack();
        else activity().onBackPressed();
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


    protected void goHome() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    protected void onHideByInViewController() {
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
        if (is(parent()) && !parent().isShowing()) return NO;
        return YES;
    }

    private void onViewVisibilityChanged(boolean showing) {
        if (_isShowing = showing) {
            _isVisibleEventRegistrations.setActive(YES);
            invalidateOptionsMenu();
            onViewShowing();
        } else {
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
