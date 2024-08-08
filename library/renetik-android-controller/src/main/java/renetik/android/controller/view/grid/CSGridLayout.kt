package renetik.android.controller.view.grid

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import renetik.android.controller.base.CSView
import renetik.android.core.kotlin.collections.reload
import renetik.android.ui.extensions.set
import renetik.android.ui.protocol.CSViewInterface

// TODO: Not used, remove? 8.8.2024...
class CSGridLayout(parent: CSViewInterface, viewId: Int) :
    CSView<RecyclerView>(parent, viewId) {
    val items = mutableListOf<CSView<*>>()
    private var adapter = CSRecyclerLayoutAdapter()

    fun reload(vararg items: CSView<*>) = reload(items.toList())

    fun reload(items: List<CSView<*>>) {
        this.items.reload(items)
        adapter.notifyDataSetChanged()
    }

    private inner class CSRecyclerLayoutViewHolder(
        var view: FrameLayout
    ) : ViewHolder(view)

    private inner class CSRecyclerLayoutAdapter : Adapter<CSRecyclerLayoutViewHolder>() {

        init {
            view.adapter = this
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) =
            CSRecyclerLayoutViewHolder(FrameLayout(context))

        override fun onBindViewHolder(
            viewHolder: CSRecyclerLayoutViewHolder, position: Int) {
            if (isDestructed) return
            viewHolder.view.set(items[position])
        }

        override fun getItemCount() = items.size
    }

    override fun onDestruct() {
        items.clear()
        view.adapter = null
        super.onDestruct()
    }
}