package cs.android.view.list;

import cs.android.json.CSJSONData;
import cs.android.viewbase.CSLayoutId;
import cs.java.callback.CSRunWith;

/**
 * Created by renetik on 05/01/18.
 */

public class CSListRowView<DataType extends CSJSONData> extends CSRowView<CSListRow<DataType>> {
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
