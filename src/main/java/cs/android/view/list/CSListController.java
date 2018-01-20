package cs.android.view.list;

import android.content.res.Configuration;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import cs.android.lang.CSWork;
import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSReturnWith;
import cs.java.callback.CSRunWithWith;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;

import static android.os.SystemClock.uptimeMillis;
import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.obtain;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.event;
import static cs.java.lang.CSLang.fire;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.no;

public class CSListController<RowType> extends CSViewController<AbsListView> {
    public final CSEvent<CSList<RowType>> onLoad = event();
    private final CSList<RowType> _dataList = list();
    private CSReturnWith<CSIRowView<RowType>, Integer> _createView;
    private BaseAdapter _listAdapter = new CSListAdapter(this);
    private int _savedSelectionIndex;
    private int _firstVisiblePosition;
    private int _itemsTypesCount = 1;
    private CSView _emptyView;
    private boolean _firstLoad;
    private SparseBooleanArray _savedCheckedItems;
    private CSRunWithWith<Integer, CSIRowView<RowType>> _onItemClick;
    private CSReturnWith<Integer, Integer> _positionViewType;
    private CSRunWithWith<Integer, CSIRowView<RowType>> _onItemLongClick;
    private CSReturnWith<Boolean, Integer> _isEnabled;
    private CSWork _autoReload;

    public CSListController(CSViewController parent, int listViewId, CSReturnWith<CSIRowView<RowType>, Integer> createView) {
        super(parent, listViewId);
        _createView = createView;
    }

    public CSListController<RowType> onCreateView(CSReturnWith<CSIRowView<RowType>, Integer> createView) {
        _createView = createView;
        return this;
    }

    public CSListController(CSViewController parent, int listViewId) {
        super(parent, listViewId);
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
        SparseBooleanArray positions = asView().getCheckedItemPositions();
        if (positions != null) for (int i = 0; i < positions.size(); i++)
            if (positions.valueAt(i)) {
                RowType checkedRow = data().at(positions.keyAt(i));
                if (is(checkedRow)) checkedRows.add(checkedRow);
            }
        return checkedRows;
    }

    int getItemTypesCount() {
        return _itemsTypesCount;
    }

    @SuppressWarnings({"unchecked"})
    public View getRowView(int position, View view) {
        CSIRowView<RowType> rowView;
        if (no(view)) {
            rowView = createView(getItemViewType(position));
            view = rowView.asView();
            view.setTag(rowView);
        } else rowView = asRowView(view);
        rowView.row(_dataList.get(position));
        return view;
    }

    @SuppressWarnings({"unchecked"})
    private CSIRowView<RowType> asRowView(View view) {
        return (CSIRowView<RowType>) view.getTag();
    }

    public CSListController<RowType> loadAdd(CSList<RowType> list) {
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

    public CSListController<RowType> reload(CSList<RowType> list) {
//        saveState();
        _dataList.clear();
        loadAdd(list);
//        restoreState();
        return this;
    }

    public void reloadData() {
        _listAdapter.notifyDataSetChanged();
        updateEmptyView();
    }

    public void restoreSelectionAndScrollState() {
        if (asView() instanceof ListView)
            asListView().setSelectionFromTop(_firstVisiblePosition, 0);
        if (_savedSelectionIndex > -1) asAbsListView().setSelection(_savedSelectionIndex);
        if (is(_savedCheckedItems)) for (int i : iterate(_savedCheckedItems.size()))
            if (_savedCheckedItems.valueAt(i)) asAbsListView()
                    .setItemChecked(i, _savedCheckedItems.valueAt(i));
    }

    public void saveSelectionAndScrollState() {
        if (asView() instanceof ListView)
            _firstVisiblePosition = asListView().getFirstVisiblePosition();
        _savedSelectionIndex = asAbsListView().getSelectedItemPosition();
        _savedCheckedItems = asAbsListView().getCheckedItemPositions();
    }

    public void scrollToTop() {
        asAbsListView().setSelection(0);
        asAbsListView().dispatchTouchEvent(obtain(uptimeMillis(), uptimeMillis(), ACTION_CANCEL, 0, 0, 0));
    }

    public CSView emptyView(int id) {
        return emptyView(new CSView<>(parentController(), id));
    }

    public CSView emptyView(CSView view) {
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

    protected CSIRowView<RowType> createView(int viewType) {
        return _createView.invoke(viewType);
    }

    protected final int getItemViewType(int position) {
        return is(_positionViewType) ? _positionViewType.invoke(position) : position;
    }

    protected void onCreate() {
        super.onCreate();
        asAdapterView().setAdapter(_listAdapter);
        asAbsListView().setFastScrollEnabled(YES);
        asAbsListView().setOnItemClickListener((parent, view, position, id) -> {
            if (is(_onItemClick)) _onItemClick.run(position, asRowView(view));
        });
        asAbsListView().setOnItemLongClickListener((parent, view, position, id) -> {
            if (is(_onItemLongClick)) _onItemLongClick.run(position, asRowView(view));
            return true;
        });
    }

    int getItemsCount() {
        return _dataList.size();
    }

    public CSListController<RowType> viewTypes(int itemsTypesCount) {
        _itemsTypesCount = itemsTypesCount;
        return this;
    }

    public CSListController<RowType> onItemClick(CSRunWithWith<Integer, CSIRowView<RowType>> onItemClick) {
        _onItemClick = onItemClick;
        return this;
    }

    public CSListController<RowType> onItemLongClick(CSRunWithWith<Integer, CSIRowView<RowType>> onItemLongClick) {
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

    public void unCheckAll() {
        SparseBooleanArray positions = asView().getCheckedItemPositions();
        if (positions != null) for (int i = 0; i < positions.size(); i++) {
            boolean checked = positions.valueAt(i);
            if (checked) {
                int index = positions.keyAt(i);
                asAbsListView().setItemChecked(index, NO);
            }
        }
    }

    public boolean isEnabled(int position) {
        if (is(_isEnabled)) return _isEnabled.invoke(position);
        return true;
    }

    public CSListController setEmptyText(int emptyView, String message) {
        emptyView(emptyView).text(message);
        return this;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        saveSelectionAndScrollState();
        asAdapterView().setAdapter(_listAdapter);
        restoreSelectionAndScrollState();
    }

}
