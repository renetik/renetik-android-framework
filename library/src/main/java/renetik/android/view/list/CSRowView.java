package renetik.android.view.list;

import android.view.View;

import renetik.android.viewbase.CSLayoutId;
import renetik.android.viewbase.CSView;

/**
 * Created by renetik on 08/12/17.
 */

public class CSRowView<T> extends CSView<View> {
    private T _row;

    public CSRowView(CSListController parent, CSLayoutId layout) {
        super(parent, layout);
    }

    public T data() {
        return _row;
    }

    public void data(T row) {
        _row = row;
        onLoad(_row);
    }

    protected void onLoad(T row) {
    }
}
