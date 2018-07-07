package cs.android.viewbase.menu;

import android.view.MenuItem;

import cs.java.lang.CSValue;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.equal;
import static cs.java.lang.CSLang.no;
import static cs.java.lang.CSLang.set;

public class CSOnMenuItem {
    private final CSValue<Boolean> _consumed;
    private final MenuItem _item;

    public CSOnMenuItem(MenuItem item) {
        _item = item;
        _consumed = new CSValue<>(false);
    }

    public boolean consume(CSMenuItem item) {
        if (_consumed.getValue()) return NO;
        if (set(item.id())) _consumed.setValue(_item.getItemId() == item.id());
        else _consumed.setValue(equal(_item.getTitle(), item.title()));
        return _consumed.getValue();
    }

    public boolean consume(int id) {
        if (_consumed.getValue()) return NO;
        _consumed.setValue(_item.getItemId() == id);
        return _consumed.getValue();
    }

    public boolean consumed() {
        return _consumed.getValue();
    }

    public boolean isCheckable() {
        return _item.isCheckable();
    }

    public boolean isChecked() {
        return _item.isChecked();
    }

    public void checked(boolean checked) {
        _item.setChecked(checked);
    }

}