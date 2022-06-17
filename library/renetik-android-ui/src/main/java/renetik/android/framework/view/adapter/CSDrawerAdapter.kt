package renetik.android.framework.view.adapter

import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

open class CSDrawerAdapter(
    private val onDrawerSlide: ((drawerView: View, slideOffset: Float) -> Unit)? = null,
    private val onDrawerClosed: ((drawerView: View) -> Unit)? = null,
    private val onDrawerOpened: ((drawerView: View) -> Unit)? = null,
    private val onDrawerStateChanged: ((newState: Int) -> Unit)? = null
) : DrawerLayout.DrawerListener {
    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        onDrawerSlide?.invoke(drawerView, slideOffset)
    }

    override fun onDrawerClosed(drawerView: View) {
        onDrawerClosed?.invoke(drawerView)
    }

    override fun onDrawerOpened(drawerView: View) {
        onDrawerOpened?.invoke(drawerView)
    }

    override fun onDrawerStateChanged(newState: Int) {
        onDrawerStateChanged?.invoke(newState)
    }
}