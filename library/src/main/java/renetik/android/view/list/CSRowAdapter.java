package renetik.android.view.list;

import android.content.Context;
import android.view.View;

import renetik.android.json.CSJsonData;
import renetik.android.viewbase.CSView;

/**
 * Created by renetik on 17/12/17.
 */

public abstract class CSRowAdapter<Data extends CSJsonData> implements CSIRowView<CSListRow<Data>> {
    private CSView<?> _parent;
    private CSListRow<Data> _data;

    public CSRowAdapter(CSView<?> parent) {
        _parent = parent;
    }

    public CSListRow<Data> data() {
        return _data;
    }

    public void data(CSListRow<Data> row) {
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
