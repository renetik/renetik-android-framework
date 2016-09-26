package cs.android.viewbase;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.view.MenuInflater;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import cs.android.HasActivity;
import cs.android.view.CSAlertDialog;
import cs.android.view.adapter.OnClick;
import cs.java.callback.Run;
import cs.java.callback.RunWith;
import cs.java.callback.RunWithWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;
import cs.java.event.CSTask;
import cs.java.lang.Value;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.os.Build.VERSION.SDK_INT;
import static android.support.v4.content.ContextCompat.checkSelfPermission;
import static cs.java.lang.CSMath.randomInt;
import static cs.java.lang.Lang.*;

public abstract class CSViewController extends CSView<View> implements HasActivity {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static boolean _startingActivity;
    private static CSViewController _root;
    public final CSEvent<Void> onPause = event();
    public final CSEvent<Bundle> onCreate = event();
    public final CSEvent<Bundle> onBeforeCreate = event();
    public final CSEvent<Value<Boolean>> onBack = event();
    public final CSEvent<Void> onStart = event();
    public final CSEvent<Void> onStop = event();
    public final CSEvent<Void> onLowMemory = event();
    public final CSEvent<Bundle> onSaveInstance = event();
    public final CSEvent<Void> onDestroy = event();
    public final CSEvent<Void> onResume = event();
    public final CSEvent<Void> onUserLeaveHint = event();
    public final CSEvent<OnMenu> onPrepareOptionsMenu = event();
    public final CSEvent<Configuration> onConfigurationChanged = event();
    public final CSEvent<OnMenuItem> onOptionsItemSelected = event();
    public final CSEvent<OnMenu> onCreateOptionsMenu = event();
    public final CSEvent<ActivityResult> onActivityResult = event();
    public final CSEvent<OnKeyDownResult> onKeyDown = event();
    public final CSEvent<Intent> onNewIntent = event();
    public final CSEvent<CSViewController> onInViewControllerShow = event();
    public final CSEvent<CSViewController> onInViewControllerHide = event();
    public final CSEvent<RequestPermissionResult> onRequestPermissionsResult = event();
    private final CSEvent<Void> onPauseNative = event();
    private final CSEvent<Void> onResumeNative = event();
    private final InViewController _inView;
    private CSViewController _parent;
    private Activity _activity;
    private Bundle state;
    private CSTask<?> _parentEventsTask;
    private boolean _isCreated;
    private boolean _isResumed;
    private boolean _isPaused;
    private boolean _isDestroyed;
    private int _viewId;
    private LayoutId _layoutId;
    private boolean _isStarted;
    private InViewController _parentInView;
    private CSList<CSMenuItem> _menuItems = list();

    public CSViewController(InViewController parentInView, LayoutId layoutId) {
        super(parentInView);
        _inView = createInView();
        _parent = parentInView.parent();
        _parentInView = parentInView;
        _layoutId = layoutId;
    }

    public CSViewController(Activity activity, LayoutId layoutId) {
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

    public CSViewController(CSViewController parent, LayoutId id) {
        super(parent);
        _inView = null;
        _parent = parent;
        _layoutId = id;
        initializeFromParent(parent);
    }

    public static boolean isStartingActivity() {
        return _startingActivity;
    }

    public static CSViewController root() {
        return _root;
    }

    public InViewController inView() {
        return no(_inView) && is(parent()) ? parent().inView() : _inView;
    }

    protected void requestPermissions(List<String> permissions, final Run onGranted) {
        if (SDK_INT < 23) {
            run(onGranted);
            return;
        }
        String[] deniedPermissions = getDeniedPermissions(permissions);
        if (set(deniedPermissions)) {
            final int MY_PERMISSIONS_REQUEST = randomInt(0, 999);
            ActivityCompat.requestPermissions(activity(), deniedPermissions, MY_PERMISSIONS_REQUEST);
            onRequestPermissionsResult.add(new CSListener<RequestPermissionResult>() {
                public void onEvent(EventRegistration registration, RequestPermissionResult arg) {
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

    private String[] getDeniedPermissions(List<String> permissions) {
        List<String> deniedPermissions = list();
        for (String permission : permissions)
            if (checkSelfPermission(activity(), permission) != PERMISSION_GRANTED)
                deniedPermissions.add(permission);
        return toStringArray(deniedPermissions);
    }

    public void startActivityForUri(Uri uri, RunWith<ActivityNotFoundException> onActivityNotFound) {
        startActivityForUriAndType(uri, null, onActivityNotFound);
    }

    public void startActivityForUriAndType(Uri uri, String type, RunWith<ActivityNotFoundException> onActivityNotFound) {
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
                    onBackPressed((Value<Boolean>) argument);
                else if (registration.event() == parent.onActivityResult)
                    onActivityResult((ActivityResult) argument);
                else if (registration.event() == parent.onCreateOptionsMenu)
                    onCreateOptionsMenu((OnMenu) argument);
                else if (registration.event() == parent.onOptionsItemSelected)
                    onOptionsItemSelectedImpl((OnMenuItem) argument);
                else if (registration.event() == parent.onPrepareOptionsMenu)
                    onPrepareOptionsMenuImpl((OnMenu) argument);
                else if (registration.event() == parent.onKeyDown)
                    onKeyDown((OnKeyDownResult) argument);
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
                    onRequestPermissionsResult((RequestPermissionResult) argument);
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

    protected void onBackPressed(Value<Boolean> goBack) {
        fire(onBack, goBack);
        if (goBack.get()) goBack.set(onGoBack());
    }

    protected void onActivityResult(ActivityResult result) {
        fire(onActivityResult, result);
    }

    protected void onCreateOptionsMenu(OnMenu menu) {
        fire(onCreateOptionsMenu, menu);
    }

    void onOptionsItemSelectedImpl(OnMenuItem onItem) {
        if (!onItem.consumed()) {
            fire(onOptionsItemSelected, onItem);
            if (!onItem.consumed()) onOptionsItemSelected(onItem);
        }
    }

    protected void onOptionsItemSelected(OnMenuItem onItem) {
        if (isMenuVisible()) {
            for (CSMenuItem item : _menuItems) if (onItem.consume(item.id())) item.run();
            invalidateOptionsMenu();
        }
    }

    void onPrepareOptionsMenuImpl(OnMenu menu) {
        fire(onPrepareOptionsMenu, menu);
        onPrepareOptionsMenu(menu);
    }

    protected void onPrepareOptionsMenu(OnMenu menu) {
        if (isMenuVisible())
            for (CSMenuItem item : _menuItems) if (item.isVisible()) menu.show(item.id());
    }

    protected boolean isMenuVisible() {
        return isActive();
    }

    protected void onKeyDown(OnKeyDownResult onKey) {
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
        if (is(getView())) return getView();
        else if (set(_viewId)) {
            setView(parent().asView().findViewById(_viewId));
            if (no(getView())) throw unexpected("Expected", this, "in parent", parent());
        } else if (set(_layoutId)) setView(inflateLayout(_layoutId.layoutId));
        else setView(parent().asView());
        return getView();
    }

    public CSViewController parent() {
        return _parent;
    }

    public void hide() {
        if (is(_parentInView)) _parentInView.hide();
        else super.hide();
    }

    public CSView<View> show() {
        if (is(_parentInView)) _parentInView.showController(this);
        else super.show();
        return null;
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
    protected CSTask listen(CSEvent<?>... events) {
        return new CSTask(this, events);
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
                        new RunWithWith<String, CSAlertDialog>() {
                            public void run(String value, CSAlertDialog dialog) {
                                activity().finish();
                            }
                        });
        }
    }

    protected InViewController createInView() {
        return null;
    }

    protected void goHome() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
    }

    protected void hideOnViewClick() {
        new OnClick(this) {
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
        if (no(activity))
            warn("Activity is null impossible !!!");
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
        doLater(new Run() {
            public void run() {
                Intent intent = activity().getIntent();
                activity().finish();
                startActivity(intent);
            }
        });
    }

    public void startMapsNavigation(double latitude, double longitude, String title) {
        String uri = format("http://maps.google.com/maps?&daddr=%f,%f (%s)", latitude, longitude, title);
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

    public void onRequestPermissionsResult(RequestPermissionResult requestPermissionResult) {
        fire(onRequestPermissionsResult, requestPermissionResult);
    }

    public static class CSMenuItem {
        private CSViewController _controller;
        private int _id;
        private Run _run;
        private boolean _visible = YES;

        public CSMenuItem(CSViewController controller, int id) {
            _controller = controller;
            _id = id;
        }

        public CSMenuItem onClick(Run run) {
            _run = run;
            return this;
        }

        public int id() {
            return _id;
        }

        public void run() {
            if (is(_run)) _run.run();
        }

        public CSMenuItem hide() {
            _visible = NO;
            if (_controller.isCreated()) _controller.invalidateOptionsMenu();
            return this;
        }

        public boolean isVisible() {
            return _visible;
        }

        public void show() {
            _visible = YES;
            if (_controller.isCreated()) _controller.invalidateOptionsMenu();
        }
    }

}
