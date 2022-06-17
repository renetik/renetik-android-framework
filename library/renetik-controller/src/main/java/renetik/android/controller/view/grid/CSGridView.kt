package renetik.android.controller.view.grid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.base.asCS
import renetik.android.controller.base.findView
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.listen
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.event.register
import renetik.android.view.*
import renetik.android.widget.scrollToIndex
import renetik.android.core.kotlin.collections.list

@Suppress("UNCHECKED_CAST")
class CSGridView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (CSGridView<ItemType>) -> CSGridItemView<ItemType>
) : CSView<GridView>(parent, viewId) {

    val selectedItem: CSEventProperty<ItemType?> = property(null)
    private var listAdapter = Adapter()
    val data = list<ItemType>()

    fun reload(iterable: Iterable<ItemType>) = apply {
        data.clear()
        load(iterable)
    }

    fun load(iterable: Iterable<ItemType>) = apply {
        data.addAll(iterable)
        reload()
    }

    fun reload() {
        listAdapter.notifyDataSetChanged()
        updateEmptyView()
    }

    private val onItemSelected = event<CSGridItemView<ItemType>>()
    fun onSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onItemSelected.listen { function(it) } }

    val onReSelected = event<CSGridItemView<ItemType>>()
    fun onReSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onReSelected.listen { function(it) } }

    val onDisabledItemClick = event<CSGridItemView<ItemType>>()
    fun onDisabledItemClick(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onDisabledItemClick.listen { function(it) } }

    val onItemActivated = event<CSGridItemView<ItemType>>()
    fun onActive(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onItemActivated.listen { function(it) } }

    init {
        view.adapter = listAdapter
        view.isFastScrollEnabled = true
        onItemSelected.listen { selectedItem.value(it.value) }
    }

    private fun CSGridItemView<ItemType>.updateSelection() {
        val isActive = selectedItem.value == value
        isSelected = isActive && onReSelected.isListened
        isActivated = isActive && !onReSelected.isListened
        if (isActive) onItemActivated.fire(this)
    }

    private fun loadView(toReuseView: View?, position: Int): View {
        var rowView = toReuseView?.asCS<CSGridItemView<ItemType>>()
        if (rowView == null) {
            rowView = createView(this)
            register(selectedItem.onChange { rowView.updateSelection() })
            rowView.view.onClick { rowView.onClick() }
        }
        rowView.load(data[position], position)
        rowView.updateDisabled()
        rowView.updateSelection()
        return rowView.view
    }

    private fun CSGridItemView<ItemType>.updateDisabled() {
        if (itemDisabled) {
            if (onDisabledItemClick.isListened) view.alphaToDisabled()
            else view.disabledByAlpha()
        } else {
            view.disabledByAlpha(false)
            view.alpha = 1f
        }
    }

    private fun CSGridItemView<ItemType>.onClick() =
        if (selectedItem.value != this.value) {
            if (itemDisabled) onDisabledItemClick.fire(this)
            else onItemSelected.fire(this)
        } else onReSelected.fire(this)

    private var emptyView: View? = null
    fun emptyView(id: Int) = apply { emptyView = parent.findView(id) }
    private fun updateEmptyView() {
        emptyView?.let { if (data.isEmpty()) it.fadeIn() else it.fadeOut() }
    }

    fun scrollToActive(smooth: Boolean = true) = apply {
        view.scrollToIndex(data.indexOf(selectedItem.value), smooth)
    }

    inner class Adapter : BaseAdapter() {
        override fun getCount() = data.size
        override fun getViewTypeCount() = 1
        override fun isEnabled(position: Int) = true
        override fun getItem(position: Int) = data[position]
        override fun getItemViewType(position: Int) = 0
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, toReuseView: View?, parent: ViewGroup) =
            loadView(toReuseView, position)
    }
}

