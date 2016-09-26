package cs.android.viewbase;

import android.view.MenuItem;

import cs.java.lang.Value;

import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.no;

public class OnMenuItem {
    private final Value<Boolean> _consumed;
    private final MenuItem _item;

    public OnMenuItem(MenuItem item) {
        _item = item;
        _consumed = new Value<>(false);
    }

    public boolean consume(int id) {
        if (_consumed.get()) return NO;
        _consumed.set(_item.getItemId() == id);
        return _consumed.get();
    }

    public boolean consume(MenuItem item) {
        if (no(item)) return NO;
        return consume(item.getItemId());
    }

    public boolean consumed() {
        return _consumed.get();
    }
}