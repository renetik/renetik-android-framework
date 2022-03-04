package renetik.android.widget

import android.view.Gravity
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.IntDef
import androidx.core.view.GravityCompat.END
import androidx.core.view.GravityCompat.START
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.*
import renetik.android.framework.view.adapter.CSDrawerAdapter
import renetik.android.view.view

@IntDef(value = [Gravity.LEFT, Gravity.RIGHT, START, END], flag = true)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class EdgeGravity

private fun DrawerLayout.toggleDrawer(@EdgeGravity gravity: Int) =
    if (isDrawerOpen(gravity)) closeDrawer(gravity) else openDrawer(gravity)

fun DrawerLayout.toggleLeft() = toggleDrawer(START)

fun DrawerLayout.lockRight() {
    setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, END)
}

fun DrawerLayout.unlockRight() {
    setDrawerLockMode(LOCK_MODE_UNLOCKED, END)
}

fun DrawerLayout.closeLeft() {
    if (isDrawerOpen(START)) closeDrawer(START)
}

fun DrawerLayout.openLeft() = openDrawer(START)

fun DrawerLayout.toggleRight() = toggleDrawer(END)

fun DrawerLayout.closeRight() {
    if (isDrawerOpen(END)) closeDrawer(END)
}

fun DrawerLayout.close() {
    closeRight()
    closeLeft()
}

fun DrawerLayout.openRight() = openDrawer(END)

val DrawerLayout.isDrawerOpen get() = isDrawerOpen(START) || isDrawerOpen(END)

fun DrawerLayout.setupLeftPanelSliding(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            view(contentId).translationX = drawerView.width * slideOffset
    }))
}

fun DrawerLayout.setupRightPanelSliding(@IdRes viewId: Int, @IdRes contentId: Int) {
    addDrawerListener(CSDrawerAdapter(onDrawerSlide = { drawerView, slideOffset ->
        if (drawerView.id == viewId)
            view(contentId).translationX = drawerView.width * -slideOffset
    }))
}

fun DrawerLayout.onDrawerStateChanged(function: (newState: Int) -> Unit) {
    addDrawerListener(object : SimpleDrawerListener() {
        override fun onDrawerStateChanged(newState: Int) = function(newState)
    })
}

fun DrawerLayout.onOpenStateChanged(function: (DrawerLayout) -> Unit) {
    var state = isDrawerOpen
    onDrawerStateChanged {
        if (it == STATE_IDLE && state != isDrawerOpen) {
            function(this)
            state = isDrawerOpen
        }
    }
}

fun DrawerLayout.onDrawerOpening(function: (DrawerLayout) -> Unit) {
    val drawerLayout = this
    var onDrawerOpeningCalled = false
    addDrawerListener(object : SimpleDrawerListener() {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            super.onDrawerSlide(drawerView, slideOffset)
            if (!onDrawerOpeningCalled && slideOffset > 0f && !isDrawerOpen) {
                function(drawerLayout)
                onDrawerOpeningCalled = true
            }
        }

        override fun onDrawerStateChanged(newState: Int) {
            if (newState == STATE_IDLE) onDrawerOpeningCalled = false
        }
    })
}