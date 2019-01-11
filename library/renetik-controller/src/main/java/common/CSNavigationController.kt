package renetik.android.controller.common

import android.graphics.drawable.Drawable
import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.extensions.applicationIcon
import renetik.android.extensions.applicationLabel
import renetik.android.extensions.applicationLogo
import renetik.android.java.collections.list
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.hasItems
import renetik.android.java.extensions.collections.last
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

open class CSNavigationController(activity: CSActivity)
    : CSViewController<FrameLayout>(activity, layout(R.layout.cs_navigation)), CSNavigationItem {

    open var controllers = list<CSViewController<*>>()

    fun <T : View> push(controller: CSViewController<T>): CSViewController<T> {
        if (controllers.hasItems) controllers.last?.showingInContainer(false)
        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_slide_in_top))
        view.add(controller)
        controller.showingInContainer(true)
        controller.initialize()
        updateBackButton()
        updateBarTitle()
        updateBarIcon()
        invalidateOptionsMenu()
        hideKeyboard()
        return controller
    }

    fun pop() {
        controllers.deleteLast().notNull { lastController ->
            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_slide_out_top))
            lastController.showingInContainer(false)
            view.remove(lastController)
            lastController.deInitialize()

            controllers.last?.showingInContainer(true)
            updateBackButton()
            updateBarTitle()
            updateBarIcon()
            hideKeyboard()
        }
    }

    fun <T : View> pushAsLast(controller: CSViewController<T>): CSViewController<T> {
        controllers.deleteLast().notNull { lastController ->
            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_fade_out))
            lastController.showingInContainer(false)
            view.remove(lastController)
            lastController.deInitialize()
        }

        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_fade_in))
        view.add(controller)
        controller.showingInContainer(true)
        controller.initialize()
        updateBackButton()
        updateBarTitle()
        updateBarIcon()
        invalidateOptionsMenu()
        hideKeyboard()
        return controller
    }

    private fun updateBarTitle() {
        (controllers.last as? CSNavigationItem)?.navigationItemTitle?.let { lastControllerItemTitle ->
            setActionBarTitle(lastControllerItemTitle)
        } ?: let {
            navigationItemTitle?.let { navigationControllerItemTitle ->
                setActionBarTitle(navigationControllerItemTitle)
            } ?: setActionBarTitle(applicationLabel)
        }
    }

    private fun updateBarIcon() {
        (controllers.last as? CSNavigationItem)?.navigationItemIcon?.let { lastControllerItemIcon ->
            setActionBarIcon(lastControllerItemIcon)
        } ?: let {
            navigationItemIcon?.let { icon -> setActionBarIcon(icon) }
                    ?: setActionBarIcon(applicationLogo ?: applicationIcon)
        }
    }

    private fun setActionBarTitle(title: String) {
        actionBar?.setDisplayShowTitleEnabled(title.isSet)
        actionBar?.title = title
    }

    private fun setActionBarIcon(icon: Drawable?) {
        actionBar?.setDisplayShowHomeEnabled(icon.isSet)
        actionBar?.setIcon(icon)
    }

    private fun setActionBarIcon(icon: Int) {
        actionBar?.setDisplayShowHomeEnabled(icon.isSet)
        actionBar?.setIcon(icon)
    }

    private fun updateBackButton() {
        val isBackButtonVisible =
                (controllers.last as? CSNavigationItem)?.isNavigationItemBackButton
                        ?: isNavigationItemBackButton
        if (controllers.size > 1 && isBackButtonVisible) showBackButton()
        else hideBackButton()
    }

    open fun showBackButton() {
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    open fun hideBackButton() {
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            pop()
            return false
        }
        return true
    }

    override fun onOptionsItemSelected(onItem: CSOnMenuItem) {
        super.onOptionsItemSelected(onItem)
        if (onItem.consume(android.R.id.home)) goBack()
    }
}

interface CSNavigationItem {
    val isNavigationItemBackButton get() = true
    val navigationItemIcon: Int? get() = null
    val navigationItemTitle: String? get() = null
}
