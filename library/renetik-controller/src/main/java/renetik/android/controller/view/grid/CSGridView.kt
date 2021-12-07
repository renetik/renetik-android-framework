package renetik.android.controller.view.grid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.base.findView
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.event.resume
import renetik.android.view.alphaToDisabled
import renetik.android.view.disabledByAlpha
import renetik.android.view.fadeIn
import renetik.android.view.fadeOut
import renetik.android.view.onClick
import renetik.android.widget.scrollToIndex
import renetik.kotlin.collections.list

@Suppress("UNCHECKED_CAST")
class CSGridView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (CSGridView<ItemType>) -> CSGridItemView<ItemType>
) : CSView<GridView>(parent, viewId) {

    val activeItem: CSEventProperty<ItemType?> = property(null)
    private var listAdapter = Adapter()
    val data = list<ItemType>()

    init {
        view.adapter = listAdapter
        view.isFastScrollEnabled = true
    }

    val dataCount get() = data.size

    fun property(property: CSEventProperty<ItemType>) = apply {
        val registration = parent.register(property
            .onChange { activeItem.value(property.value) })
        activeItem.onChange {
            registration.pause()
            property.value = it!!
            registration.resume()
        }
        activeItem.value(property.value)
    }

    @JvmName("propertyNullableItem")
    fun property(property: CSEventProperty<ItemType?>) = apply {
        val registration = parent.register(property
            .onChange { activeItem.value(property.value) })
        activeItem.onChange {
            registration.pause()
            property.value = it
            registration.resume()
        }
        activeItem.value(property.value)
    }

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

    val onItemSelected = event<CSGridItemView<ItemType>>()
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

    private fun CSGridItemView<ItemType>.updateSelection() {
        val isActive = activeItem.value == value
        isSelected = isActive && !onReSelected.isListened
        isActivated = isActive && onReSelected.isListened
        if (isActive) onItemActivated.fire(this)
    }

    private fun loadView(toReuseView: View?, position: Int): View {
        var rowView = toReuseView?.tag as? CSGridItemView<ItemType>
        if (rowView == null) {
            rowView = createView(this)
            parent.register(activeItem.onChange { rowView.updateSelection() })
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
        if (activeItem.value != this.value) {
            if (itemDisabled) onDisabledItemClick.fire(this)
            else {
                activeItem.value = this.value
                onItemSelected.fire(this)
            }
        } else onReSelected.fire(this)

    private var emptyView: View? = null
    fun emptyView(id: Int) = apply { emptyView = parent.findView(id) }
    private fun updateEmptyView() {
        emptyView?.let { if (data.isEmpty()) it.fadeIn() else it.fadeOut() }
    }

    fun scrollToActive() = apply { view.scrollToIndex(data.indexOf(activeItem.value)) }

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

