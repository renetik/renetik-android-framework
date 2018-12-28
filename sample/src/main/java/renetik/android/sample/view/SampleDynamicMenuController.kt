package renetik.android.sample.view

import android.view.View
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSMenuItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.checkBox
import renetik.android.extensions.title
import renetik.android.sample.R
import renetik.android.view.extensions.onChecked

class SampleDynamicMenuController(title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_dynamic_menu)) {

    private val addMenuItem: CSMenuItem = menuItem("").alwaysAsAction()

    init {
        title(R.id.SampleDynamicMenu_Title, title)
        checkBox(R.id.SampleDynamicMenu_AddMenuItemVisible)
                .onChecked { addMenuItem.visible(it.isChecked) }.isChecked = addMenuItem.isVisible
        updateAddMenuItemTitle()
        addMenuItem.onClick {
            dialog(addMenuItem.title!!).showInput("Enter menu item name", "Menu item ${menuItems.size}") { dialog ->
                menuItem(dialog.inputValue()).onClick { addedMenuItem ->
                    dialog("Remove menu item '${addedMenuItem.title}'?").show {
                        addedMenuItem.remove()
                        updateAddMenuItemTitle()
                    }
                }.neverAsAction()
                updateAddMenuItemTitle()
            }
        }
    }

    private fun updateAddMenuItemTitle() {
        addMenuItem.title = "Add no action item ${menuItems.size}"
    }

}
