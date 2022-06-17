package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.ContentFrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.core.extensions.content.input
import renetik.android.controller.common.CSNavigationView
import renetik.android.event.CSEvent.Companion.event
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.property.CSProperty
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.ui.protocol.CSVisibility
import renetik.android.ui.protocol.CSVisibleEventOwner
import renetik.android.extensions.isVisible
import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.unexpected
import renetik.android.event.*
import renetik.android.event.owner.CSEventOwner
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistrations

open class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, LifecycleOwner, CSEventOwner, CSVisibleEventOwner {

    override val eventResume = event<Unit>()
    override val eventPause = event<Unit>()
    override val eventBack = event<CSProperty<Boolean>>()
    final override fun activity(): CSActivity = activity!!
    private var isResumed = false
    private var isResumeFirstTime = false
    private val isPaused get() = !isResumed
    private var parentActivityView: CSActivityView<*>? = null
    var activity: CSActivity? = null
    var showingInPager: Boolean? = null

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

    open fun onResume() {
        if (isResumed) return
        isResumed = true
        if (!isResumeFirstTime) {
            onResumeFirstTime()
            isResumeFirstTime = true
        } else onResumeAgain()
        updateVisibility()
        eventResume.fire()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeAgain() {}

    open fun onPause() {
        if (isPaused && isVisible) {
            logWarn(Throwable(), "Not Resumed while paused, should be resumed first", this)
            return
        }
        isResumed = false
        updateVisibility()
        eventPause.fire()
    }

    override fun onDestroy() {
        if (isResumed) onPause()
        if (isDestroyed) unexpected("$className $this Already destroyed")
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
        register(parent.eventResume.listen(::onResume))
        register(parent.eventPause.listen(::onPause))
        register(parent.eventBack.listen(::onBack))
        register(parent.eventVisibility.listen { updateVisibility() })
    }

    protected open fun onBack(goBack: CSProperty<Boolean>) {
        eventBack.fire(goBack)
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

    fun showingInPager(isShowing: Boolean) = apply {
        if (showingInPager == isShowing) return this
        showingInPager = isShowing
        updateVisibility()
    }

    override fun hideKeyboardImpl() {
        activity?.currentFocus?.let {
            input.hideSoftInputFromWindow(it.rootView.windowToken, 0)
        } ?: super.hideKeyboardImpl()
    }

    override fun getLifecycle(): Lifecycle = activity().lifecycle

    private var _isVisible = false
    override val isVisible: Boolean get() = _isVisible
    private var onViewShowingCalled = false
    override val eventVisibility = event<Boolean>()

    override fun updateVisibility() {
        if (checkIfIsShowing()) {
            if (!isVisible) onViewVisibilityChanged(true)
        } else if (isVisible) onViewVisibilityChanged(false)
    }

    private fun checkIfIsShowing(): Boolean {
        if (!isResumed) return false
        if (!view.isVisible) return false
        if (showingInPager == false) return false
        if (showingInPager == true && parentActivityView?.isVisible == true) return true
        if (parentActivityView?.isVisible == false) return false
        return view.isShowing()
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        if (isVisible == showing) return
        _isVisible = showing
        if (isVisible) {
            isVisibleEventRegistrations.setActive(true)
            onViewShowing()
        } else {
            isVisibleEventRegistrations.setActive(false)
            onViewHiding()
            whileVisibleEventRegistrations.cancel()
        }
        onViewVisibilityChanged()
    }

    protected open fun onViewVisibilityChanged() {
        eventVisibility.fire(isVisible)
    }

    protected open fun onViewShowing() {
        if (!onViewShowingCalled) {
            onViewShowingFirstTime()
            onViewShowingCalled = true
        } else onViewShowingAgain()
    }

    protected open fun onViewShowingFirstTime() {}

    protected open fun onViewShowingAgain() {}

    protected open fun onViewHiding() {
        if (!onViewShowingCalled) {
            onViewHidingFirstTime()
            onViewShowingCalled = true
        } else onViewHidingAgain()
    }

    protected open fun onViewHidingFirstTime() {}

    protected open fun onViewHidingAgain() {}

    private val isVisibleEventRegistrations = CSRegistrations()
    fun ifVisible(registration: CSRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    private val whileVisibleEventRegistrations = CSRegistrations()
    override fun whileShowing(registration: CSRegistration) =
        registration.let { whileVisibleEventRegistrations.add(it) }

    open val navigation: CSNavigationView? by lazy {
        var controller: CSActivityView<*>? = this
        do {
            if (controller is CSNavigationView) return@lazy controller
            controller = controller?.parentActivityView
        } while (controller != null)
        null
    }
}

//TODO: could this be in main View extension ?
private fun View.isShowing(): Boolean {
    if (!isVisible) return false
    var view: View = this
    while (true) {
        val parent = view.parent
        when {
            parent == null -> return false
            parent !is View -> return true
            parent is ContentFrameLayout -> return true
            !parent.isVisible -> return false
            parent.asCSActivityView()?.isVisible == true -> return true
            else -> view = parent
        }
    }
}





