package renetik.android.controller.navigation

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import renetik.android.controller.R
import renetik.android.controller.base.CSActivity
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.navigation.CSNavigationAnimation.FadeIn
import renetik.android.controller.navigation.CSNavigationAnimation.None
import renetik.android.controller.navigation.CSNavigationAnimation.SlideInRight
import renetik.android.controller.navigation.CSNavigationAnimation.SlideOutLeft
import renetik.android.core.kotlin.collections.deleteLast
import renetik.android.core.kotlin.collections.hasKey
import renetik.android.core.kotlin.ifNull
import renetik.android.core.kotlin.isNotNull
import renetik.android.core.kotlin.onNotNull
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.logging.CSLog.logWarnTrace
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.onGlobalFocus
import renetik.android.ui.extensions.remove

class CSNavigationView : CSActivityView<FrameLayout> {
    constructor(activity: CSActivity<*>) : super(activity, layout(R.layout.cs_navigation))
    constructor(parent: CSActivityView<out ViewGroup>)
        : super(parent, layout(R.layout.cs_navigation))

    private val controllersMap = linkedMapOf<String, CSActivityView<*>>()

    val controllers get() = controllersMap.values

    // WORKAROUND CODE:
    // I had issue with EditText after focus when removed by pop,Activity.onBackPressed was never fired again
    // Like if some events was go to removed view. This somehow helps I found that when I clear focus
    // while still having edittext, problem is not there so this is ugly programmatic workaround
    // simulating manual clear focus when closing view .
    private var focusedView: View? = null

    init {
        onGlobalFocus { _, newFocus -> focusedView = newFocus }
    }

    fun <T : View> push(
        controller: CSActivityView<T>,
        pushId: String? = null
    ): CSActivityView<T> {
//        logDebug { message(controller) }
        val isFullScreen =
            (controller as? CSNavigationItem)?.isFullscreenNavigationItem?.value ?: true
        current?.showingInPager(!isFullScreen)
        controllersMap[pushId ?: controller.toString()] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun pop(controller: CSActivityView<*>) {
//        logDebug { message(controller) }
        controllersMap.remove(controller.toString()).ifNull {
            logWarnTrace { "Controller $controller not found in navigation" }
        }.elseDo { popController(controller) }
    }

    fun pop() {
        controllersMap.deleteLast().isNotNull { popController(it) }
    }

    private fun popController(controller: CSActivityView<*>) {
        focusedView?.clearFocus()
        popAnimation(controller)
        controller.showingInPager(false)
        view.remove(controller)
        current?.showingInPager(true)
        hideKeyboard()
        onViewControllerPop()
    }

    fun <T : View> pushAsLast(controller: CSActivityView<T>): CSActivityView<T> {
        controllersMap.deleteLast().isNotNull { lastController ->
            popAnimation(controller)
            view.remove(lastController)
            onViewControllerPop()
        }

        controllersMap[controller.toString()] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun <T : View> push(
        pushId: String,
        controller: CSActivityView<T>
    ): CSActivityView<T> {
        if (controllersMap.hasKey(pushId))
            for (lastEntry in controllersMap.entries.reversed()) {
                controllersMap.remove(lastEntry.key)
                view.remove(lastEntry.value)
                onViewControllerPop()
                if (lastEntry.key == pushId) break
            }
        controllersMap[pushId] = controller
        pushAnimation(controller)
        view.add(controller)
        controller.showingInPager(true)
        controller.lifecycleUpdate()
        hideKeyboard()
        onViewControllerPush()
        return controller
    }

    fun <T : View> replace(
        oldController: CSActivityView<T>,
        newController: CSActivityView<T>
    ): CSActivityView<T> {
        if (current == oldController) return pushAsLast(newController)

        val entryOfController = controllersMap.entries.find { it.value == oldController }
            ?: unexpected("oldController not found in navigation")

        oldController.showingInPager(false)
        view.remove(oldController)
        onViewControllerPop()

        controllersMap[entryOfController.key] = newController
        val indexIfController = controllersMap.entries.indexOf(entryOfController)
        view.addView(newController.view, indexIfController)
        newController.showingInPager(true)
        newController.lifecycleUpdate()
        onViewControllerPush()
        return newController
    }

    private val current get() = controllersMap.values.lastOrNull()
    private val currentItem get() = current as? CSNavigationItem
    private fun onViewControllerPush() = currentItem.onNotNull { it.onViewControllerPush(this) }
    private fun onViewControllerPop() = currentItem.onNotNull { it.onViewControllerPop(this) }

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            if (currentItem?.isNavigationBackPressedAllowed == false) return true
            pop()
            return false
        }
        return true
    }

    private fun pushAnimation(controller: CSActivityView<*>) {
        val animation = (controller as? CSNavigationItem)?.pushAnimation ?: FadeIn
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }

    private fun popAnimation(controller: CSActivityView<*>) {
        val animation = (controller as? CSNavigationItem)?.popAnimation ?: SlideOutLeft
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }

    override var navigation: CSNavigationView?
        get() = this
        set(_) = unexpected()
}