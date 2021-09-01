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
import renetik.android.controller.extensions.add
import renetik.android.controller.extensions.remove
import renetik.android.framework.lang.CSLayoutRes.Companion.layout
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.hasKey
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.notNull
import renetik.android.primitives.isFalse
import renetik.android.primitives.isSet

object CSNavigationInstance {
    val navigation get() = CSNavigationView.navigation
    val isInitialized get() = CSNavigationView.isInitialized
}

@Suppress("LeakingThis")
open class CSNavigationView : CSActivityView<FrameLayout>, CSNavigationItem {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var navigation: CSNavigationView
        val isInitialized get() = Companion::navigation.isInitialized
    }

    constructor(activity: CSActivity) : super(activity, layout(R.layout.cs_navigation))

    constructor(parent: CSActivityView<out ViewGroup>) :
            super(parent, layout(R.layout.cs_navigation))

    private val actionBar get() = activity().supportActionBar


    init {
        if (CSNavigationInstance.isInitialized) navigation.activity?.finish()
        navigation = this
    }

    private val _controllers = linkedMapOf<String, CSActivityView<*>>()
    val controllers get() = _controllers.values

    fun <T : View> push(
        controller: CSActivityView<T>,
        pushId: String? = null
    ): CSActivityView<T> {
        current?.showingInPager(false)
//        pushId?.let { controller.setValue(PushID, it) }
        _controllers[pushId ?: controller.toString()] = controller
        pushAnimation(this, controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        updateBar()
        hideKeyboard()
        onViewControllerPush(controller)
        return controller
    }

    fun pop(controller: CSActivityView<*>) {
        _controllers.remove(controller.toString()).notNull { popController(controller) }
    }

    fun pop() {
        _controllers.deleteLast().notNull { popController(it) }
    }

    private fun popController(controller: CSActivityView<*>) {
        popAnimation(this, controller)
        controller.showingInPager(false)
        view.remove(controller)
        current?.showingInPager(true)
        updateBar()
        hideKeyboard()
        onViewControllerPop(controller)
    }

    fun <T : View> pushAsLast(controller: CSActivityView<T>): CSActivityView<T> {
        _controllers.deleteLast().notNull { lastController ->
            popAnimation(this, controller)
            view.remove(lastController)
            onViewControllerPop(lastController)
        }

        _controllers[controller.toString()] = controller
        pushAnimation(this, controller)
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
        if (_controllers.hasKey(pushId))
            for (lastEntry in _controllers.entries.reversed()) {
                _controllers.remove(lastEntry.key)
                view.remove(lastEntry.value)
                onViewControllerPop(lastEntry.value)
                if (lastEntry.key == pushId) break
            }
        _controllers[pushId] = controller
        pushAnimation(this, controller)
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
        if (current == oldController) return pushAsLast(newController)

        val entryOfController = _controllers.entries.find { it.value == oldController }
            ?: throw exception("oldController not found in navigation")

        oldController.showingInPager(false)
        view.remove(oldController)
        onViewControllerPop(oldController)

        _controllers[entryOfController.key] = newController
        val indexIfController = _controllers.entries.indexOf(entryOfController)
        view.addView(newController.view, indexIfController)
        newController.showingInPager(false)
        newController.lifecycleUpdate()
        onViewControllerPush(newController)
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

    private val current get() = _controllers.values.lastOrNull()

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            pop()
            return false
        }
        return true
    }

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

    private val currentNavigationItem get() = (current as? CSNavigationItem) ?: this
}


private fun pushAnimation(navigation: CSNavigationView, controller: CSActivityView<*>) {
    val animation = ((controller as? CSNavigationItem)?.pushAnimation ?: navigation.pushAnimation)
    if (animation != CSNavigationAnimation.None)
        controller.view.startAnimation(loadAnimation(navigation, animation.resource))
}

private fun popAnimation(navigation: CSNavigationView, controller: CSActivityView<*>) {
    val animation = ((controller as? CSNavigationItem)?.popAnimation ?: navigation.popAnimation)
    if (animation != CSNavigationAnimation.None)
        controller.view.startAnimation(loadAnimation(navigation, animation.resource))
}
