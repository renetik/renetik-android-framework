package cs.android.viewbase;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import cs.java.lang.Value;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.YES;

public class OnMenu {

    private final Menu _menu;
    private final Value<Boolean> _showMenu;
    private final Activity _activity;

    public OnMenu(Activity activity, Menu menu) {
        _activity = activity;
        _menu = menu;
        _showMenu = new Value<>(false);
    }

    public MenuItem find(int id) {
        return _menu.findItem(id);
    }

    public OnMenu hide(int id) {
        find(id).setVisible(NO);
        return this;
    }

    public MenuItem show(int id) {
        MenuItem menuItem = find(id);
        menuItem.setVisible(YES);
        return menuItem;
    }

    public boolean showMenu() {
        return _showMenu.get();
    }

    public void showMenu(boolean showMenu) {
        _showMenu.set(showMenu);
    }

    public void inflate(int menuId) {
        _activity.getMenuInflater().inflate(menuId, _menu);
        showMenu(YES);
    }


}