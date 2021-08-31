package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.content.input
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSProperty
import renetik.android.java.extensions.className
import renetik.android.java.extensions.exception
import renetik.android.logging.CSLog.logWarn
import renetik.android.view.extensions.isShowing

abstract class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, CSVisibleEventOwner,
    LifecycleOwner, CSEventOwner, CSVisibility {

    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onBack = event<CSProperty<Boolean>>()
    override fun activity(): CSActivity = activity!!
    private var isResumed = false
    private var isResumeFirstTime = false
    private val isPaused get() = !isResumed
    var parentController: CSActivityView<*>? = null // remove CSListView make private
    var activity: CSActivity? = null

    private var showingInPager: Boolean? = null
    private val keyValueMap = mutableMapOf<String, Any>()

    constructor(parent: CSActivityView<*>) : super(parent) {
        parentController = parent
        initializeParent(parent)
    }

    constructor(activity: CSActivity, layout: CSLayoutRes) : super(activity, layout) {
        this.activity = activity
        initializeParent(activity)
    }

    constructor(parent: CSActivityView<*>, @IdRes viewId: Int) : super(parent, viewId) {
        parentController = parent
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, layout: CSLayoutRes) : super(parent, layout) {
        parentController = parent
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, group: ViewGroup, layout: CSLayoutRes)
            : super(parent, group, layout) {
        parentController = parent
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
            logWarn(Throwable(), "Not Resumed while paused, should be resumed first", this)
            return
        }
        isResumed = false
        updateVisibility()
        onPause.fire()
    }

    override fun onDestroy() {
        if (isDestroyed)
            throw exception("$className $this Already destroyed")
        parentController = null
        activity = null
        whileVisibleEventRegistrations.cancel()
        isVisibleEventRegistrations.cancel()
        super.onDestroy()
    }

    fun lifecycleUpdate() {
        parentController?.let {
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
        register(parent.onViewVisibilityChanged.listen { updateVisibility() })
    }

    protected open fun onBack(goBack: CSProperty<Boolean>) {
        onBack.fire(goBack)
        if (goBack.value && isVisible) {
            hideKeyboard()
            goBack.value = onGoBack()
        }
    }

    protected open fun onGoBack() = true

    fun goBack(): Unit = parentController?.goBack() ?: let { activity().onBackPressed() }

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

    //TODO: Remove keyValueMap, PushId in CSNavigation could be done cleaner by interface !!!
    fun setValue(key: String, value: String) {
        keyValueMap[key] = value
    }

    fun getValue(key: String) = keyValueMap[key]


    override var isVisible = false
    private var onViewShowingCalled = false
    override val onViewVisibilityChanged = event<Boolean>()

    override fun updateVisibility() {
        if (checkIfIsShowing()) {
            if (!isVisible) onViewVisibilityChanged(true)
        } else if (isVisible) onViewVisibilityChanged(false)
    }

    fun checkIfIsShowing(): Boolean {
        if (!isResumed) return false
        if (showingInPager == false) return false
        if (parentController?.isVisible == false) return false
        return view.isShowing()
    }

//    protected open fun checkIfIsShowing() = view.isShowing()

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
        onViewVisibilityChanged.fire(isVisible)
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
}



