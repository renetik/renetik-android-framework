package renetik.android.controller.common

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import renetik.android.base.layout
import renetik.android.controller.R
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSViewController
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.extensions.applicationLabel
import renetik.android.java.extensions.collections.*
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.isSet
import renetik.android.java.extensions.notNull
import renetik.android.view.extensions.add
import renetik.android.view.extensions.remove

lateinit var navigation: CSNavigationController

open class CSNavigationController : CSViewController<FrameLayout>, CSNavigationItem {

    constructor(activity: CSActivity) : super(activity, layout(R.layout.cs_navigation))

    constructor(parent: CSViewController<out ViewGroup>) : super(
        parent,
        layout(R.layout.cs_navigation)
    )

    init {
        navigation = this
    }

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
        onViewControllerPush(controller)
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
            onViewControllerPop(lastController)
        }
    }

    fun <T : View> pushAsLast(controller: CSViewController<T>): CSViewController<T> {
        controllers.deleteLast().notNull { lastController ->
            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_fade_out))
            lastController.showingInContainer(false)
            view.remove(lastController)
            lastController.deInitialize()
            onViewControllerPop(lastController)
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
        onViewControllerPush(controller)
        return controller
    }

    fun <T : View> replace(
        oldController: CSViewController<T>,
        newController: CSViewController<T>
    ): CSViewController<T> {
        if (controllers.last == oldController) return pushAsLast(newController)

        val indexOfController = controllers.indexOf(oldController)
        if (indexOfController == -1) throw exception("oldController not found in navigation")

        controllers.delete(oldController).let { lastController ->
            //            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_fade_out))
            lastController.showingInContainer(false)
            view.remove(lastController)
            lastController.deInitialize()
            onViewControllerPop(lastController)
        }
        controllers.put(newController, indexOfController)
//        controller.view.startAnimation(loadAnimation(this, R.anim.abc_fade_in))
        view.addView(newController.view, indexOfController)
        newController.showingInContainer(false)
        newController.initialize()
//        updateBackButton()
//        updateBarTitle()
//        updateBarIcon()
//        invalidateOptionsMenu()
//        hideKeyboard()
        onViewControllerPush(newController)
        return newController
    }

    private fun updateBarTitle() {
        (controllers.last as? CSNavigationItem)?.let { item ->
            item.isNavigationTitleVisible?.let { isVisible ->
                if (!isVisible) {
                    setActionBarTitle(null)
                    return
                } else item.navigationItemTitle?.let { title ->
                    setActionBarTitle(title)
                    return
                }
            }
        }
        isNavigationTitleVisible?.let { isVisible ->
            if (!isVisible) {
                setActionBarTitle(null)
            } else navigationItemTitle?.let { title ->
                setActionBarTitle(title)
            }
        }
    }

    private fun updateBarIcon() {
        (controllers.last as? CSNavigationItem)?.let { item ->
            item.isNavigationIconVisible?.let { isVisible ->
                if (!isVisible) {
                    setActionBarIcon(null)
                    return
                } else item.navigationItemIcon?.let { icon ->
                    setActionBarIcon(icon)
                    return
                }
            }
        }
        isNavigationIconVisible?.let { isVisible ->
            if (!isVisible) {
                setActionBarIcon(null)
            } else navigationItemIcon?.let { icon ->
                setActionBarIcon(icon)
            }
        }
    }

    private fun setActionBarTitle(title: String?) {
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

    open fun onViewControllerPush(controller: CSViewController<*>) {
    }

    open fun onViewControllerPop(controller: CSViewController<*>) {

    }
}

interface CSNavigationItem {
    val isNavigationIconVisible: Boolean? get() = null
    val navigationItemIcon: Int? get() = null
    val isNavigationTitleVisible: Boolean? get() = null
    val navigationItemTitle: String? get() = null
    val isNavigationItemBackButton get() = true
}
