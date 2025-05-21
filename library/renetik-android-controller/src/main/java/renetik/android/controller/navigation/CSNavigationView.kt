package renetik.android.controller.navigation

import android.view.View
import android.view.animation.AnimationUtils.loadAnimation
import android.widget.FrameLayout
import renetik.android.controller.base.CSActivityView
import renetik.android.controller.navigation.CSNavigationAnimation.None
import renetik.android.controller.navigation.CSNavigationAnimation.SlideOutLeft
import renetik.android.core.kotlin.collections.deleteLast
import renetik.android.core.kotlin.collections.hasKey
import renetik.android.core.kotlin.then
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes.Companion.layout
import renetik.android.core.logging.CSLog.logWarnTrace
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.onGlobalFocus
import renetik.android.ui.extensions.remove

class CSNavigationView(
    parent: CSActivityView<out FrameLayout>
) : CSActivityView<FrameLayout>(
    parent, layout(renetik.android.ui.R.layout.cs_frame_match)
) {

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
    ): CSActivityView<T> {
//        logDebug { message(controller) }
        val isFullScreen = (controller as? CSNavigationItemView)?.isFullScreen ?: true
        current?.showingInPager(!isFullScreen)
        controllersMap[controller.toString()] = controller
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
        controllersMap.remove(controller.toString())?.run { popController(controller) }
            ?: run { logWarnTrace { "Controller $controller not found in navigation" } }
    }

    fun pop() {
        controllersMap.deleteLast()?.also { popController(it) }
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
        controllersMap.deleteLast()?.also { lastController ->
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
    private val currentItem get() = current as? CSNavigationItemView
    private fun onViewControllerPush() = then { currentItem?.onViewControllerPush(this) }
    private fun onViewControllerPop() = then { currentItem?.onViewControllerPop(this) }

    override fun onGoBack(): Boolean {
        if (controllers.size > 1) {
            if (currentItem?.isBackNavigationAllowed == false) return true
            pop()
            return false
        }
        return true
    }

    private fun pushAnimation(controller: CSActivityView<*>) {
        val animation = (controller as? CSNavigationItemView)?.pushAnimation ?: None
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }

    private fun popAnimation(controller: CSActivityView<*>) {
        val animation =
            (controller as? CSNavigationItemView)?.popAnimation ?: SlideOutLeft
        if (animation != None)
            controller.view.startAnimation(loadAnimation(this, animation.resource))
    }

    override var navigation: CSNavigationView?
        get() = this
        set(_) = unexpected()
}