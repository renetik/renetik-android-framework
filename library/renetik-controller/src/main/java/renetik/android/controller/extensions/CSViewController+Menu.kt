package renetik.android.controller.extensions

import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSMenuItem

fun <T : CSViewController<*>> T.menuItem(@IdRes id: Int) = addMenuItem(CSMenuItem(this, id))

fun <T : CSViewController<*>> T.menuItem(actionView: View) = menuItem("").apply {
    this.actionView = actionView
    alwaysAsAction()
}

fun <T : CSViewController<*>> T.menuItem(title: String) = addMenuItem(CSMenuItem(this, title))

fun <T : CSViewController<*>> T.menuItem(title: String,
                                         @DrawableRes iconResource: Int,
                                         onClick: ((CSMenuItem) -> Unit)? = null) =
    addMenuItem(CSMenuItem(this, title)).setIconResourceId(iconResource)
        .apply { onClick?.let { onClick(onClick) } }

fun <T : CSViewController<*>> T.menuItem(title: String, onClick: (CSMenuItem) -> Unit) =
    menuItem(title).onClick(onClick)

fun <T : CSViewController<*>> T.menuItem(@DrawableRes iconResource: Int,
                                         onClick: (CSMenuItem) -> Unit) =
    menuItem("", iconResource).onClick(onClick)