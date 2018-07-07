package cs.android.view.list;

import android.widget.AbsListView;

import cs.android.R;
import cs.android.json.CSJsonData;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSReturnWithWith;

import static cs.android.view.list.CSListRow.RowTypes.Row;
import static cs.java.lang.CSLang.is;

public class CSListRowController<RowType extends CSJsonData, T extends AbsListView> extends CSListController<CSListRow<RowType>, T> {

    private final CSReturnWithWith<CSIRowView<CSListRow<RowType>>, Integer, CSListRowController<RowType, T>> _createView;

    public CSListRowController(CSViewController parent, int listViewId, CSReturnWithWith<CSIRowView<CSListRow<RowType>>, Integer, CSListRowController<RowType, T>> createView) {
        super(parent, listViewId);
        _createView = createView;
        viewTypes(CSListRow.RowTypes.values().length)
                .positionViewType(position -> dataAt(position).rowType().ordinal())
                .isEnabled(position -> Row == dataAt(position).rowType());
    }

    protected final CSIRowView<CSListRow<RowType>> createView(int viewType) {
        CSIRowView<CSListRow<RowType>> rowView = _createView.invoke(viewType, this);
        if (is(rowView)) return rowView;
        return new CSRowView(this, layout(R.layout.empty));
    }
}