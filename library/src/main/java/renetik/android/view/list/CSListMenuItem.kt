package renetik.android.view.list

import renetik.android.viewbase.CSViewController
import renetik.android.viewbase.menu.CSMenuItem
import renetik.java.collections.CSList


class CSListMenuItem<RowType>(controller: CSViewController<*>, title: String) : CSMenuItem(controller, title) {

    private var finish = true
    private var run: ((CSMenuItem, CSList<RowType>) -> Unit)? = null

    fun finish(finish: Boolean) = apply { this.finish = finish }

    fun onClick(function: (CSMenuItem, CSList<RowType>) -> Unit) = apply { run = function }

    fun run(checkedRows: CSList<RowType>) = apply {
        run?.invoke(this, checkedRows)
        super.run()
    }

    fun finish(): Boolean = finish
}
