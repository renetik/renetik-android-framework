package renetik.android.controller.view.grid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.event.pause
import renetik.android.framework.event.property.CSEventProperty
import renetik.android.framework.event.property.CSEventPropertyFunctions
import renetik.android.framework.event.resume
import renetik.android.view.onClick
import renetik.android.widget.scrollToIndex
import renetik.kotlin.collections.list

@Suppress("UNCHECKED_CAST")
class CSSimpleGridView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (CSSimpleGridView<ItemType>, position: Int) -> CSGridItemView<ItemType>
) : CSView<GridView>(parent, viewId) {

    val property: CSEventProperty<ItemType?> = CSEventPropertyFunctions.property(null)
    private var listAdapter = Adapter()
    private val data = list<ItemType>()

    init {
        view.adapter = listAdapter
    }

    fun property(property: CSEventProperty<ItemType>) = apply {
        val registration = parent.register(property
            .onChange { this.property.value(property.value) })
        this.property.onChange {
            registration.pause()
            property.value = it!!
            registration.resume()
        }
        this.property.value(property.value)
    }

    @JvmName("propertyNullableItem")
    fun property(property: CSEventProperty<ItemType?>) = apply {
        val registration = parent.register(property
            .onChange { this.property.value(property.value) })
        this.property.onChange {
            registration.pause()
            property.value = it
            registration.resume()
        }
        this.property.value(property.value)
    }

    fun reload(list: Iterable<ItemType>) = apply {
        data.clear()
        load(list)
    }

    fun load(list: Iterable<ItemType>) = apply {
        data.addAll(list)
        reload()
    }

    fun reload() = listAdapter.notifyDataSetChanged()

    val onItemSelected = event<CSGridItemView<ItemType>>()
    fun onSelected(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onItemSelected.listen { function(it) } }

    private fun CSGridItemView<ItemType>.updateSelection() {
        isSelected = property.value == row
    }

    private fun loadView(toReuseView: View?, position: Int): View {
        val rowView = createView(this, position)
        parent.register(property.onChange { rowView.updateSelection() })
        rowView.view.onClick { rowView.onClick() }
        rowView.load(data[position], position)
        rowView.updateSelection()
        return rowView.view
    }

    private fun CSGridItemView<ItemType>.onClick() {
        if (property.value != this.row) {
            property.value = this.row
            onItemSelected.fire(this)
        }
    }

    fun scrollToActive() = view.scrollToIndex(data.indexOf(property.value))

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