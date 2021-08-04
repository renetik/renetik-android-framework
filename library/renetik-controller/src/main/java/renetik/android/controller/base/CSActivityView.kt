package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.content.input
import renetik.android.framework.event.CSEventOwner
import renetik.android.framework.event.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.listen
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSProperty
import renetik.android.java.extensions.className
import renetik.android.java.extensions.exception
import renetik.android.logging.CSLog.logWarn

abstract class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, LifecycleOwner, CSEventOwner {

    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onBack = event<CSProperty<Boolean>>()
    override fun activity() = activity!!
    var isResumed = false
    var isResumeFirstTime = false
    val isPaused get() = !isResumed
    var parentController: CSActivityView<*>? = null
    var activity: CSActivity? = null

    //    private var viewId: Int? = null
    private var showingInPager: Boolean? = null
    private val keyValueMap = mutableMapOf<String, Any>()
    var lifecycleStopOnRemoveFromParent = true

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
//        this.viewId = viewId
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, layout: CSLayoutRes) : super(parent, layout) {
        parentController = parent
        initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, group: ViewGroup, layout: CSLayoutRes)
            : super(group, layout) {
        parentController = parent
        initializeParent(parent)
    }

    protected open fun onResume() {
        if (isResumed) logWarn("already resumed", this)
        isResumed = true
        if (!isResumeFirstTime) {
            onResumeFirstTime()
            isResumeFirstTime = true
        } else onResumeRestore()
        updateVisibilityChanged()
        onResume.fire()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeRestore() {}

    open fun onPause() {
        if (!isResumed && isShowing)
            logWarn(Throwable(), "Not Resumed while paused, should be resumed first", this)
        isResumed = false
        updateVisibilityChanged()
        onPause.fire()
    }

    override fun onDestroy() {
        if (isDestroyed) throw exception("$className $this Already destroyed")
        parentController = null
        activity = null
        super.onDestroy()
    }

    fun lifecycleUpdate() {
        parentController?.let {
            if (it.isResumed) {
                if (!isResumed) onResume()
            } else if (!isPaused) onPause()
        }
    }

    fun lifecycleStop() {
        if (isResumed) onPause()
        onDestroy()
    }

    private fun initializeParent(parent: CSActivityViewInterface) {
        activity = parent.activity()
        register(parent.onResume.listen(::onResume))
        register(parent.onPause.listen(::onPause))
        register(parent.onDestroy.listen(::onDestroy))
        register(parent.onBack.listen(::onBack))
        register(parent.onViewVisibilityChanged.listen { updateVisibilityChanged() })
    }

    protected open fun onBack(goBack: CSProperty<Boolean>) {
        onBack.fire(goBack)
        if (goBack.value && isShowing) {
            hideKeyboard()
            goBack.value = onGoBack()
        }
    }

    protected open fun onGoBack() = true

    fun goBack(): Unit = parentController?.goBack() ?: let { activity().onBackPressed() }

    override fun onAddedToParent() {
        lifecycleUpdate()
        super.onAddedToParent()
    }

    override fun onRemovedFromParent() {
        if (lifecycleStopOnRemoveFromParent) lifecycleStop()
        else if (isResumed && !isPaused) onPause()
        super.onRemovedFromParent()
    }

    fun showingInPager(isShowing: Boolean) {
        if (showingInPager == isShowing) return
        showingInPager = isShowing
        updateVisibilityChanged()
    }

    override fun checkIfIsShowing(): Boolean {
        if (!isResumed) return false
        if (showingInPager == false) return false
        if (parentController?.isShowing == false) return false
        return super.checkIfIsShowing()
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
}

