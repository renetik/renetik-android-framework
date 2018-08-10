package cs.android.view.list;

import android.content.Context;
import android.view.View;

import cs.android.json.CSJsonData;
import cs.android.viewbase.CSView;

/**
 * Created by renetik on 17/12/17.
 */

public abstract class CSRowAdapter<Data extends CSJsonData> implements CSIRowView<CSListRow<Data>> {
    private CSView<?> _parent;
    private CSListRow<Data> _data;

    public CSRowAdapter(CSView<?> parent) {
        _parent = parent;
    }

    public CSListRow<Data> row() {
        return _data;
    }

    public void row(CSListRow<Data> row) {
        _data = row;
        onLoadRowData(row.data(), row.index());
    }

    protected abstract void onLoadRowData(Data row, int index);

    public View getView() {
        return _parent.getView();
    }

    public Context context() {
        return _parent.context();
    }
}
