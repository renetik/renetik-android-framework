package renetik.android.controller.view.grid

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.kotlin.collections.list
import renetik.kotlin.collections.reload

class CSGridLayout(parent: CSActivityView<*>,
                   viewId: Int,
                   vararg items: CSView<*>) : CSView<GridView>(parent, viewId) {

    private val items = list(items)
    private var adapter = Adapter()

    init {
        view.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun reload(vararg items: CSView<*>) = reload(items.toList())

    fun reload(items: List<CSView<*>>) {
        this.items.reload(items)
        adapter.notifyDataSetChanged()
    }

    inner class Adapter : BaseAdapter() {
        override fun getCount() = items.size
        override fun getViewTypeCount() = 1
        override fun isEnabled(position: Int) = true
        override fun getItem(position: Int) = items[position]
        override fun getItemViewType(position: Int) = 0
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, toReuseView: View?, parent: ViewGroup) =
            items[position].view
    }
}