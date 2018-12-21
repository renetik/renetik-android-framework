package renetik.android.listview

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AbsListView
import android.widget.AbsListView.MultiChoiceModeListener
import android.widget.ListView.CHOICE_MODE_MULTIPLE_MODAL
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSOnMenu
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.controller.menu.GeneratedMenuItems
import renetik.java.collections.list

open class CSListActionsMultiSelectionController<RowType : Any, AbsListViewType : AbsListView>(
        private val parent: CSListController<RowType, AbsListViewType>)
    : CSViewController<View>(parent), MultiChoiceModeListener {

    private var menuItems = list<CSListMenuItem<RowType>>()

    protected fun listMenu(title: String) = menuItems.put(CSListMenuItem(this, title))

    override fun onCreate() {
        super.onCreate()
        parent.view.apply { choiceMode = CHOICE_MODE_MULTIPLE_MODAL }.setMultiChoiceModeListener(this)
    }

    override fun onCreateActionMode(mode: ActionMode, menu: Menu) =
            true.also { mode.title = "Edit list" }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        menu.removeGroup(GeneratedMenuItems)
        val onMenu = CSOnMenu(activity(), menu)
        for (item in menuItems) if (item.isVisible) onMenu.show(item)
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

    override fun onDestroyActionMode(mode: ActionMode) = Unit

    override fun onItemCheckedStateChanged(mode: ActionMode, position: Int, id: Long, checked: Boolean) = Unit

}
