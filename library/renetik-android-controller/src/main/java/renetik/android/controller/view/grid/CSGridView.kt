package renetik.android.controller.view.grid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.base.asCS
import renetik.android.core.kotlin.collections.list
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.plus
import renetik.android.ui.extensions.view.alphaToDisabled
import renetik.android.ui.extensions.view.disabledByAlpha
import renetik.android.ui.extensions.view.onClick

@Deprecated("Replace with CSRecyclerView")
class CSGridView<ItemType : Any, ViewType : CSGridItemView<ItemType>>(
    parent: CSActivityView<*>, viewId: Int,
    val createView: (CSGridView<ItemType, ViewType>) -> ViewType
) : CSView<GridView>(parent, viewId) {

    val selectedItem: CSProperty<ItemType?> = property(null)
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
    }

    val eventItemSelected = event<ViewType>()
    fun onItemSelected(function: (ViewType) -> Unit) =
        apply { eventItemSelected.listen { function(it) } }

    val eventItemReSelected = event<ViewType>()
    fun onItemReSelected(function: (ViewType) -> Unit) =
        apply { eventItemReSelected.listen { function(it) } }

    val eventDisabledItemClick = event<ViewType>()
    fun onDisabledItemClick(function: (ViewType) -> Unit) =
        apply { eventDisabledItemClick.listen { function(it) } }

    val eventItemActivated = event<ViewType>()
    fun onItemActive(function: (ViewType) -> Unit) =
        apply { eventItemActivated.listen { function(it) } }

    init {
        view.adapter = listAdapter
        view.isFastScrollEnabled = true
        eventItemSelected.listen { selectedItem.value(it.value) }
    }

    override fun onDestruct() {
        view.adapter = null
        super.onDestruct()
    }

    private fun ViewType.updateSelection() {
        val isActive = selectedItem.value == value
        isSelected = isActive && eventItemReSelected.isListened
        isActivated = isActive && !eventItemReSelected.isListened
        if (isActive) eventItemActivated.fire(this)
    }

    private fun loadView(toReuseView: View?, position: Int): View {
        var rowView: ViewType? = toReuseView?.asCS()
        if (rowView == null) {
            rowView = createView(this)
            this + selectedItem.onChange { rowView.updateSelection() }
            rowView.view.onClick { rowView.onClick() }
        }
        rowView.load(data[position], position)
        rowView.updateDisabled()
        rowView.updateSelection()
        return rowView.view
    }

    private fun CSGridItemView<ItemType>.updateDisabled() {
        if (itemDisabled) {
            if (eventDisabledItemClick.isListened) view.alphaToDisabled()
            else view.disabledByAlpha()
        } else {
            view.disabledByAlpha(false)
            view.alpha = 1f
        }
    }

    private fun ViewType.onClick() =
        if (selectedItem.value != this.value) {
            if (itemDisabled) eventDisabledItemClick.fire(this)
            else eventItemSelected.fire(this)
        } else eventItemReSelected.fire(this)

    inner class Adapter : BaseAdapter() {
        override fun getCount() =
            data.size

        override fun getViewTypeCount() = 1
        override fun isEnabled(position: Int) = true
        override fun getItem(position: Int) =
            data[position]

        override fun getItemViewType(position: Int) = 0
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, toReuseView: View?, parent: ViewGroup) =
            loadView(toReuseView, position)
    }
}

