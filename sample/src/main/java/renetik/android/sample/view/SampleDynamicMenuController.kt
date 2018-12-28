package renetik.android.sample.view

import android.view.View
import renetik.android.base.layout
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSMenuItem
import renetik.android.dialog.extensions.dialog
import renetik.android.extensions.compoundButton
import renetik.android.extensions.title
import renetik.android.sample.R

class SampleDynamicMenuController(title: String)
    : CSViewController<View>(navigation, layout(R.layout.sample_dynamic_menu)) {

    private val addMenuItem: CSMenuItem = menu("Add menu item").onClick { menuItem ->
        dialog(menuItem.title!!).showInput { dialog ->
            menu(dialog.inputValue()).onClick {
                dialog("Remove menu item ?").show {
                    menuItem.remove()
                    updateAddMenuItemTitle()
                }
            }.neverAsAction()
            updateAddMenuItemTitle()
        }
    }.alwaysAsAction()

    init {
        title(R.id.SampleDynamicMenu_Title, title)
        compoundButton(R.id.SampleDynamicMenu_AddMenuItemVisible)
                .setOnCheckedChangeListener { _, isChecked -> addMenuItem.visible(isChecked) }
    }

    private fun updateAddMenuItemTitle() {
        addMenuItem.title = "Add menu item ${menuItems.size + 1}"
    }

}