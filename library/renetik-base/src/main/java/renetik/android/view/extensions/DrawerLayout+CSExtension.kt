package renetik.android.view.extensions

import android.view.Gravity
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import renetik.android.view.adapter.CSDrawerAdapter

@IntDef(value = [Gravity.LEFT, Gravity.RIGHT, GravityCompat.START, GravityCompat.END], flag = true)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class EdgeGravity

private fun DrawerLayout.toggleDrawer(@EdgeGravity gravity: Int) =
    if (isDrawerOpen(gravity)) closeDrawer(gravity) else openDrawer(gravity)

fun DrawerLayout.toggleDrawerLeft() = toggleDrawer(GravityCompat.START)

fun DrawerLayout.closeDrawerLeft() = closeDrawer((GravityCompat.START))

fun DrawerLayout.toggleDrawerRight() = toggleDrawer(GravityCompat.END)

fun DrawerLayout.closeDrawerRight() = closeDrawer((GravityCompat.END))


fun DrawerLayout.slideLeftDrawer(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            simpleView(contentId).translationX = drawerView.width * slideOffset
    }))
}

fun DrawerLayout.slideRightDrawer(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            simpleView(contentId).translationX = drawerView.width * -slideOffset
    }))
}