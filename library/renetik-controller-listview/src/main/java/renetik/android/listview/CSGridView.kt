package renetik.android.listview

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
import renetik.android.framework.event.property.CSEventPropertyFunctions
import renetik.android.framework.event.resume
import renetik.android.java.extensions.collections.list
import renetik.android.view.extensions.fadeIn
import renetik.android.view.extensions.fadeOut
import renetik.android.view.onClick
import renetik.android.widget.scrollToIndex

@Suppress("UNCHECKED_CAST")
class CSGridView<ItemType : Any>(
    val parent: CSActivityView<*>, viewId: Int,
    val createView: (CSGridView<ItemType>) -> CSGridItemView<ItemType>)
    : CSView<GridView>(parent, viewId) {

    private val property: CSEventProperty<ItemType?> = CSEventPropertyFunctions.property(null)
    private var listAdapter = Adapter()
    private val data = list<ItemType>()

    init {
        view.adapter = listAdapter
        view.isFastScrollEnabled = true
    }

    val dataCount get() = data.size

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
        return this
    }

    fun reload() {
        listAdapter.notifyDataSetChanged()
        updateEmptyView()
    }

    private val onItemSelected = event<ItemType>()
    fun onSelected(function: (ItemType) -> Unit) = apply { onItemSelected.listen { function(it) } }

    val onReSelected = event<ItemType>()
    fun onReSelected(function: (ItemType) -> Unit) = apply { onReSelected.listen { function(it) } }

    val onItemActivated = event<CSGridItemView<ItemType>>()
    fun onActive(function: (CSGridItemView<ItemType>) -> Unit) =
        apply { onItemActivated.listen { function(it) } }

    private fun CSGridItemView<ItemType>.updateSelection() {
        val isActive = property.value == row
        isSelected = isActive && !onReSelected.isListened
        isActivated = isActive && onReSelected.isListened
        if (isActive) onItemActivated.fire(this)
    }

    private fun loadView(toReuseView: View?, position: Int): View {
        var rowView = toReuseView?.tag as? CSGridItemView<ItemType>
        if (rowView == null) {
            rowView = createView(this)
            parent.register(property.onChange { rowView.updateSelection() })
            rowView.view.onClick {
                if (property.value != rowView.row) {
                    property.value = rowView.row
                    onItemSelected.fire(rowView.row)
                } else {
                    onReSelected.fire(rowView.row)
                }
            }
        }
        rowView.load(data[position], position)
        rowView.updateSelection()
        return rowView.view
    }

    private var emptyView: View? = null
    fun emptyView(id: Int) = apply { emptyView = parent.findView(id) }
    private fun updateEmptyView() {
        emptyView?.let { if (data.isEmpty()) it.fadeIn() else it.fadeOut() }
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