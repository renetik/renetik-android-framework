package renetik.android.view.list;

import android.widget.AbsListView;

import renetik.android.R;
import renetik.android.java.callback.CSReturnWithWith;
import renetik.android.json.CSJsonData;
import renetik.android.viewbase.CSViewController;

import static renetik.android.lang.CSLang.is;
import static renetik.android.view.list.CSListRow.RowTypes.Row;

public class CSListRowController<RowType extends CSJsonData, T extends AbsListView> extends CSListController<CSListRow<RowType>, T> {

    private final CSReturnWithWith<CSRowView<CSListRow<RowType>>, Integer, CSListRowController<RowType, T>> _createView;

    public CSListRowController(CSViewController parent, int listViewId, CSReturnWithWith<CSRowView<CSListRow<RowType>>, Integer, CSListRowController<RowType, T>> createView) {
        super(parent, listViewId);
        _createView = createView;
        viewTypes(CSListRow.RowTypes.values().length)
                .positionViewType(position -> dataAt(position).rowType().ordinal())
                .isEnabled(position -> Row == dataAt(position).rowType());
    }

    protected final CSRowView<CSListRow<RowType>> createView(int viewType) {
        CSRowView<CSListRow<RowType>> rowView = _createView.invoke(viewType, this);
        if (is(rowView)) return rowView;
        return new CSRowView(this, layout(R.layout.cs_empty));
    }
}