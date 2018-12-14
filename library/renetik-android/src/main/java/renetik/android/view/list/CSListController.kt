package renetik.android.view.list

import android.content.res.Configuration
import android.os.SystemClock.uptimeMillis
import android.util.SparseBooleanArray
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.obtain
import android.view.View
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ListView
import renetik.android.extensions.findView
import renetik.android.extensions.view.hide
import renetik.android.extensions.view.show
import renetik.android.view.base.CSViewController
import renetik.java.collections.CSList
import renetik.java.collections.list
import renetik.java.event.event

open class CSListController<RowType : Any, ViewType : AbsListView> : CSViewController<ViewType> {

    val onLoad = event<List<RowType>>()
    val data = list<RowType>()
    var viewTypesCount = 1
    var listAdapter: BaseAdapter = CSListAdapter(this)
    private var firstLoad = false
    private lateinit var createView: (Int) -> CSRowView<RowType>
    private var firstVisiblePosition: Int = 0
    private var emptyView: View? = null
        set(value) {
            field = value?.hide()
        }
    private var onItemClick: ((Int, CSRowView<RowType>) -> Unit)? = null
    private var onPositionViewType: ((Int) -> Int)? = null
    private var onItemLongClick: ((Int, CSRowView<RowType>) -> Unit)? = null
    private var onIsEnabled: ((Int) -> Boolean)? = null
    private var savedSelectionIndex: Int = 0
    private var savedCheckedItems: SparseBooleanArray? = null

    val checkedRows: CSList<RowType>
        get() {
            val checkedRows = list<RowType>()
            val positions = view.checkedItemPositions
            if (positions != null)
                for (i in 0 until positions.size())
                    if (positions.valueAt(i))
                        data.at(positions.keyAt(i))?.let { checkedRow -> checkedRows.add(checkedRow) }
            return checkedRows
        }

    constructor(parent: CSViewController<*>, view: ViewType, createView: (Int) -> CSRowView<RowType>)
            : super(parent, view) {
        this.createView = createView
    }

    constructor(parent: CSViewController<*>, listViewId: Int) : super(parent, listViewId) {}

    fun onCreateView(function: (Int) -> CSRowView<RowType>) = apply { createView = function }

    fun clear() = apply {
        data.clear()
        reloadData()
    }

    fun getRowView(position: Int, view: View?): View {
        var rowView: CSRowView<RowType>
        if (view == null) rowView = createView.invoke(getItemViewType(position))
        else rowView = asRowView(view)
        rowView.data(data.get(position))
        return rowView.view
    }

    private fun asRowView(view: View) = view.tag as CSRowView<RowType>

    fun loadAdd(list: List<RowType>) = apply {
        firstLoad = true
        data.addAll(list)
        reloadData()
        onLoad.fire(list)
        return this
    }

    fun prependData(status: RowType) = apply {
        data.add(0, status)
        reloadData()
    }

    fun reload(list: List<RowType>) = apply {
        data.clear()
        loadAdd(list)
    }

    fun reloadData() {
        listAdapter.notifyDataSetChanged()
        updateEmptyViewVisibility()
    }

    fun restoreSelectionAndScrollState() {
        (view as? ListView)?.setSelectionFromTop(firstVisiblePosition, 0)
        if (savedSelectionIndex > -1) view.setSelection(savedSelectionIndex)
        savedCheckedItems?.let { item ->
            for (i in 0 until item.size())
                if (item.valueAt(i)) view.setItemChecked(i, item.valueAt(i))
        }
    }

    fun saveSelectionAndScrollState() {
        (view as? ListView)?.let { firstVisiblePosition = it.firstVisiblePosition }
        savedSelectionIndex = view.selectedItemPosition
        savedCheckedItems = view.checkedItemPositions
    }

    fun scrollToTop() {
        view.setSelection(0)
        view.dispatchTouchEvent(obtain(uptimeMillis(), uptimeMillis(), ACTION_CANCEL, 0f, 0f, 0))
    }

    fun getItemViewType(position: Int) = onPositionViewType?.invoke(position) ?: position

    override fun onCreate() {
        super.onCreate()
        view.setAdapter(listAdapter)
        view.isFastScrollEnabled = true
        view.setOnItemClickListener { _, view, position, _ ->
            onItemClick?.invoke(position, asRowView(view))
        }
        view.setOnItemLongClickListener { _, view, position, _ ->
            onItemLongClick?.invoke(position, asRowView(view))
            true
        }
    }

    fun onItemClick(function: (Int, CSRowView<RowType>) -> Unit) = apply { onItemClick = function }

    fun onItemLongClick(function: (Int, CSRowView<RowType>) -> Unit) = apply { onItemLongClick = function }

    fun onPositionViewType(function: (Int) -> Int) = apply { onPositionViewType = function }

    fun dataAt(position: Int) = data.at(position)

    fun onIsEnabled(function: (Int) -> Boolean) = apply { onIsEnabled = function }

    fun isEnabled(position: Int) = onIsEnabled?.invoke(position) ?: true

    fun emptyView(id: Int) = apply { emptyView = parentController?.findView(id) }

    fun checkAll() = apply { for (index in 0 until view.count) view.setItemChecked(index, true) }

    fun unCheckAll() = apply {
        val positions = view.checkedItemPositions
        if (positions != null)
            for (i in 0 until positions.size()) {
                val checked = positions.valueAt(i)
                if (checked) {
                    val index = positions.keyAt(i)
                    view.setItemChecked(index, false)
                }
            }
    }

    private fun updateEmptyViewVisibility() {
        if (!firstLoad) return
        emptyView?.let {
            if (data.isEmpty()) it.show()
            else it.hide()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        saveSelectionAndScrollState()
        view.setAdapter(listAdapter)
        restoreSelectionAndScrollState()
    }

}
