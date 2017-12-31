package cs.android.view.menu;

import cs.java.collections.CSList;

import static cs.java.lang.CSLang.list;

/**
 * Created by renetik on 25/12/17.
 */

public class CSCustomMenuHeader {
    private CSList<CSCustomMenuItem> _items = list();
    private String _title;
    private int _index;

    public CSCustomMenuHeader(int index, String title) {
        _index = index;
        _title = title;
    }

    public int index() {
        return _index;
    }

    public String title() {
        return _title;
    }

    public CSCustomMenuItem item(CharSequence name) {
        return _items.put(new CSCustomMenuItem(_items.count(), name));
    }

    public CSList<CSCustomMenuItem> items() {
        return _items;
    }

    public void clear() {
        _items.clear();
    }
}
