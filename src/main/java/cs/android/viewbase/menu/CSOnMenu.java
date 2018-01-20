package cs.android.viewbase.menu;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import cs.java.lang.CSValue;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;

public class CSOnMenu {

    private final Menu _menu;
    private final CSValue<Boolean> _showMenu;
    private final Activity _activity;

    public CSOnMenu(Activity activity, Menu menu) {
        _activity = activity;
        _menu = menu;
        _showMenu = new CSValue<>(false);
    }

    public MenuItem find(int id) {
        return _menu.findItem(id);
    }

    public CSOnMenu hide(int id) {
        find(id).setVisible(NO);
        return this;
    }

    public MenuItem show(int id) {
        MenuItem menuItem = find(id);
        menuItem.setVisible(YES);
        return menuItem;
    }

    public boolean showMenu() {
        return _showMenu.getValue();
    }

    public void showMenu(boolean showMenu) {
        _showMenu.set(showMenu);
    }

    public void inflate(int menuId) {
        _activity.getMenuInflater().inflate(menuId, _menu);
        showMenu(YES);
    }


}