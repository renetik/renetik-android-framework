package cs.android.view.list;

import android.view.View;

import cs.android.viewbase.CSLayoutId;
import cs.android.viewbase.CSView;

/**
 * Created by renetik on 08/12/17.
 */

public class CSRowView<T> extends CSView<View> implements CSIRowView<T> {
    private T _row;

    public CSRowView(CSListController parent, CSLayoutId layout) {
        super(parent.asAdapterView(), layout);
    }

    public T row() {
        return _row;
    }

    public void row(T row) {
        _row = row;
        onLoad(_row);
    }

    protected void onLoad(T row) {
    }
}
