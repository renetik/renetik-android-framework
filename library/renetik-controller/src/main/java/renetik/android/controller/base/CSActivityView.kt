package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.content.input
import renetik.android.controller.common.CSNavigationView
import renetik.android.framework.event.*
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSProperty
import renetik.android.framework.logging.CSLog.warn
import renetik.android.view.extensions.isShowing
import renetik.kotlin.className
import renetik.kotlin.exception

abstract class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, LifecycleOwner, CSEventOwner, CSVisibleEventOwner {

    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onBack = event<CSProperty<Boolean>>()
    override fun activity(): CSActivity = activity!!
    private var isResumed = false
    private var isResumeFirstTime = false
    private val isPaused get() = !isResumed
    private var parentActivityView: CSActivityView<*>? = null
    var activity: CSActivity? = null
    private var showingInPager: Boolean? = null

    constructor(parent: CSActivityView<*>) : super(parent) {
        parentActivityView = parent
        initializeParent(parent)
    }

    constructor(activity: CSActivity, layout: CSLayoutRes) : super(activity, layout) {
        this.activity = activity
        initializeParent(activity)
    }

    constructor(parent: CSActivityView<*>, @IdRes viewId: Int) : super(parent, viewId) {
        parentActivityView = parent
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, layout: CSLayoutRes) : super(parent, layout) {
        parentActivityView = parent
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, group: ViewGroup, layout: CSLayoutRes)
            : super(parent, group, layout) {
        parentActivityView = parent
        initializeParent(parent)
    }

    protected open fun onResume() {
        if (isResumed) return
        isResumed = true
        if (!isResumeFirstTime) {
            onResumeFirstTime()
            isResumeFirstTime = true
        } else onResumeRestore()
        updateVisibility()
        onResume.fire()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeRestore() {}

    open fun onPause() {
        if (isPaused && isVisible) {
            warn(Throwable(), "Not Resumed while paused, should be resumed first", this)
            return
        }
        isResumed = false
        updateVisibility()
        onPause.fire()
    }

    override fun onDestroy() {
        if (isDestroyed)
            throw exception("$className $this Already destroyed")
        whileVisibleEventRegistrations.cancel()
        isVisibleEventRegistrations.cancel()
        super.onDestroy()
        parentActivityView = null
        activity = null
    }

    fun lifecycleUpdate() {
        parentActivityView?.let {
            if (it.isResumed) {
                if (isPaused) onResume()
            } else if (isResumed) onPause()
        }
    }

    fun lifecycleStop() {
        if (isResumed) onPause()
        onDestroy()
    }

    private fun <Parent> initializeParent(parent: Parent)
            where Parent : CSActivityViewInterface, Parent : CSVisibility {
        activity = parent.activity()
        register(parent.onResume.listen(::onResume))
        register(parent.onPause.listen(::onPause))
        register(parent.onBack.listen(::onBack))
        register(parent.eventViewVisibilityChanged.listen { updateVisibility() })
    }

    protected open fun onBack(goBack: CSProperty<Boolean>) {
        onBack.fire(goBack)
        if (goBack.value && isVisible) {
            hideKeyboard()
            goBack.value = onGoBack()
        }
    }

    protected open fun onGoBack() = true

    override fun onAddedToParent() {
        lifecycleUpdate()
        updateVisibility()
        super.onAddedToParent()
    }

    override fun onRemovedFromParent() {
        if (isResumed) onPause()
        updateVisibility()
        super.onRemovedFromParent()
    }

    fun showingInPager(isShowing: Boolean) {
        if (showingInPager == isShowing) return
        showingInPager = isShowing
        updateVisibility()
    }

    override fun hideKeyboardImpl() {
        activity?.currentFocus?.let {
            input.hideSoftInputFromWindow(it.rootView.windowToken, 0)
        } ?: super.hideKeyboardImpl()
    }

    override fun getLifecycle(): Lifecycle = activity().lifecycle

    override var isVisible = false
    private var onViewShowingCalled = false
    override val eventViewVisibilityChanged = event<Boolean>()

    override fun updateVisibility() {
        if (checkIfIsShowing()) {
            if (!isVisible) onViewVisibilityChanged(true)
        } else if (isVisible) onViewVisibilityChanged(false)
    }

    private fun checkIfIsShowing(): Boolean {
        if (!isResumed) return false
        if (showingInPager == false) return false
        if (parentActivityView?.isVisible == false) return false
        return view.isShowing()
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        if (isVisible == showing) return
        isVisible = showing
        onViewVisibilityChanged()
        if (isVisible) {
            isVisibleEventRegistrations.setActive(true)
            onViewVisible()
        } else {
            isVisibleEventRegistrations.setActive(false)
            onViewHiding()
            whileVisibleEventRegistrations.cancel()
        }
    }

    protected open fun onViewVisibilityChanged() {
        eventViewVisibilityChanged.fire(isVisible)
    }

    protected open fun onViewVisible() {
        if (!onViewShowingCalled) {
            onViewVisibleFirstTime()
            onViewShowingCalled = true
        } else onViewShowingAgain()
    }

    protected open fun onViewVisibleFirstTime() {}

    protected open fun onViewShowingAgain() {}

    protected open fun onViewHiding() {
        if (!onViewShowingCalled) {
            onViewHidingFirstTime()
            onViewShowingCalled = true
        } else onViewHidingAgain()
    }

    protected open fun onViewHidingFirstTime() {}

    protected open fun onViewHidingAgain() {}

    private val isVisibleEventRegistrations = CSEventRegistrations()
    fun ifVisible(registration: CSEventRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    private val whileVisibleEventRegistrations = CSEventRegistrations()
    override fun whileVisible(registration: CSEventRegistration) =
        registration.let { whileVisibleEventRegistrations.add(it) }

  open  val navigation: CSNavigationView? by lazy {
        var controller: CSActivityView<*>? = this
        do {
            if (controller is CSNavigationView) return@lazy controller
            controller = controller?.parentActivityView
        } while (controller != null)
        null
    }
}





