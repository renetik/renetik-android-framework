package renetik.android.view.list

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

class CSListAdapter(private val controller: CSListController<*, *>) : BaseAdapter() {

    override fun getCount() = controller.data.count()

    override fun getViewTypeCount() = controller.viewTypesCount

    override fun isEnabled(position: Int) = controller.isEnabled(position)

    override fun getItem(position: Int) = controller.data.at(position)

    override fun getItemViewType(position: Int) = controller.getItemViewType(position)

    override fun getItemId(position: Int) = position.toLong()

    override fun getView(position: Int, view: View?, parent: ViewGroup?) =
            controller.getRowView(position, view)
}