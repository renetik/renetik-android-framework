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
import renetik.android.event.registration.plus
import renetik.android.event.util.CSLater.later
import renetik.android.ui.extensions.findView
import renetik.android.ui.extensions.view.*

class CSRecyclerView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (
        CSRecyclerView<ItemType>, viewType: Int,
        parent: ViewGroup,
    ) -> CSGridItemView<ItemType>,
) : CSView<RecyclerView>(parent, viewId) {

    data class CSRecyclerViewItem<ItemType>(val data: ItemType, val type: Int = 0)

    val selectedItem: CSProperty<ItemType?> = property(null)
    val data = list<CSRecyclerViewItem<ItemType>>()

    private val adapter = Adapter()

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
        apply { eventItemSelected.listen(function) }

    val eventItemReSelected = event<CSGridItemView<ItemType>>()
    fun onItemReSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventItemReSelected.listen(function) }

    val eventDisabledItemClick = event<CSGridItemView<ItemType>>()
    fun onDisabledItemClick(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventDisabledItemClick.listen(function) }

    val eventItemActivated = event<CSGridItemView<ItemType>>()
    fun onItemActive(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { eventItemActivated.listen(function) }

    init {
        view.adapter = adapter
        this + eventItemSelected.listen { selectedItem.value(it.value) }
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
        smooth: Boolean = false, speedPerPixel: Int = shortAnimationDuration,
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
            val parent = this@CSRecyclerView
            val itemView: CSGridItemView<ItemType> = createView(parent, viewType, viewGroup)
            // selectedItem will get fired if view is dismissed on selection in subsequent views.
            parent + selectedItem.onChange { itemView.updateSelection() }
            return ViewHolder(itemView.view)
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            if (isDestructed) return // There was null pointer ex here...
            viewHolder.view.asCS<CSGridItemView<ItemType>>()?.apply {
                load(data[position].data, position)
                updateDisabled()
                updateSelection()
            }
        }

        override fun getItemViewType(position: Int): Int = data[position].type

        override fun getItemCount() = data.size
    }
}

