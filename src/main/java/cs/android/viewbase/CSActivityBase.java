package cs.android.viewbase;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cs.android.viewbase.menu.CSOnMenu;
import cs.android.viewbase.menu.CSOnMenuItem;
import cs.java.lang.CSValue;

import static cs.android.viewbase.menu.CSOnMenuKt.GeneratedMenuItems;
import static cs.java.lang.CSLang.no;

public abstract class CSActivityBase extends AppCompatActivity implements CSActivity {

    private CSActivityManager _manager;
    private CSViewController _controller;

    public AppCompatActivity activity() {
        return this;
    }

    public Context context() {
        return this;
    }

    public CSViewController controller() {
        return _controller;
    }

    public MenuInflater getSupportMenuInflater() {
        return getMenuInflater();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        _controller.onActivityResult(new CSActivityResult(requestCode, resultCode, data));
        super.onActivityResult(requestCode, resultCode, data);
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

    protected void onPause() {
        _controller.onPause();
        super.onPause();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        _controller.onSaveInstanceState(outState);
    }

    protected void onNewIntent(Intent intent) {
        _controller.onNewIntent(intent);
        super.onNewIntent(intent);
    }

    protected void onResume() {
        _controller.onResume();
        super.onResume();
    }

    protected void onStart() {
        _controller.onStart();
        super.onStart();
    }

    public void onCreate(Bundle state) {
        super.onCreate(state);
        _controller = activityManager().create();
        activityManager().onCreate(state);
    }

    private CSActivityManager activityManager() {
        if (no(_manager)) _manager = createActivityManager();
        return _manager;
    }

    protected CSActivityManager createActivityManager() {
        return new CSActivityManager(this);
    }

    protected void onStop() {
        _controller.onStop();
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        activityManager().onDestroy();
        _controller = null;
        System.gc();
    }

    public void onBackPressed() {
        CSValue<Boolean> goBack = new CSValue<>(true);
        _controller.onBack(goBack);
        if (goBack.getValue()) super.onBackPressed();
    }

    protected void onUserLeaveHint() {
        _controller.onUserLeaveHint();
        super.onUserLeaveHint();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        CSOnMenu onMenu = new CSOnMenu(activity(), menu);
        _controller.onCreateOptionsMenu(onMenu);
        return onMenu.getShowMenu().getValue();
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.removeGroup(GeneratedMenuItems);
        CSOnMenu onMenu = new CSOnMenu(activity(), menu);
        _controller.onPrepareOptionsMenuImpl(onMenu);
        return onMenu.getShowMenu().getValue();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        _controller.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        CSOnMenuItem onMenuItem = new CSOnMenuItem(item);
        _controller.onOptionsItemSelectedImpl(onMenuItem);
        return onMenuItem.getConsumed().getValue();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        _controller.onRequestPermissionsResult(new CSRequestPermissionResult(requestCode, permissions, grantResults));
    }
}
