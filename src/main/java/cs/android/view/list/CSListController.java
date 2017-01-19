package cs.android.view.list;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.List;

import cs.android.view.CSRowView;
import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSReturnWith;
import cs.java.callback.CSRunWithWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;

import static cs.java.lang.CSLang.*;

public class CSListController<RowType> extends CSViewController {
    private final CSList<RowType> _dataList = list();
    private final CSEvent<List<RowType>> onLoad = event();
    private final CSReturnWith<CSRowView<RowType>, Integer> _createView;
    private BaseAdapter _listAdapter = new CSListAdapter(this);
    private int savedSelectionIndex;
    private int _firstVisiblePosition;
    private int _itemsTypesCount = 1;
    private CSView _emptyView;
    private boolean _firstLoad;
    private SparseBooleanArray _savedCheckedItems;
    private CSRunWithWith<Integer, CSRowView<RowType>> _onItemClick;
    private CSReturnWith<Integer, Integer> _positionViewType;
    private CSRunWithWith<Integer, CSRowView<RowType>> _onItemLongClick;
    private CSReturnWith<Boolean, Integer> _isEnabled;

    public CSListController(CSViewController parent, int listViewId, CSReturnWith<CSRowView<RowType>, Integer> createView) {
        super(parent, listViewId);
        _createView = createView;
    }

    public BaseAdapter adapter() {
        return _listAdapter;
    }

    public void clear() {
        _dataList.clear();
        reloadData();
    }

    public CSList<RowType> data() {
        return _dataList;
    }

    public CSView emptyView() {
        return _emptyView;
    }

    public CSList<RowType> getCheckedRows() {
        final CSList<RowType> checkedRows = list();
        SparseBooleanArray positions = asAbsListView().getCheckedItemPositions();
        if (positions != null) for (int i = 0; i < positions.size(); i++)
            if (positions.valueAt(i)) checkedRows.add(data().get(positions.keyAt(i)));
        return checkedRows;
    }

    int getItemTypesCount() {
        return _itemsTypesCount;
    }

    public CSEvent<List<RowType>> getOnLoad() {
        return onLoad;
    }

    @SuppressWarnings({"unchecked"})
    public View getRowView(int position, View view) {
        CSRowView<RowType> rowView;
        if (no(view)) {
            rowView = createView(getItemViewType(position));
            view = rowView.asView();
            view.setTag(rowView);
        } else rowView = asRowView(view);
        rowView.load(_dataList.get(position));
        return view;
    }

    @SuppressWarnings({"unchecked"})
    private CSRowView<RowType> asRowView(View view) {
        return (CSRowView<RowType>) view.getTag();
    }

    public CSListController<RowType> loadAdd(List<RowType> list) {
        _firstLoad = YES;
        _dataList.addAll(list);
        reloadData();
        fire(onLoad, list);
        return this;
    }

    public void prependData(RowType status) {
        _dataList.add(0, status);
        reloadData();
    }

    public CSListController<RowType> reload(List<RowType> list) {
        saveState();
        _dataList.clear();
        loadAdd(list);
        restoreState();
        return this;
    }

    protected void onCreate(Bundle state) {
        super.onCreate(state);
        asAdapterView().setAdapter(_listAdapter);
    }

    public void reloadData() {
        _listAdapter.notifyDataSetChanged();
        updateEmptyView();
    }

    public void restoreState() {
        if (asView() instanceof ListView)
            asListView().setSelectionFromTop(_firstVisiblePosition, 0);
        if (savedSelectionIndex > -1) asAbsListView().setSelection(savedSelectionIndex);
        if (is(_savedCheckedItems)) for (int i : iterate(_savedCheckedItems.size()))
            if (_savedCheckedItems.valueAt(i)) asAbsListView()
                    .setItemChecked(i, _savedCheckedItems.valueAt(i));
    }

    public void saveState() {
        if (asView() instanceof ListView) _firstVisiblePosition = asListView()
                .getFirstVisiblePosition();
        savedSelectionIndex = asAbsListView().getSelectedItemPosition();
        _savedCheckedItems = asAbsListView().getCheckedItemPositions();
    }

    @SuppressWarnings("unused")
    public void scrollToTop() {
        asAbsListView().setSelection(0);
        asAbsListView().dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock
                .uptimeMillis(), MotionEvent.ACTION_CANCEL, 0, 0, 0));
    }

    public CSView setEmptyView(int id) {
        return setEmptyView(new CSView<>(parent(), id));
    }

    public CSView setEmptyView(CSView view) {
        _emptyView = view;
        if (is(_emptyView)) _emptyView.hide();
        return _emptyView;
    }

    public CSListController<RowType> setListAdapter(BaseAdapter listAdapter) {
        _listAdapter = listAdapter;
        return this;
    }

    private void updateEmptyView() {
        if (!_firstLoad) return;
        if (no(_emptyView)) return;
        if (_dataList.isEmpty())
            _emptyView.show();
        else _emptyView.hide();
    }

    protected final CSRowView<RowType> createView(int viewType) {
        return _createView.invoke(viewType);
    }

    protected final int getItemViewType(int position) {
        return is(_positionViewType) ? _positionViewType.invoke(position) : position;
    }

    protected void onCreate() {
        super.onCreate();
        asAbsListView().setFastScrollEnabled(YES);
        asAbsListView().setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (is(_onItemClick)) _onItemClick.run(position, asRowView(view));
            }

        });
        asAbsListView().setOnItemLongClickListener(new OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (is(_onItemLongClick)) _onItemLongClick.run(position, asRowView(view));
                return true;
            }
        });
    }

    public void onPause() {
        super.onPause();
        saveState();
    }

    public void onResume() {
         super.onResume();
        if (_dataList.hasItems()) {
            asAdapterView().setAdapter(_listAdapter);
            restoreState();
        }
    }

    int getItemsCount() {
        return _dataList.size();
    }

    public CSListController<RowType> viewTypes(int itemsTypesCount) {
        _itemsTypesCount = itemsTypesCount;
        return this;
    }

    public CSListController<RowType> onItemClick(CSRunWithWith<Integer, CSRowView<RowType>> onItemClick) {
        _onItemClick = onItemClick;
        return this;
    }

    public CSListController<RowType> onItemLongClick(CSRunWithWith<Integer, CSRowView<RowType>> onItemLongClick) {
        _onItemLongClick = onItemLongClick;
        return this;
    }

    public CSListController<RowType> positionViewType(CSReturnWith<Integer, Integer> getTtemViewType) {
        _positionViewType = getTtemViewType;
        return this;
    }

    public RowType dataAt(Integer position) {
        return this.data().at(position);
    }

    public CSListController<RowType> isEnabled(CSReturnWith<Boolean, Integer> isEnabled) {
        _isEnabled = isEnabled;
        return this;
    }

    public void checkAll() {
        for (Integer index : iterate(asAbsListView().getCount()))
            asAbsListView().setItemChecked(index, YES);
    }

    public boolean isEnabled(int position) {
        if (is(_isEnabled)) return _isEnabled.invoke(position);
        return true;
    }

    public CSListController setEmptyText(int emptyView, String message) {
        setEmptyView(emptyView).text(message);
        return this;
    }
}
