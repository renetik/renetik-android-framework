package renetik.android.listview.actions

import renetik.android.controller.base.CSActivityView
import renetik.android.controller.menu.CSMenuItem

class CSListMenuItem<RowType>(controller: CSActivityView<*>, title: String) : CSMenuItem(controller, title) {

    private var finish = true
    private var run: ((CSMenuItem, List<RowType>) -> Unit)? = null

    fun finish(finish: Boolean) = apply { this.finish = finish }

    fun onClick(function: (CSMenuItem, List<RowType>) -> Unit) = apply { run = function }

    fun run(checkedRows: List<RowType>) = apply {
        run?.invoke(this, checkedRows)
        super.run()
    }

    fun finish(): Boolean = finish
}
