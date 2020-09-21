package renetik.android.listview

import android.content.res.Configuration
import android.util.SparseBooleanArray
import android.view.View
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.ListView.CHOICE_MODE_SINGLE
import renetik.android.controller.base.CSViewController
import renetik.android.extensions.findView
import renetik.android.extensions.view
import renetik.android.java.event.event
import renetik.android.java.extensions.collections.at
import renetik.android.java.extensions.collections.index
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.reload
import renetik.android.view.extensions.*

open class CSListController<RowType : Any, ViewType : AbsListView> : CSViewController<ViewType> {

    internal val onLoad = event<List<RowType>>()
    internal var viewTypesCount = 1
    private val filteredData = list<RowType>()
    private val data = list<RowType>()
    private var dataFilter: ((data: List<RowType>) -> List<RowType>)? = null
    private var listAdapter: BaseAdapter = CSListAdapter(this)
    private val createView: (CSListController<RowType, ViewType>).(Int) -> CSRowView<RowType>
    private var firstVisiblePosition: Int = 0
    private var emptyView: View? = null
        set(value) {
            field = value?.hide()
        }
    private var onItemClick: ((CSRowView<RowType>) -> Unit)? = null
    private var onItemClickViewId: Int? = null
    private var onItemLongClick: ((CSRowView<RowType>) -> Unit)? = null
    private var onPositionViewType: ((Int) -> Int)? = null
    private var onIsEnabled: ((Int) -> Boolean)? = null
    private var savedSelectionIndex: Int = 0
    private var savedCheckedItems: SparseBooleanArray? = null
    val checkedRows: List<RowType>
        get() {
            val checkedRows = list<RowType>()
            val positions = view.checkedItemPositions
            if (positions != null)
                for (i in 0 until positions.size())
                    if (positions.valueAt(i))
                        filteredData.at(positions.keyAt(i))
                            ?.let { checkedRow -> checkedRows.add(checkedRow) }
            return checkedRows
        }

    constructor(
        parent: CSViewController<*>, view: ViewType,
        createView: (CSListController<RowType, ViewType>).(Int) -> CSRowView<RowType>
    ) : super(parent, view) {
        this.createView = createView
    }

    constructor(
        parent: CSViewController<*>, listViewId: Int,
        createView: (CSListController<RowType, ViewType>).(Int) -> CSRowView<RowType>
    ) : super(parent, listViewId) {
        this.createView = createView
    }

    override fun onCreate() {
        super.onCreate()
        view.adapter = listAdapter
        view.isFastScrollEnabled = true
        onItemClickViewId ?: onItemClick?.let {
            view.setOnItemClickListener { _, view, _, _ -> it.invoke(asRowView(view)) }
        }
        onItemLongClick?.let {
            view.setOnItemLongClickListener { _, view, _, _ ->
                it.invoke(asRowView(view))
                true
            }
        }
    }

    override fun onViewShowingFirstTime() {
        super.onViewShowingFirstTime()
        updateEmptyView()
    }

    fun onItemClick(function: (CSRowView<RowType>) -> Unit) =
        apply { onItemClick = function }

    fun emptyView(id: Int) =
        apply { emptyView = parentController?.findView(id) }

    fun onItemClick(itemClickViewId: Int, function: (CSRowView<RowType>) -> Unit) = apply {
        onItemClickViewId = itemClickViewId
        onItemClick = function
    }

    fun clear() = apply {
        data.clear()
        reload()
    }

    fun getRowView(position: Int, view: View?): View {
        val rowView = if (view == null) createView(position) else asRowView(view)
        rowView.load(filteredData[position], position)
        return rowView.view
    }

    private fun createView(position: Int): CSRowView<RowType> =
        createView.invoke(this, getItemViewType(position)).also { createdView ->
            onItemClickViewId?.let {
                createdView.view(it).onClick { onItemClick?.invoke(createdView) }
            }
        }

    @Suppress("UNCHECKED_CAST")
    private fun asRowView(view: View) = view.tag as CSRowView<RowType>

    fun load(list: List<RowType>) = apply {
        data.addAll(list)
        reload()
        onLoad.fire(list)
        return this
    }

    fun prependData(item: RowType) = apply {
        data.add(0, item)
        reload()
    }

    fun reload(list: List<RowType>) = apply {
        data.clear()
        load(list)
    }

    fun reload() {
        dataFilter?.let { filter -> filteredData.reload(filter(data)) } ?: filteredData.reload(data)
        listAdapter.notifyDataSetChanged()
        updateEmptyView()
    }

    private fun restoreSelectionAndScrollState() {
        (view as? ListView)?.setSelectionFromTop(firstVisiblePosition, 0)
        if (savedSelectionIndex > -1) view.setSelection(savedSelectionIndex)
        savedCheckedItems?.let { item ->
            for (i in 0 until item.size())
                if (item.valueAt(i)) view.setItemChecked(i, item.valueAt(i))
        }
    }

    private fun saveSelectionAndScrollState() {
        (view as? ListView)?.let { firstVisiblePosition = it.firstVisiblePosition }
        savedSelectionIndex = view.selectedItemPosition
        savedCheckedItems = view.checkedItemPositions
    }

    fun getItemViewType(position: Int) = onPositionViewType?.invoke(position) ?: position

    fun filter(function: (data: List<RowType>) -> List<RowType>) =
        apply { dataFilter = function }

    fun onItemLongClick(function: (CSRowView<RowType>) -> Unit) =
        apply { onItemLongClick = function }

    fun onPositionViewType(function: (Int) -> Int) = apply { onPositionViewType = function }

    fun dataAt(position: Int) = filteredData.at(position)

    val dataCount: Int get() = filteredData.size

    fun onIsEnabled(function: (Int) -> Boolean) = apply { onIsEnabled = function }

    fun isEnabled(position: Int) = onIsEnabled?.invoke(position) ?: true

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

    private fun updateEmptyView() {
        emptyView?.let {
            if (filteredData.isEmpty()) it.fadeIn()
            else it.fadeOut()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        saveSelectionAndScrollState()
        view.adapter = listAdapter
        restoreSelectionAndScrollState()
    }

    fun selectedIndex(index: Int) = apply {
        view.choiceMode = CHOICE_MODE_SINGLE
        view.setItemChecked(index, true)
        view.setSelectionFromTop(index, 0)
        view.setSelection(index)
    }

    fun selectedValue(value: RowType) = apply {
        data.index(value)?.let {
            selectedIndex(it)
        } ?: let {
            view.clearChoices()
            view.scrollToTop()
        }
    }
}