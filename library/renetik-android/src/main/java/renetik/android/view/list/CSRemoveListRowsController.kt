package renetik.android.view.list

import android.view.ActionMode
import android.view.Menu
import android.widget.AbsListView
import renetik.android.extensions.dialog

class CSRemoveListRowsController<RowType : Any, AbsListViewType : AbsListView>(
        private val listController: CSListController<RowType, AbsListViewType>,
        question: String, onRemove: (List<RowType>) -> Unit)
    : CSListActionsMultiSelectionController<RowType, AbsListViewType>( listController) {

    private val selectAll = listMenu("Select All").finish(false)
            .onClick { _ -> listController.checkAll() }

    init {
        listMenu("Delete").onClick { _, items -> dialog(question).show { onRemove.invoke(items) } }
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        selectAll.visible(listController.data.size > 1)
        return super.onPrepareActionMode(mode, menu)
    }

}
