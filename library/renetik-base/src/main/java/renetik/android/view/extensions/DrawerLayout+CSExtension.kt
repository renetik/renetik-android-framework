package renetik.android.view.extensions

import android.view.Gravity
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.core.view.GravityCompat.END
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import renetik.android.view.adapter.CSDrawerAdapter

@IntDef(value = [Gravity.LEFT, Gravity.RIGHT, START, END], flag = true)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class EdgeGravity

private fun DrawerLayout.toggleDrawer(@EdgeGravity gravity: Int) =
    if (isDrawerOpen(gravity)) closeDrawer(gravity) else openDrawer(gravity)

fun DrawerLayout.toggleLeftPanel() = toggleDrawer(START)

fun DrawerLayout.closeLeftPanel() = closeDrawer((START))

fun DrawerLayout.openLeftPanel() = openDrawer((START))

fun DrawerLayout.toggleRightPanel() = toggleDrawer(END)

fun DrawerLayout.closeRightPanel() = closeDrawer((END))

fun DrawerLayout.openRightPanel() = openDrawer((END))

val DrawerLayout.isLeftPanelOpen get() = isDrawerOpen(START)

val DrawerLayout.isRightPanelOpen get() = isDrawerOpen(END)

fun DrawerLayout.setupLeftPanelSliding(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            simpleView(contentId).translationX = drawerView.width * slideOffset
    }))
}

fun DrawerLayout.setupRightPanelSliding(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            simpleView(contentId).translationX = drawerView.width * -slideOffset
    }))
}

fun DrawerLayout.onDrawerStateChanged(function: (DrawerLayout) -> Unit) {
    val drawerLayout = this
    addDrawerListener(object : SimpleDrawerListener() {
        override fun onDrawerStateChanged(newState: Int) = function(drawerLayout)
    })
}