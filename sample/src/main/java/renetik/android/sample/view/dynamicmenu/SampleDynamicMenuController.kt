package renetik.android.sample.view.dynamicmenu

import android.view.View
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSMenuItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.checkBox
import renetik.android.extensions.textView
import renetik.android.sample.R
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.onChecked
import renetik.android.view.extensions.title

class SampleDynamicMenuController(title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_dynamic_menu)) {

    private val addMenuItem: CSMenuItem = menuItem("").alwaysAsAction()

    init {
        textView(R.id.SampleDynamicMenu_Title).title(title)
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
