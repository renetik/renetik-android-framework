package renetik.android.view.menu;

import renetik.android.viewbase.CSView;
import renetik.android.java.callback.CSRunWith;

import static renetik.android.lang.CSLang.YES;
import static renetik.android.lang.CSLang.run;

/**
 * Created by renetik on 25/12/17.
 */

public class CSCustomMenuItem {
    private int _index;
    private CharSequence _title;
    private int _iconResource;
    private CSRunWith<CSCustomMenuItem> _onClick;
    private String _note;
    private boolean _closeMenu = YES;
    private CSView _customView;

    public CSCustomMenuItem(int index, CharSequence title) {
        _index = index;
        _title = title;
    }

    public CSCustomMenuItem icon(int iconResource) {
        _iconResource = iconResource;
        return this;
    }

    public int iconResource() {
        return _iconResource;
    }

    public CharSequence title() {
        return _title;
    }

    public CSCustomMenuItem title(CharSequence title) {
        _title = title;
        return this;
    }

    public int index() {
        return _index;
    }

    public CSCustomMenuItem onClick(CSRunWith<CSCustomMenuItem> onClick) {
        _onClick = onClick;
        return this;
    }

    public String note() {
        return _note;
    }

    public CSCustomMenuItem onClick() {
        run(_onClick, this);
        return this;
    }

    public CSCustomMenuItem note(String note) {
        _note = note;
        return this;
    }

    public CSCustomMenuItem closeMenu(boolean closeMenu) {
        _closeMenu = closeMenu;
        return this;
    }

    public boolean isCloseMenu() {
        return _closeMenu;
    }

    public CSCustomMenuItem customView(CSView view) {
        _customView = view;
        return this;
    }

    public CSView customView() {
        return _customView;
    }
}
