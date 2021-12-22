package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.widget.ContentFrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.content.input
import renetik.android.controller.common.CSNavigationView
import renetik.android.framework.event.*
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.property.CSProperty
import renetik.android.framework.logging.CSLog.warn
import renetik.android.view.isVisible
import renetik.kotlin.className
import renetik.kotlin.unexpected

abstract class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, LifecycleOwner, CSEventOwner, CSVisibleEventOwner {

    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onBack = event<CSProperty<Boolean>>()
    final override fun activity(): CSActivity = activity!!
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

    open fun onResume() {
        if (isResumed) return
        isResumed = true
        if (!isResumeFirstTime) {
            onResumeFirstTime()
            isResumeFirstTime = true
        } else onResumeAgain()
        updateVisibility()
        onResume.fire()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeAgain() {}

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

    private var _isVisible = false
    override val isVisible: Boolean get() = _isVisible
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

        // This is useful when showing just started in parent container,
        // so view.isShowing() returns false
        if (showingInPager == true && parentActivityView?.isVisible == true) return true

//        if (parentActivityView?.isVisible == false) return false //TODO !!!!!!
        return view.isShowing()
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        if (isVisible == showing) return
        _isVisible = showing
        onViewVisibilityChanged()
        if (isVisible) {
            isVisibleEventRegistrations.setActive(true)
            onViewShowing()
        } else {
            isVisibleEventRegistrations.setActive(false)
            onViewHiding()
            whileVisibleEventRegistrations.cancel()
        }
    }

    protected open fun onViewVisibilityChanged() {
        eventViewVisibilityChanged.fire(isVisible)
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

    private val isVisibleEventRegistrations = CSEventRegistrations()
    fun ifVisible(registration: CSEventRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    private val whileVisibleEventRegistrations = CSEventRegistrations()
    override fun whileShowing(registration: CSEventRegistration) =
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





