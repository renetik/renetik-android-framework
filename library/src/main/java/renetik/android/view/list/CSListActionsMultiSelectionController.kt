package renetik.android.view.list

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.ListView.CHOICE_MODE_MULTIPLE_MODAL
import renetik.java.collections.list
import renetik.android.view.base.CSViewController
import renetik.android.view.menu.CSOnMenu
import renetik.android.view.menu.CSOnMenuItem
import renetik.android.view.menu.GeneratedMenuItems

open class CSListActionsMultiSelectionController<RowType : Any, AbsListViewType : AbsListView>(
        private val parent: CSListController<RowType, AbsListViewType>)
    : CSViewController<View>(parent), MultiChoiceModeListener {

    private var menuItems = list<CSListMenuItem<RowType>>()

    protected fun listMenu(title: String) = menuItems.put(CSListMenuItem<RowType>(this, title))

    override fun onCreate() {
        super.onCreate()
        parent.view.apply { choiceMode = CHOICE_MODE_MULTIPLE_MODAL }
                .setMultiChoiceModeListener(this)
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        menu.removeGroup(GeneratedMenuItems)
        val onMenu = CSOnMenu(activity(), menu)
        onMenu.onPrepareItems(menuItems)
        return onMenu.showMenu.value
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        val onMenuItem = CSOnMenuItem(item)
        for (menuItem in menuItems)
            if (onMenuItem.consume(menuItem)) {
                if (onMenuItem.isCheckable) menuItem.onChecked(onMenuItem)
                else menuItem.run(parent.checkedRows)
                if (menuItem.finish()) mode.finish()
            }
        invalidateOptionsMenu()
        return onMenuItem.isConsumed
    }

    override fun onDestroyActionMode(mode: ActionMode) {}

    override fun onItemCheckedStateChanged(mode: ActionMode, position: Int, id: Long, checked: Boolean) {}

}
