package renetik.android.sample.view.dynamicmenu

import android.view.View
import renetik.android.base.CSLayoutRes.Companion.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.common.CSNavigationItem
import renetik.android.controller.extensions.checkBox
import renetik.android.controller.extensions.dialog
import renetik.android.controller.extensions.menuItem
import renetik.android.controller.extensions.textView
import renetik.android.controller.menu.CSMenuItem
import renetik.android.sample.R
import renetik.android.sample.view.navigation
import renetik.android.view.extensions.onChecked
import renetik.android.view.extensions.text

class SampleDynamicMenuController(title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_dynamic_menu)),
    CSNavigationItem {

    private val addMenuItem: CSMenuItem = menuItem("").alwaysAsAction()

    init {
        textView(R.id.SampleDynamicMenu_Title).text(title)
        checkBox(R.id.SampleDynamicMenu_AddMenuItemVisible)
            .onChecked { addMenuItem.visible(it.isChecked) }.isChecked = addMenuItem.isVisible
        updateAddMenuItemTitle()
        addMenuItem.onClick {
            dialog(addMenuItem.title!!)
                .showInput("Enter menu item name", "Sub Menu item ${menuItems.size}") { dialog ->
                    menuItem(dialog.inputText).onClick { addedMenuItem ->
                        dialog("Remove menu item '${addedMenuItem.title}'?")
                            .withIcon(R.drawable.om_black_196).show {
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

    override val navigationItemTitle = ""
}
