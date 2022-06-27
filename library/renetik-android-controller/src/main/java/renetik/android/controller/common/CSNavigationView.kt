package renetik.android.controller.common

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import renetik.android.core.extensions.content.color
import renetik.android.controller.R
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.base.onGlobalFocus
import renetik.android.controller.common.CSNavigationAnimation.None
import renetik.android.controller.extensions.add
import renetik.android.controller.extensions.remove
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.core.kotlin.primitives.isSet
import renetik.android.ui.extensions.view.background
import renetik.android.ui.extensions.view.removeFromSuperview
import renetik.android.core.kotlin.collections.deleteLast
import renetik.android.core.kotlin.collections.hasKey
import renetik.android.core.kotlin.notNull
import renetik.android.core.kotlin.unexpected

class CSNavigationView : CSActivityView<FrameLayout>, CSNavigationItem {

    constructor(activity: CSActivity) : super(activity, layout(R.layout.cs_navigation))

    constructor(parent: CSActivityView<out ViewGroup>) :
            super(parent, layout(R.layout.cs_navigation))

    private val actionBar get() = activity().supportActionBar

    private val _controllers = linkedMapOf<String, CSActivityView<*>>()
    val controllers get() = _controllers.values

    private val backgroundView =
        View(this).also { it.background(color(renetik.android.ui.R.color.cs_dialog_background)) }

    // WORKAROUND CODE:
    // I had issue with EditText after focus when removed by pop,Activity.onBackPressed was never fired again
    // Like if some events was go to removed view. This somehow helps I found that when I clear focus
    // while still having edittext, problem is not there so this is ugly programmatic workaround
    // simulating manual clear focus when closing view .
    private var focusedView: View? = null

    init {
        onGlobalFocus { _, newFocus -> focusedView = newFocus }
    }

    fun <T : View> push(controller: CSActivityView<T>,
                        pushId: String? = null): CSActivityView<T> {
        logDebug { controller }
        val isFullScreen =
            (controller as? CSNavigationItem)?.isFullscreenNavigationItem?.value ?: true
        current?.showingInPager(!isFullScreen)
        _controllers[pushId ?: controller.toString()] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun pop(controller: CSActivityView<*>) {
        logDebug { controller }
        _controllers.remove(controller.toString()).notNull { popController(controller) }
    }

    fun pop() {
        _controllers.deleteLast().notNull { popController(it) }
    }

    private fun popController(controller: CSActivityView<*>) {
        backgroundView.removeFromSuperview()
        focusedView?.clearFocus()
        popAnimation(controller)
        controller.showingInPager(false)
        view.remove(controller)
        current?.showingInPager(true)
        updateBar()
        hideKeyboard()
        onViewControllerPop()
    }

    fun <T : View> pushAsLast(controller: CSActivityView<T>): CSActivityView<T> {
        _controllers.deleteLast().notNull { lastController ->
            popAnimation(controller)
            view.remove(lastController)
            onViewControllerPop()
        }

        _controllers[controller.toString()] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun <T : View> push(
        pushId: String,
        controller: CSActivityView<T>
    ): CSActivityView<T> {
        if (_controllers.hasKey(pushId))
            for (lastEntry in _controllers.entries.reversed()) {
                _controllers.remove(lastEntry.key)
                view.remove(lastEntry.value)
                onViewControllerPop()
                if (lastEntry.key == pushId) break
            }
        _controllers[pushId] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun <T : View> replace(
        oldController: CSActivityView<T>,
        newController: CSActivityView<T>
    ): CSActivityView<T> {
        if (current == oldController) return pushAsLast(newController)

        val entryOfController = _controllers.entries.find { it.value == oldController }
            ?: unexpected("oldController not found in navigation")

        oldController.showingInPager(false)
        view.remove(oldController)
        onViewControllerPop()

        _controllers[entryOfController.key] = newController
        val indexIfController = _controllers.entries.indexOf(entryOfController)
        view.addView(newController.view, indexIfController)
        newController.showingInPager(true)
        newController.lifecycleUpdate()
        onViewControllerPush()
        return newController
    }

    private fun updateBarTitle() {
        (current as? CSNavigationItem)?.let { item ->
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
        (current as? CSNavigationItem)?.let { item ->
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

    fun setActionBarIcon(icon: Drawable) {
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(icon)
    }

    private fun setActionBarIcon(@DrawableRes icon: Int) {
        actionBar?.setDisplayShowHomeEnabled(true)
        actionBar?.setIcon(icon)
    }

    fun hideActionBarIcon() {
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

    private val current get() = _controllers.values.lastOrNull()

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            if ((controllers.last() as? CSNavigationItem)
                    ?.isNavigationBackPressedAllowed == false) return true
            pop()
            return false
        }
        return true
    }

    private fun onViewControllerPush() {
        (current as? CSNavigationItem)?.onViewControllerPush(this)
    }

    fun onViewControllerPop() {
        (current as? CSNavigationItem)?.onViewControllerPop(this)
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

    private val currentNavigationItem get() = (current as? CSNavigationItem) ?: this

    private fun pushAnimation(controller: CSActivityView<*>) {
        val animation = ((controller as? CSNavigationItem)?.pushAnimation ?: pushAnimation)
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }

    private fun popAnimation(controller: CSActivityView<*>) {
        val animation = ((controller as? CSNavigationItem)?.popAnimation ?: popAnimation)
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }
}