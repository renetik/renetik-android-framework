package cs.android.view.list;

import android.view.View;

import cs.android.CSContextInterface;
import cs.android.viewbase.CSLayoutId;
import cs.android.viewbase.CSView;

/**
 * Created by renetik on 08/12/17.
 */

public class CSRowView<T> extends CSView<View> implements CSIRowView<T> {
    private T _row;

    public CSRowView(CSContextInterface parent) {
        super(parent);
    }

    public CSRowView(CSContextInterface parent, CSLayoutId layout) {
        super(parent, layout);
    }

    public T rowData() {
        return _row;
    }

    public void rowData(T row) {
        _row = row;
        onLoadRowData(_row);
    }

    protected void onLoadRowData(T row) {
    }
}
