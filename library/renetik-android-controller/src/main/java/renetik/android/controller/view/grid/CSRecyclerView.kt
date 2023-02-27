package renetik.android.controller.view.grid

import CSCenteringRecyclerSmoothScroller
import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.base.asCS
import renetik.android.controller.view.grid.CSRecyclerView.Adapter.ViewHolder
import renetik.android.core.kotlin.collections.firstIndex
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.unexpected
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.later
import renetik.android.event.registration.register
import renetik.android.ui.extensions.findView
import renetik.android.ui.extensions.view.*

@Suppress("UNCHECKED_CAST")
class CSRecyclerView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (CSRecyclerView<ItemType>, viewType: Int,
                     parent: ViewGroup) -> CSGridItemView<ItemType>)
    : CSView<RecyclerView>(parent, viewId) {

    class CSRecyclerViewItem<ItemType>(val data: ItemType, val type: Int = 0)

    val selectedItem: CSProperty<ItemType?> = property(null)
    private var adapter = Adapter()
    val data = list<CSRecyclerViewItem<ItemType>>()

    fun reload(iterable: Iterable<CSRecyclerViewItem<ItemType>>) = apply {
        data.clear()
        load(iterable)
    }

    fun load(iterable: Iterable<CSRecyclerViewItem<ItemType>>) = apply {
        data.addAll(iterable)
        reload()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reload() {
        adapter.notifyDataSetChanged()
        updateEmptyView()
    }

    val eventItemSelected = event<CSGridItemView<ItemType>>()
    fun onItemSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventItemSelected.listen { function(it) } }

    val eventItemReSelected = event<CSGridItemView<ItemType>>()
    fun onItemReSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventItemReSelected.listen { function(it) } }

    val eventDisabledItemClick = event<CSGridItemView<ItemType>>()
    fun onDisabledItemClick(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventDisabledItemClick.listen { function(it) } }

    val eventItemActivated = event<CSGridItemView<ItemType>>()
    fun onItemActive(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventItemActivated.listen { function(it) } }

    init {
        view.adapter = adapter
        eventItemSelected.listen { selectedItem.value(it.value) }
    }

    private fun CSGridItemView<ItemType>.updateSelection() {
        if (!isLoaded) return
        val isActive = selectedItem.value == value
        isSelected = isActive && eventItemReSelected.isListened
        isActivated = isActive && !eventItemReSelected.isListened
        if (isActive) eventItemActivated.fire(this)
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

    fun onItemClick(item: CSGridItemView<ItemType>) {
        if (selectedItem.value != item.value) {
            if (item.itemDisabled) eventDisabledItemClick.fire(item)
            else eventItemSelected.fire(item)
        } else eventItemReSelected.fire(item)
    }

    private var emptyView: View? = null
    fun emptyView(id: Int) = apply { emptyView = parent.findView(id) }
    private fun updateEmptyView() {
        emptyView?.let { if (data.isEmpty()) it.fadeIn() else it.fadeOut() }
    }

    fun scrollToActive(
        smooth: Boolean = true, speedPerPixel: Int = shortAnimationDuration
    ) = apply {
        val position = data.firstIndex { it.data == selectedItem.value } ?: return@apply
        if (smooth) {
            val scroller = CSCenteringRecyclerSmoothScroller(this, speedPerPixel)
            scroller.targetPosition = position
            later(shortAnimationDuration) {
                view.layoutManager?.startSmoothScroll(scroller) ?: unexpected()
            }
        } else view.scrollToPosition(position)
    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
            val itemView: CSGridItemView<ItemType> =
                createView(this@CSRecyclerView, viewType, viewGroup)
            register(selectedItem.onChange { itemView.updateSelection() })
            return ViewHolder(itemView.view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            val rowView = viewHolder.view.asCS<CSGridItemView<ItemType>>()!!
            rowView.load(data[position].data, position)
            rowView.updateDisabled()
            rowView.updateSelection()
        }

        override fun getItemViewType(position: Int): Int = data[position].type

        override fun getItemCount() = data.size
    }
}

