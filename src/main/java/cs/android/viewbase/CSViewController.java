package cs.android.viewbase;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import cs.android.CSActivityInterface;
import cs.android.view.CSAlertDialog;
import cs.android.view.adapter.CSClick;
import cs.java.callback.CSRun;
import cs.java.callback.CSRunWith;
import cs.java.callback.CSRunWithWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;
import cs.java.event.CSTask;
import cs.java.lang.CSValue;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.error;
import static cs.java.lang.CSLang.event;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.fire;
import static cs.java.lang.CSLang.info;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.run;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.stringf;
import static cs.java.lang.CSLang.unexpected;
import static cs.java.lang.CSLang.warn;
import static cs.java.lang.CSMath.randomInt;

public abstract class CSViewController extends CSView<View> implements CSActivityInterface {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static boolean _startingActivity;
    private static CSViewController _root;
    public final CSEvent<Void> onPause = event();
    public final CSEvent<Bundle> onCreate = event();
    public final CSEvent<Bundle> onBeforeCreate = event();
    public final CSEvent<CSValue<Boolean>> onBack = event();
    public final CSEvent<Void> onStart = event();
    public final CSEvent<Void> onStop = event();
    public final CSEvent<Void> onLowMemory = event();
    public final CSEvent<Bundle> onSaveInstance = event();
    public final CSEvent<Void> onDestroy = event();
    public final CSEvent<Void> onResume = event();
    public final CSEvent<Void> onUserLeaveHint = event();
    public final CSEvent<CSOnMenu> onPrepareOptionsMenu = event();
    public final CSEvent<Configuration> onConfigurationChanged = event();
    public final CSEvent<CSOnMenuItem> onOptionsItemSelected = event();
    public final CSEvent<CSOnMenu> onCreateOptionsMenu = event();
    public final CSEvent<CSActivityResult> onActivityResult = event();
    public final CSEvent<CSOnKeyDownResult> onKeyDown = event();
    public final CSEvent<Intent> onNewIntent = event();
    public final CSEvent<CSViewController> onInViewControllerShow = event();
    public final CSEvent<CSViewController> onInViewControllerHide = event();
    public final CSEvent<CSRequestPermissionResult> onRequestPermissionsResult = event();
    public final CSEvent<Void> onPauseNative = event();
    public final CSEvent<Void> onResumeNative = event();
    private final CSInViewController _inView;
    private boolean _isCreated;
    private boolean _isResumed;
    private boolean _isPaused;
    private boolean _isDestroyed;
    private boolean _isStarted;
    private CSViewController _parent;
    private Activity _activity;
    private Bundle state;
    private CSTask<?> _parentEventsTask;
    private int _viewId;
    private CSLayoutId _layoutId;
    private CSInViewController _parentInView;
    private CSList<CSMenuItem> _menuItems = list();

    public CSViewController(CSInViewController parentInView, CSLayoutId layoutId) {
        super(parentInView);
        _inView = createInView();
        _parent = parentInView.parent();
        _parentInView = parentInView;
        _layoutId = layoutId;
    }

    public CSViewController(Activity activity, CSLayoutId layoutId) {
        super(activity);
        _inView = createInView();
        _startingActivity = NO;
        _layoutId = layoutId;
        _parent = null;
    }

    public CSViewController(CSViewController parent) {
        super(parent);
        _inView = null;
        _parent = parent;
        initializeFromParent(parent);
    }

    public CSViewController(CSViewController parent, int viewId) {
        super(parent);
        _inView = null;
        _parent = parent;
        _viewId = viewId;
        initializeFromParent(parent);
    }

    public CSViewController(CSViewController parent, CSLayoutId id) {
        super(parent);
        _inView = null;
        _parent = parent;
        _layoutId = id;
        initializeFromParent(parent);
    }

    public static boolean isStartingActivity() {
        return _startingActivity;
    }

    public static CSViewController rootController() {
        return _root;
    }

    public static Activity rootActivity() {
        return is(_root) ? _root.activity() : null;
    }

    public CSInViewController inView() {
        return no(_inView) && is(parent()) ? parent().inView() : _inView;
    }

    public void requestPermissions(List<String> permissions, final CSRun onGranted) {
        if (SDK_INT < 23) {
            run(onGranted);
            return;
        }
        String[] deniedPermissions = getDeniedPermissions(permissions);
        if (set(deniedPermissions)) {
            final int MY_PERMISSIONS_REQUEST = randomInt(0, 999);
            ActivityCompat.requestPermissions(activity(), deniedPermissions, MY_PERMISSIONS_REQUEST);
            onRequestPermissionsResult.add(new CSListener<CSRequestPermissionResult>() {
                public void onEvent(EventRegistration registration, CSRequestPermissionResult arg) {
                    if (arg.requestCode == MY_PERMISSIONS_REQUEST) {
                        registration.cancel();
                        for (int result : arg.grantResults)
                            if (PERMISSION_GRANTED != result) return;
                        run(onGranted);
                    }
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

    private void initializeFromParent(final CSViewController parent) {
        if (is(_parentEventsTask)) warn("_parentEventsTask should not exist at this point");
        setActivity(parent.activity());
        _parentEventsTask = new CSTask(parent.onBeforeCreate, parent.onCreate, parent.onStart,
                parent.onResumeNative, parent.onPauseNative, parent.onStop, parent.onSaveInstance,
                parent.onDestroy, parent.onBack, parent.onActivityResult,
                parent.onCreateOptionsMenu, parent.onOptionsItemSelected,
                parent.onPrepareOptionsMenu, parent.onKeyDown, parent.onNewIntent,
                parent.onUserLeaveHint, parent.onLowMemory, parent.onInViewControllerShow,
                parent.onInViewControllerHide, parent.onConfigurationChanged,
                parent.onRequestPermissionsResult).add(new CSListener() {
            public void onEvent(EventRegistration registration, Object argument) {
                if (isDestroyed()) return;
                if (registration.event() == parent.onBeforeCreate)
                    onBeforeCreate((Bundle) argument);
                else if (registration.event() == parent.onCreate)
                    onCreate((Bundle) argument);
                else if (registration.event() == parent.onStart)
                    onStart();
                else if (registration.event() == parent.onResumeNative)
                    onResumeNative();
                else if (registration.event() == parent.onPauseNative)
                    onPauseNative();
                else if (registration.event() == parent.onStop)
                    onStop();
                else if (registration.event() == parent.onLowMemory)
                    onLowMemory();
                else if (registration.event() == parent.onSaveInstance)
                    onSaveInstanceState((Bundle) argument);
                else if (registration.event() == parent.onDestroy)
                    onDestroy();
                else if (registration.event() == parent.onBack)
                    onBackPressed((CSValue<Boolean>) argument);
                else if (registration.event() == parent.onActivityResult)
                    onActivityResult((CSActivityResult) argument);
                else if (registration.event() == parent.onCreateOptionsMenu)
                    onCreateOptionsMenu((CSOnMenu) argument);
                else if (registration.event() == parent.onOptionsItemSelected)
                    onOptionsItemSelectedImpl((CSOnMenuItem) argument);
                else if (registration.event() == parent.onPrepareOptionsMenu)
                    onPrepareOptionsMenuImpl((CSOnMenu) argument);
                else if (registration.event() == parent.onKeyDown)
                    onKeyDown((CSOnKeyDownResult) argument);
                else if (registration.event() == parent.onNewIntent)
                    onNewIntent((Intent) argument);
                else if (registration.event() == parent.onUserLeaveHint)
                    onUserLeaveHint();
                else if (registration.event() == parent.onInViewControllerShow)
                    onInViewControllerShow((CSViewController) argument);
                else if (registration.event() == parent.onInViewControllerHide)
                    onInViewControllerHide((CSViewController) argument);
                else if (registration.event() == parent.onConfigurationChanged)
                    onConfigurationChanged((Configuration) argument);
                else if (registration.event() == parent.onRequestPermissionsResult)
                    onRequestPermissionsResult((CSRequestPermissionResult) argument);
                else throw unexpected();
            }
        });
    }

    protected void onLowMemory() {
        fire(onLowMemory);
    }

    public boolean isRoot() {
        return _root == this;
    }

    protected void onBackPressed(CSValue<Boolean> goBack) {
        fire(onBack, goBack);
        if (goBack.get()) goBack.set(onGoBack());
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
                if (onItem.consume(item.id()))
                    if (onItem.isCheckable())
                        item.onChecked(onItem);
                    else
                        item.run();
            invalidateOptionsMenu();
        }
    }

    void onPrepareOptionsMenuImpl(CSOnMenu menu) {
        fire(onPrepareOptionsMenu, menu);
        onPrepareOptionsMenu(menu);
    }

    protected void onPrepareOptionsMenu(CSOnMenu menu) {
        if (isMenuVisible())
            for (CSMenuItem item : _menuItems)
                if (item.isVisible()) {
                    MenuItem androidItem = menu.show(item.id());
                    if (androidItem.isCheckable())
                        androidItem.setChecked(item.isChecked());
                }

    }

    protected boolean isMenuVisible() {
        return isActive();
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

    public View asView() {
        if (isDestroyed()) return null;
        if (is(getView())) return getView();
        else if (set(_viewId)) {
            setView(parent().asView().findViewById(_viewId));
            if (no(getView())) throw unexpected("Expected", this, "in parent", parent());
        } else if (set(_layoutId)) setView(inflateLayout(_layoutId.id));
        else setView(parent().asView());
        return getView();
    }

    public CSViewController parent() {
        return _parent;
    }

    public CSView<View> hide() {
        if (is(_parentInView)) _parentInView.hide();
        else super.hide();
        return this;
    }

    public CSView<View> show() {
        if (is(_parentInView)) _parentInView.showController(this);
        else super.show();
        return this;
    }

    public Context context() {
        if (is(parent())) return parent().context();
        return super.context();
    }

    public MenuInflater getMenuInflater() {
        return ((CSActivity) activity()).getSupportMenuInflater();
    }

    public Activity activity() {
        return _activity;
    }

    public Bundle getState() {
        return state;
    }

    public ActionBar getActionBar() {
        return ((CSActivity) activity()).getSupportActionBar();
    }

    public int getActionBarHeight() {
        if (is(getActionBar())) return getActionBar().getHeight();
        return 0;
    }

    public void goBack() {
        if (is(parent()))
            parent().goBack();
        else activity().onBackPressed();
    }

    public boolean hasLayout() {
        return is(_layoutId);
    }

    /**
     * Have to call from onResume
     */
    protected CSTask<Object> listenAll(CSEvent... events) {
        return new CSTask(this, events);
    }

    protected <Argument> CSTask<Argument> listen(CSEvent<Argument> event) {
        return new CSTask<Argument>(this, event);
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

    public void startActivity(Class<? extends Activity> activityClass) {
        startActivity(new Intent(activity(), activityClass));
    }

    public void startActivity(Intent intent) {
        _startingActivity = YES;
        activity().startActivity(intent);
    }

    public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
        startActivityForResult(new Intent(activity(), activityClass), requestCode);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        _startingActivity = YES;
        activity().startActivityForResult(intent, requestCode);
    }

    public void switchActivity(Class<? extends Activity> activityClass) {
        switchActivity(new Intent(activity(), activityClass));
    }

    public void switchActivity(Intent intent) {
        activity().finish();
        startActivity(intent);
    }

    public void switchActivity(Class<? extends Activity> activityClass, int resultCode) {
        activity().setResult(resultCode);
        switchActivity(new Intent(activity(), activityClass));
    }

    protected void checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity(), result, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else
                new CSAlertDialog(this).show("Google Play Services missing application cannot continue", null, "OK",
                        new CSRunWithWith<String, CSAlertDialog>() {
                            public void run(String value, CSAlertDialog dialog) {
                                activity().finish();
                            }
                        });
        }
    }

    protected CSInViewController createInView() {
        return null;
    }

    protected void goHome() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    protected void hideOnViewClick() {
        new CSClick(this) {
            public void onClick(View v) {
                hide();
            }
        };
    }

    public void onDeinitialize(Bundle state) {
        onPauseNative();
        onSaveInstanceState(state);
        onStop();
    }

    protected void onSaveInstanceState(Bundle state) {
        fire(onSaveInstance, state);
    }

    protected void onHideByInViewController() {
    }

    public void onInitialize() {
        initializeFromParent(set(_parentInView) ? _parentInView : _parent);
        onBeforeCreate(state);
        onCreate(state);
        onStart();
        onResumeNative();
    }

    protected void onBeforeCreate(Bundle state) {
        if (is(parent())) {
            if (no(parent().activity())) throw unexpected();
            setActivity(parent().activity());
        } else info(this);
        fire(onBeforeCreate, state);
    }

    protected void setActivity(Activity activity) {
        _activity = activity;
        setContext(activity);
    }

    private void updateRoot() {
        if (no(_parent)) _root = this;
    }

    protected void onCreate(Bundle state) {
        this.state = state;
        updateRoot();
        fire(onCreate, state);
        if (no(state)) onCreateFirstTime();
        else onCreateRestore();
        onCreate();
        _isCreated = YES;
        _isResumed = NO;
        _isStarted = NO;
        _isPaused = NO;
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

    public void onResumeNative() {
        fire(onResumeNative);
        if (isActive()) onResume();
    }

    public void onResume() {
        if (_isResumed)
            warn("Called onResume when resumed , this should not happen !");
        updateRoot();
        _isResumed = true;
        _isPaused = false;
        invalidateOptionsMenu();
        fire(onResume);
    }

    public void onPauseNative() {
        fire(onPauseNative);
        if (isActive()) onPause();
    }

    public void onPause() {
        if (_isPaused)
            warn("Called onPause when paused , this should not happen !");
        _isResumed = false;
        _isPaused = true;
        fire(onPause);
    }

    protected void onStop() {
        _isStarted = false;
        state = null;
        fire(onStop);
    }

    protected void onInViewControllerHide(CSViewController controller) {
        hideKeyboard();
        invalidateOptionsMenu();
        fire(onInViewControllerHide, controller);
        onResume();
    }

    protected void onInViewControllerShow(CSViewController controller) {
        invalidateOptionsMenu();
        fire(onInViewControllerShow, controller);
        onPause();
    }

    public boolean isActive() {
        return inView() == null || inView().isHidden();
    }

    public void onDestroy() {
        super.onDestroy();
        if (isDestroyed())
            throw exception();
        if (is(_parentEventsTask)) {
            _parentEventsTask.cancel();
            _parentEventsTask = null;
        }
        setView(null);
        _parentInView = null;
        _parent = null;
        if (isRoot()) _root = null;
        _activity = null;
        _isDestroyed = true;
        fire(onDestroy);
        System.gc();
    }

    public void invalidateOptionsMenu() {
        if (activity() == null) {
            error(new Throwable(), "activity is null");
            return;
        }
        ((CSActivity) activity()).supportInvalidateOptionsMenu();
    }

    protected CSMenuItem menu(int id) {
        return _menuItems.put(new CSMenuItem(this, id));
    }

    protected void restartActivity() {
        doLater(new CSRun() {
            public void run() {
                Intent intent = activity().getIntent();
                activity().finish();
                startActivity(intent);
            }
        });
    }

    public void startMapsNavigation(double latitude, double longitude, String title) {
        String uri = stringf("http://maps.google.com/maps?&daddr=%f,%f (%s)", latitude, longitude, title);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException e) {
                error(e);
            }
        }
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
}
