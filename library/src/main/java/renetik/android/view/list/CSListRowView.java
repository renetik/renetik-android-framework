package renetik.android.view.list;

import renetik.android.json.CSJsonData;
import renetik.android.viewbase.CSLayoutId;
import renetik.android.java.callback.CSRunWith;

/**
 * Created by renetik on 05/01/18.
 */

public class CSListRowView<DataType extends CSJsonData> extends CSRowView<CSListRow<DataType>> {
    private final CSRunWith<CSListRowView<DataType>> _onLoad;

    public CSListRowView(CSListController parent, CSLayoutId layout, CSRunWith<CSListRowView<DataType>> onLoad) {
        super(parent, layout);
        _onLoad = onLoad;
    }

    protected void onLoad(CSListRow<DataType> row) {
        super.onLoad(row);
        _onLoad.run(this);
    }

}
