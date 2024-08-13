package renetik.android.controller.view.grid

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.base.asCS
import renetik.android.controller.view.grid.item.CSGridItemView
import renetik.android.core.kotlin.collections.firstIndex
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.unexpected
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.plus
import renetik.android.event.util.CSLater.later
import renetik.android.ui.extensions.findView
import renetik.android.ui.extensions.view.alphaToDisabled
import renetik.android.ui.extensions.view.disabledByAlpha
import renetik.android.ui.extensions.view.fadeIn
import renetik.android.ui.extensions.view.fadeOut
import renetik.android.ui.extensions.view.mediumAnimationDuration
import renetik.android.ui.extensions.view.shortAnimationDuration

typealias GridViewOut<T> = CSGridView<T, out CSGridItemView<T>>
typealias GridView<T> = CSGridView<T, CSGridItemView<T>>

class CSGridView<ItemType : Any, ViewType : CSGridItemView<ItemType>>(
    parent: CSActivityView<*>, viewId: Int,
    val createView: (
        CSGridView<ItemType, ViewType>, viewType: Int, parent: ViewGroup,
    ) -> ViewType
) : CSView<RecyclerView>(parent, viewId) {

    constructor(
        parent: CSActivityView<*>, viewId: Int,
        createView: (CSGridView<ItemType, ViewType>, parent: ViewGroup) -> ViewType
    ) : this(parent, viewId, { viewParent, _, group -> createView(viewParent, group) })

    constructor(
        parent: CSActivityView<*>, viewId: Int,
        createView: (CSGridView<ItemType, ViewType>) -> ViewType
    ) : this(parent, viewId, { viewParent, _, group -> createView(viewParent) })

    val selectedItem: CSProperty<ItemType?> = property(null)
    val data = list<Pair<ItemType, Int>>()

    private val adapter = Adapter()

    fun reload(iterable: Iterable<Pair<ItemType, Int>>) = apply {
        data.clear()
        load(iterable)
    }

    fun load(iterable: Iterable<Pair<ItemType, Int>>) = apply {
        data.addAll(iterable)
        reload()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun reload() {
        adapter.notifyDataSetChanged()
        updateEmptyView()
    }

    val eventItemSelected = event<ViewType>()
    fun onItemSelected(function: (ViewType) -> Unit) =
        apply { eventItemSelected.listen(function) }

    val eventItemReSelected = event<ViewType>()
    fun onItemReSelected(function: (ViewType) -> Unit) =
        apply { eventItemReSelected.listen(function) }

    val eventDisabledItemClick = event<ViewType>()
    fun onDisabledItemClick(function: (ViewType) -> Unit) =
        apply { eventDisabledItemClick.listen(function) }

    val eventItemActivated = event<ViewType>()
    fun onItemActive(function: (ViewType) -> Unit) =
        apply { eventItemActivated.listen(function) }

    init {
        view.adapter = adapter
        this + eventItemSelected.listen { selectedItem.value(it.value) }
    }

    private fun ViewType.updateSelection() {
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

    fun onItemClick(item: ViewType) {
        if (selectedItem.value != item.value) {
            if (item.itemDisabled) eventDisabledItemClick.fire(item)
            else eventItemSelected.fire(item)
        } else eventItemReSelected.fire(item)
    }

    private var emptyView: View? = null
    fun emptyView(id: Int) = apply { emptyView = parentView.findView(id) }
    private fun updateEmptyView() {
        emptyView?.let { if (data.isEmpty()) it.fadeIn() else it.fadeOut() }
    }

    fun scrollToActive(
        smooth: Boolean = false, animationDuration: Int = mediumAnimationDuration,
    ) = apply {
        val position = data.firstIndex { it.first == selectedItem.value } ?: return@apply
        if (smooth) {
//            val scroller = CSCenteringRecyclerSmoothScroller(this, animationDuration)
            val scroller = LinearSmoothScroller(this) // Isn't this better?
            scroller.targetPosition = position
            later(shortAnimationDuration) {
                view.layoutManager?.startSmoothScroll(scroller) ?: unexpected()
            }
        } else view.scrollToPosition(position)
    }

    private class AdapterViewHolder(val view: View) : RecyclerView.ViewHolder(view)
    private inner class Adapter : RecyclerView.Adapter<AdapterViewHolder>() {

        override fun onCreateViewHolder(group: ViewGroup, type: Int): AdapterViewHolder {
            val parent = this@CSGridView
            val itemView: ViewType = createView(parent, type, group)
            // selectedItem will get fired if view is dismissed on selection in subsequent views.
            parent + selectedItem.onChange { itemView.updateSelection() }
            return AdapterViewHolder(itemView.view)
        }

        override fun onBindViewHolder(viewHolder: AdapterViewHolder, position: Int) {
            if (isDestructed) return // There was null pointer ex here...
            viewHolder.view.asCS<ViewType>()?.apply {
                load(data[position].first, position)
                updateDisabled()
                updateSelection()
            }
        }

        override fun getItemViewType(position: Int): Int = data[position].second

        override fun getItemCount() = data.size
    }
}

