package renetik.android.controller.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.base.CSLayoutId
import renetik.android.base.CSView
import renetik.android.controller.menu.CSMenuItem
import renetik.android.controller.menu.CSOnMenu
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.extensions.service
import renetik.android.java.common.CSValue
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.event.CSEventRegistrations
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.event.register
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.isNull
import renetik.android.java.extensions.later
import renetik.android.logging.CSLog.logWarn
import renetik.android.view.extensions.findViewRecursive

var root: CSViewController<*>? = null

abstract class CSViewController<ViewType : View> : CSView<ViewType>, CSViewControllerParent,
    LifecycleOwner {
    override val onCreate = event<Bundle?>()
    override val onSaveInstanceState = event<Bundle>()
    override val onStart = event<Unit>()
    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onStop = event<Unit>()
    override val onDestroy = event<Unit>()
    override val onBack = event<CSValue<Boolean>>()
    override val onConfigurationChanged = event<Configuration>()
    override val onOrientationChanged = event<Configuration>()
    override val onLowMemory = event<Unit>()
    override val onUserLeaveHint = event<Unit>()
    override val onPrepareOptionsMenu = event<CSOnMenu>()
    override val onOptionsItemSelected = event<CSOnMenuItem>()
    override val onCreateOptionsMenu = event<CSOnMenu>()
    override val onActivityResult = event<CSActivityResult>()
    override val onKeyDown = event<CSOnKeyDownResult>()
    override val onNewIntent = event<Intent>()
    override val onRequestPermissionsResult = event<CSRequestPermissionResult>()
    override val onViewVisibilityChanged = event<Boolean>()
    override fun activity() = activity!!

    private val isVisibleEventRegistrations = CSEventRegistrations()
    private val whileShowingEventRegistrations = CSEventRegistrations()
    private val eventRegistrations = CSEventRegistrations()

    var isStarted = false
    var isCreated = false
    var isResumed = false
    var isResumeFirstTime = false
    var isPaused = false
    var isDestroyed = false

    var bundle: Bundle? = null
    var parentController: CSViewController<*>? = null
    private var activity: AppCompatActivity? = null
    private val parentRegistrations: CSEventRegistrations
    private var viewId: Int? = null
    val menuItems = list<CSMenuItem>()
    private var showingInContainer: Boolean? = null
    private var isShowing = false
    private var onViewShowingCalled = false
    private val keyValueMap = mutableMapOf<String, Any>()
    val isRoot get() = root === this
    val actionBar get() = activity().supportActionBar

    constructor(activity: CSActivity, layout: CSLayoutId? = null) : super(activity, layout) {
        this.activity = activity
        parentRegistrations = initialize(activity)
    }

    constructor(parent: CSViewController<*>) : super(parent) {
        parentController = parent
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSViewController<*>, view: ViewType) : super(parent) {
        parentController = parent
        setView(view)
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSViewController<*>, @IdRes viewId: Int) : super(parent) {
        parentController = parent
        this.viewId = viewId
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSViewController<out ViewGroup>, layoutId: CSLayoutId? = null)
            : super(parent.view, layoutId) {
        parentController = parent
        parentRegistrations = initialize(parent)
    }

    protected open fun onCreate(bundle: Bundle?) {
        if (isCreated) throw exception("Already created $this")
        this.bundle = bundle
        onCreate()
    }

    protected open fun onCreate() {
        isCreated = true
        isResumed = false
        isStarted = false
        isPaused = false
        onCreate.fire(bundle)
    }

    protected open fun onStart() {
        isStarted = true
        onStart.fire()
    }

    protected open fun onResume() {
        if (isResumed) logWarn("already resumed", this)
        isResumed = true
        isPaused = false
        if (!isResumeFirstTime) onResumeFirstTime()
        else onResumeRestore()
        isResumeFirstTime = true
        onResume.fire()
        updateVisibilityChanged()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeRestore() {}

    open fun onPause() {
        if (!isResumed)
            logWarn(Throwable(), "Not Resumed while paused, should be resumed first", this)
        isResumed = false
        isPaused = true
        updateVisibilityChanged()
        onPause.fire()
    }

    protected open fun onStop() {
        if (!isPaused) logWarn(
            Throwable(),
            "Not paused while stopped, should be paused first",
            this
        )
        isStarted = false
        bundle = null
        updateVisibilityChanged()
        onStop.fire()
    }

    protected open fun onBeforeDestroy() = Unit

    override fun onDestroy() {
        onBeforeDestroy()
        super.onDestroy()
        if (isStarted) logWarn(Throwable(), "Started while destroyed, should be stopped first")
        if (isDestroyed) throw exception("Already destroyed")
        parentRegistrations.cancel()
        parentController = null
        if (isRoot) root = null
        activity = null
        isDestroyed = true
        whileShowingEventRegistrations.cancel()
        isVisibleEventRegistrations.cancel()
        eventRegistrations.cancel()
        onDestroy.fire()
        System.gc()
    }

    fun initialize() {
        parentController?.let {
            if (it.isCreated) onCreate(it.bundle)
            if (it.isStarted) onStart()
            if (it.isResumed) onResume()
        }
    }

    fun deInitialize() {
        if (isResumed && !isPaused) onPause()
        onStop()
        onDestroy()
    }

    private fun initialize(parent: CSViewControllerParent): CSEventRegistrations {
        activity = parent.activity()
        parentController.isNull { root = this }
        return CSEventRegistrations(
            parent.onCreate.register { argument -> onCreate(argument) },
            parent.onStart.register { onStart() },
            parent.onResume.register { onResume() },
            parent.onPause.register { onPause() },
            parent.onStop.register { onStop() },
            parent.onDestroy.register { onDestroy() },
            parent.onBack.register { argument -> onBack(argument) },
            parent.onActivityResult.register { argument -> onActivityResult(argument) },
            parent.onCreateOptionsMenu.register { argument -> onCreateOptionsMenu(argument) },
            parent.onOptionsItemSelected.register { argument -> onOptionsItemSelected(argument) },
            parent.onPrepareOptionsMenu.register { argument -> onPrepareOptionsMenu(argument) },
            parent.onKeyDown.register { argument -> onKeyDown(argument) },
            parent.onNewIntent.register { argument -> onNewIntent(argument) },
            parent.onUserLeaveHint.register { onUserLeaveHint() },
            parent.onLowMemory.register { onLowMemory() },
            parent.onConfigurationChanged.register { argument -> onConfigurationChanged(argument) },
            parent.onOrientationChanged.register { argument -> onOrientationChanged(argument) },
            parent.onRequestPermissionsResult.register { argument ->
                onRequestPermissionsResult(
                    argument
                )
            },
            parent.onSaveInstanceState.register { argument -> onSaveInstanceState(argument) },
            parent.onViewVisibilityChanged.register { updateVisibilityChanged() }
        )
    }

    protected open fun onLowMemory() = onLowMemory.fire()

    protected open fun onBack(goBack: CSValue<Boolean>) {
        onBack.fire(goBack)
        if (goBack.value && isShowing) {
            hideKeyboard()
            goBack.value = onGoBack()
        }
    }

    protected open fun onGoBack() = true

    fun goBack(): Unit = parentController?.goBack() ?: let { activity().onBackPressed() }

    protected open fun onActivityResult(result: CSActivityResult) = onActivityResult.fire(result)

    protected open fun onCreateOptionsMenu(menu: CSOnMenu) = onCreateOptionsMenu.fire(menu)

    protected open fun onOptionsItemSelected(onItem: CSOnMenuItem) {
        if (onItem.isConsumed) return
        onOptionsItemSelected.fire(onItem)
        for (item in menuItems)
            if (onItem.consume(item)) {
                if (onItem.isCheckable) {
                    item.onChecked(onItem)
                    invalidateOptionsMenu()
                } else item.run()
                break
            }
    }

    protected open fun onPrepareOptionsMenu(onMenu: CSOnMenu) {
        onPrepareOptionsMenu.fire(onMenu)
        for (item in menuItems) if (item.isVisible && isShowing) onMenu.show(item)
    }

    protected open fun onKeyDown(onKey: CSOnKeyDownResult) = onKeyDown.fire(onKey)

    protected open fun onNewIntent(intent: Intent) = onNewIntent.fire(intent)

    protected open fun onUserLeaveHint() = onUserLeaveHint.fire()

    override fun obtainView(): ViewType? {
        return parentController?.let { parent ->
            @Suppress("UNCHECKED_CAST")
            viewId?.let { id -> parent.view.findViewRecursive<ViewType>(id) }
                ?: parentController!!.view as ViewType
        } ?: throw exception("This should not happen man ;)")
    }

    protected open fun onHideByInViewController() {}

    fun showingInContainer(isShowing: Boolean) {
        if (showingInContainer == isShowing) return
        showingInContainer = isShowing
        updateVisibilityChanged()
    }

    protected fun updateVisibilityChanged() {
        if (checkIfIsShowing()) {
            if (!isShowing) onViewVisibilityChanged(true)
        } else if (isShowing) onViewVisibilityChanged(false)
    }

    protected fun checkIfIsShowing(): Boolean {
        if (!isResumed) return false
        if (showingInContainer == false) return false
        if (parentController?.isShowing == false) return false
        return true
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        isShowing = showing
        onViewVisibilityChanged.fire(isShowing)
        if (isShowing) {
            isVisibleEventRegistrations.setActive(true)
            invalidateOptionsMenu()
            onViewShowing()
        } else {
            isVisibleEventRegistrations.setActive(false)
            invalidateOptionsMenu()
            onViewHiding()
            whileShowingEventRegistrations.cancel()
        }
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

    protected fun ifVisible(registration: CSEventRegistration?) =
        registration?.let { isVisibleEventRegistrations.add(it) }

    protected fun register(registration: CSEventRegistration?) =
        registration?.let { eventRegistrations.add(it) }

    protected fun whileShowing(registration: CSEventRegistration?) =
        registration?.let { whileShowingEventRegistrations.add(it) }

    protected open fun onConfigurationChanged(newConfig: Configuration) =
        onConfigurationChanged.fire(newConfig)

    protected open fun onOrientationChanged(newConfig: Configuration) =
        onOrientationChanged.fire(newConfig)

    protected open fun onRequestPermissionsResult(requestPermissionResult: CSRequestPermissionResult) =
        onRequestPermissionsResult.fire(requestPermissionResult)

    protected open fun onSaveInstanceState(outState: Bundle) = onSaveInstanceState.fire(outState)

    fun invalidateOptionsMenu() = activity?.invalidateOptionsMenu()

    fun addMenuItem(item: CSMenuItem) = item.apply {
        menuItems.put(this)
        invalidateOptionsMenu()
    }

    fun removeMenuItem(item: CSMenuItem) {
        menuItems.remove(item)
        invalidateOptionsMenu()
    }

    override fun hideKeyboard() = later {
        activity!!.currentFocus?.let {
            service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
                .hideSoftInputFromWindow(it.rootView.windowToken, 0)
        } ?: super.hideKeyboard()
    }

    override fun getLifecycle(): Lifecycle = activity().lifecycle

    fun setValue(key: String, value: String) {
        keyValueMap[key] = value
    }

    fun getValue(key: String) = keyValueMap[key]
}
