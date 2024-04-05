package renetik.android.controller.view.grid

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.FrameLayout
import android.widget.GridView
import androidx.recyclerview.widget.RecyclerView
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.CSView
import renetik.android.controller.view.grid.CSRecyclerLayout.Adapter.ViewHolder
import renetik.android.core.kotlin.collections.list
import renetik.android.core.kotlin.collections.reload
import renetik.android.ui.extensions.set

class CSGridLayout(
    parent: CSActivityView<*>, viewId: Int,
    vararg items: CSView<*>
) : CSView<GridView>(parent, viewId) {
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

@SuppressLint("NotifyDataSetChanged")
class CSRecyclerLayout(
    parent: CSActivityView<*>, viewId: Int,
) : CSView<RecyclerView>(parent, viewId) {
     val items = mutableListOf<CSView<*>>()
    private var adapter = Adapter().also { view.adapter = it }

    fun reload(vararg items: CSView<*>) = reload(items.toList())

    fun reload(items: List<CSView<*>>) {
        this.items.reload(items)
        adapter.notifyDataSetChanged()
    }

    private inner class Adapter : RecyclerView.Adapter<ViewHolder>() {
        private inner class ViewHolder(var view: FrameLayout) : RecyclerView.ViewHolder(view)

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(FrameLayout(context))

        override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
            if (isDestructed) return
            viewHolder.view.set(items[position])
        }

        override fun getItemCount() = items.size
    }
}