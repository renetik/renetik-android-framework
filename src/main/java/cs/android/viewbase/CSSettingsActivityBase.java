package cs.android.viewbase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cs.java.lang.CSValue;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.no;

public abstract class CSSettingsActivityBase extends PreferenceActivity implements CSActivity {

    private CSActivityManager _manager;
    private CSViewController _controller;

    public Activity activity() {
        return this;
    }

    private CSActivityManager activityManager() {
        if (no(_manager)) _manager = createActivityManager();
        return _manager;
    }

    public Context context() {
        return this;
    }

    public CSViewController controller() {
        return _controller;
    }

    protected CSActivityManager createActivityManager() {
        return new CSActivityManager(this);
    }

    public ActionBar getSupportActionBar() {
        return null;
    }

    public FragmentManager getSupportFragmentManager() {
        return null;
    }

    public MenuInflater getSupportMenuInflater() {
        return getMenuInflater();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _controller.onActivityResult(new CSActivityResult(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onBackPressed() {
        CSValue<Boolean> willPressBack = new CSValue<Boolean>(true);
        _controller.onBackPressed(willPressBack);
        if (willPressBack.get()) super.onBackPressed();
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        _controller = activityManager().create();
        activityManager().onCreate(state);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        CSOnMenu onMenu = new CSOnMenu(activity(), menu);
        _controller.onCreateOptionsMenu(onMenu);
        return onMenu.showMenu();
    }

    protected void onDestroy() {
        super.onDestroy();
        activityManager().onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        CSOnKeyDownResult onKeyDown = new CSOnKeyDownResult(keyCode, event);
        _controller.onKeyDown(onKeyDown);
        return super.onKeyDown(keyCode, event);
    }

    public void onLowMemory() {
        _controller.onLowMemory();
        super.onLowMemory();
    }

    protected void onNewIntent(Intent intent) {
        _controller.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        CSOnMenuItem onMenuItem = new CSOnMenuItem(item);
        _controller.onOptionsItemSelected(onMenuItem);
        return onMenuItem.consumed();
    }

    protected void onPause() {
        _controller.onPause();
        super.onPause();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        CSOnMenu onMenu = new CSOnMenu(activity(), menu);
        onMenu.showMenu(YES);
        _controller.onPrepareOptionsMenu(onMenu);
        return onMenu.showMenu();
    }

    protected void onResume() {
        _controller.onResume();
        super.onResume();
    }

    public Object onRetainNonConfigurationInstance() {
        return _controller;
    }

    public void onSaveInstanceState(Bundle state) {
        _controller.onSaveInstanceState(state);
    }

    protected void onStart() {
        _controller.onStart();
        super.onStart();
    }

    protected void onStop() {
        _controller.onStop();
        super.onStop();
    }

    protected void onUserLeaveHint() {
        _controller.onUserLeaveHint();
        super.onUserLeaveHint();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void supportInvalidateOptionsMenu() {
        invalidateOptionsMenu();
    }

}
