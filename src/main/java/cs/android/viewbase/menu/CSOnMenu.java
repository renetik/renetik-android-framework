package cs.android.viewbase.menu;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import cs.java.lang.CSValue;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.set;

public class CSOnMenu {

    private final Menu _menu;
    private final CSValue<Boolean> _showMenu;
    private final AppCompatActivity _activity;

    public CSOnMenu(AppCompatActivity activity, Menu menu) {
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

    public void show(CSMenuItem item) {
        if (set(item.id())) {
            MenuItem menuItem = find(item.id());
            menuItem.setVisible(YES);
            if (menuItem.isCheckable()) menuItem.setChecked(item.isChecked());
        } else {
            MenuItem menuItem = _menu.add(item.title());
            menuItem.setShowAsAction(item.showAsAction());
            if (menuItem.isCheckable()) menuItem.setChecked(item.isChecked());
            if (set(item.icon())) menuItem.setIcon(item.icon());
        }
    }

    public boolean showMenu() {
        return _showMenu.getValue();
    }

    public void showMenu(boolean showMenu) {
        _showMenu.setValue(showMenu);
    }

    public void inflate(int menuId) {
        _activity.getMenuInflater().inflate(menuId, _menu);
        showMenu(YES);
    }

    public Menu getMenu() {
        return _menu;
    }
}