package renetik.android.controller.common

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import renetik.android.controller.R
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.common.CSNavigationInstance.navigation
import renetik.android.controller.extensions.add
import renetik.android.controller.extensions.remove
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.java.extensions.collections.*
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.notNull
import renetik.android.primitives.isFalse
import renetik.android.primitives.isSet

object CSNavigationInstance {
    lateinit var navigation: CSNavigationView
    val isInitialized get() = ::navigation.isInitialized
}

const val PushID = "push_key"

@Suppress("LeakingThis")
@SuppressLint("UseCompatLoadingForDrawables")
open class CSNavigationView : CSActivityView<FrameLayout>, CSNavigationItem {

    constructor(activity: CSActivity) : super(activity, layout(R.layout.cs_navigation))

    constructor(parent: CSActivityView<out ViewGroup>) :
            super(parent, layout(R.layout.cs_navigation))

    private val actionBar get() = activity().supportActionBar

    init {
        if (CSNavigationInstance.isInitialized) navigation.activity?.finish()
        navigation = this
    }

    open var controllers = list<CSActivityView<*>>()

    fun <T : View> push(
        controller: CSActivityView<T>,
        pushId: String? = null
    ): CSActivityView<T> {
        currentController?.showingInPager(false)
        pushId?.let { controller.setValue(PushID, it) }
        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_slide_in_top))
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush(controller)
        return controller
    }

    fun pop() {
        controllers.deleteLast().notNull { lastController ->
            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_slide_out_top))
            lastController.showingInPager(false)
            view.remove(lastController)
            currentController?.showingInPager(true)
            updateBar()
            hideKeyboard()
            onViewControllerPop(lastController)
        }
    }

    fun <T : View> pushAsLast(controller: CSActivityView<T>): CSActivityView<T> {
        controllers.deleteLast().notNull { lastController ->
            lastController.view.startAnimation(loadAnimation(this, R.anim.abc_fade_out))
            view.remove(lastController)
            onViewControllerPop(lastController)
        }

        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_fade_in))
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush(controller)
        return controller
    }

    fun <T : View> push(
        pushId: String,
        controller: CSActivityView<T>
    ): CSActivityView<T> {
        if (controllers.any { it.getValue(PushID) == pushId })
            for (lastController in controllers.reversed()) {
                controllers.delete(lastController)
                view.remove(lastController)
                onViewControllerPop(lastController)
                if (lastController.getValue(PushID) == pushId) break
            }
        controller.setValue(PushID, pushId)
        controllers.put(controller)
        controller.view.startAnimation(loadAnimation(this, R.anim.abc_fade_in))
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush(controller)
        return controller
    }

    fun <T : View> replace(
        oldController: CSActivityView<T>,
        newController: CSActivityView<T>
    ): CSActivityView<T> {
        if (currentController == oldController) return pushAsLast(newController)

        val indexOfController = controllers.indexOf(oldController)
        if (indexOfController == -1) throw exception("oldController not found in navigation")

        controllers.delete(oldController).let { lastController ->
            lastController.showingInPager(false)
            view.remove(lastController)
            onViewControllerPop(lastController)
        }
        controllers.put(newController, indexOfController)
        view.addView(newController.view, indexOfController)
        newController.showingInPager(false)
        newController.lifecycleUpdate()
        onViewControllerPush(newController)
        return newController
    }

    private fun updateBarTitle() {
        (currentController as? CSNavigationItem)?.let { item ->
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
            if (!isVisible) setActionBarTitle(null)
            else navigationItemTitle?.let { title ->
                setActionBarTitle(title)
            }
        }
    }

    private fun updateBarIcon() {
        (currentController as? CSNavigationItem)?.let { item ->
            item.isNavigationIconVisible?.let { isVisible ->
                if (!isVisible) {
                    hideActionBarIcon()
                    return
                } else item.navigationItemIcon?.let { icon ->
                    setActionBarIcon(icon)
                    return
                }
            }
        }
        isNavigationIconVisible?.let { isVisible ->
            if (!isVisible) hideActionBarIcon()
            else navigationItemIcon?.let { icon ->
                setActionBarIcon(icon)
            }
        }
    }

    private fun setActionBarTitle(title: String?) {
        actionBar?.setDisplayShowTitleEnabled(title.isSet)
        actionBar?.title = title
    }

    open fun setActionBarIcon(icon: Drawable) {
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(icon)
    }

    private fun setActionBarIcon(@DrawableRes icon: Int) {
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(icon)
    }

    open fun hideActionBarIcon() {
        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setIcon(null)
    }

    private fun updateBarBackButton() {
        val isBackButtonVisible = currentNavigationItem.isNavigationBackButtonVisible
        if (controllers.size > 1 && isBackButtonVisible) {
            actionBar?.setDisplayHomeAsUpEnabled(true)
            updateBackButtonIcon()
        } else actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun updateBackButtonIcon() {
        val navigationBackButtonIcon = currentNavigationItem.navigationBackButtonIcon
        val navigationBackButtonIconTint = currentNavigationItem.navigationBackButtonIconTint
        navigationBackButtonIcon?.let {
            val drawable = getDrawable(it)!!
            navigationBackButtonIconTint?.let { drawable.setTint(it) }
            actionBar?.setHomeAsUpIndicator(drawable)
        } ?: actionBar?.setHomeAsUpIndicator(null)
    }

    val currentController get() = controllers.last

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            pop()
            return false
        }
        return true
    }

//    override fun onOptionsItemSelected(onItem: CSOnMenuItem) {
//        super.onOptionsItemSelected(onItem)
//        if (onItem.consume(android.R.id.home)) goBack()
//    }

    open fun onViewControllerPush(controller: CSActivityView<*>) {
    }

    open fun onViewControllerPop(controller: CSActivityView<*>) {

    }

    fun setSupportActionBar(toolbar: Toolbar) {
        activity().setSupportActionBar(toolbar)
        updateBar()
    }

    private fun updateBar() {
        val isBarVisible = currentNavigationItem.isBarVisible
        if (isBarVisible.isFalse) actionBar?.hide() else actionBar?.show()
        updateBarBackButton()
        updateBarTitle()
        updateBarIcon()
    }

    val currentNavigationItem get() = (currentController as? CSNavigationItem) ?: this
}

interface CSNavigationItem {
    val isBarVisible: Boolean? get() = null
    val isNavigationIconVisible: Boolean? get() = null
    val navigationItemIcon: Int? get() = null
    val isNavigationTitleVisible: Boolean? get() = null
    val navigationItemTitle: String? get() = null
    val isNavigationBackButtonVisible get() = true
    val navigationBackButtonIcon: Int? get() = null
    val navigationBackButtonIconTint: Int? get() = null
}
