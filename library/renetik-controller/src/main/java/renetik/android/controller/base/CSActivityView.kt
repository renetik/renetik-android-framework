package renetik.android.controller.base

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import renetik.android.content.input
import renetik.android.controller.menu.CSMenuItem
import renetik.android.controller.menu.CSOnMenu
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.framework.lang.CSProperty
import renetik.android.java.extensions.className
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.java.extensions.exception
import renetik.android.java.extensions.unexpected
import renetik.android.logging.CSLog.logWarn
import renetik.android.view.extensions.findViewRecursive

abstract class CSActivityView<ViewType : View>
    : CSView<ViewType>, CSActivityViewInterface, LifecycleOwner, CSEventOwner {

    override val onCreate = event<Bundle?>()
    override val onSaveInstanceState = event<Bundle>()
    override val onStart = event<Unit>()
    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onStop = event<Unit>()
    override val onDestroy = event<CSActivityViewInterface>()
    override val onBack = event<CSProperty<Boolean>>()
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
    override fun activity() = activity!!

    private val eventRegistrations = CSEventRegistrations()

    var isStarted = false
    var isCreated = false
    var isResumed = false
    var isResumeFirstTime = false
    var isPaused = false
    var isDestroyed = false

    var bundle: Bundle? = null
    var parentController: CSActivityView<*>? = null
    var activity: AppCompatActivity? = null
    private val parentRegistrations: CSEventRegistrations
    private var viewId: Int? = null
    val menuItems = list<CSMenuItem>()
    private var showingInPager: Boolean? = null
    private val keyValueMap = mutableMapOf<String, Any>()
    var lifecycleStopOnRemoveFromParent = true

    constructor(activity: CSActivity, layout: CSLayoutRes) : super(activity, layout) {
        this.activity = activity
        parentRegistrations = initializeParent(activity)
    }

    constructor(parent: CSActivityView<*>) : super(parent) {
        parentController = parent
        parentRegistrations = initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, view: ViewType) : super(parent, view) {
        parentController = parent
        parentRegistrations = initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, @IdRes viewId: Int) : super(parent) {
        parentController = parent
        this.viewId = viewId
        parentRegistrations = initializeParent(parent)
        lifecycleUpdate()
    }

    constructor(parent: CSActivityView<out ViewGroup>, layoutRes: CSLayoutRes)
            : super(parent.view, layoutRes) {
        parentController = parent
        parentRegistrations = initializeParent(parent)
    }

    constructor(parent: CSActivityView<*>, group: ViewGroup, layout: CSLayoutRes)
            : super(group, layout) {
        parentController = parent
        parentRegistrations = initializeParent(parent)
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
        if (isResumed)
            logWarn("already resumed", this) //TODO: this is called!!!
        isResumed = true
        isPaused = false
        if (!isResumeFirstTime) onResumeFirstTime()
        else onResumeRestore()
        isResumeFirstTime = true
        updateVisibilityChanged()
        onResume.fire()
    }

    protected open fun onResumeFirstTime() {}

    protected open fun onResumeRestore() {}

    open fun onPause() {
        if (!isResumed && isShowing)
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
        if (isDestroyed) throw exception("$className $this Already destroyed")
        parentRegistrations.cancel()
        parentController = null
        activity = null
        isDestroyed = true
        eventRegistrations.cancel()
        onDestroy.fire(this)
        System.gc()
    }

    fun lifecycleUpdate() {
        parentController?.let {
            if (it.isCreated && !isCreated) onCreate(it.bundle)
            if (it.isStarted && !isStarted) onStart()
            if (it.isResumed && !isResumed) onResume()
            if (it.isPaused && !isPaused) onPause()
        }
    }

    fun lifecycleStop() {
        if (isResumed && !isPaused) onPause()
        onStop()
        onDestroy()
    }

    private fun initializeParent(parent: CSActivityViewInterface): CSEventRegistrations {
        activity = parent.activity()
        return CSEventRegistrations(
            parent.onCreate.listen { argument -> onCreate(argument) },
            parent.onStart.listen { onStart() },
            parent.onResume.listen { onResume() },
            parent.onPause.listen { onPause() },
            parent.onStop.listen { onStop() },
            parent.onDestroy.listen { onDestroy() },
            parent.onBack.listen { argument -> onBack(argument) },
            parent.onActivityResult.listen { argument -> onActivityResult(argument) },
            parent.onCreateOptionsMenu.listen { argument -> onCreateOptionsMenu(argument) },
            parent.onOptionsItemSelected.listen { argument -> onOptionsItemSelected(argument) },
            parent.onPrepareOptionsMenu.listen { argument -> onPrepareOptionsMenu(argument) },
            parent.onKeyDown.listen { argument -> onKeyDown(argument) },
            parent.onNewIntent.listen { argument -> onNewIntent(argument) },
            parent.onUserLeaveHint.listen { onUserLeaveHint() },
            parent.onLowMemory.listen { onLowMemory() },
            parent.onConfigurationChanged.listen { argument -> onConfigurationChanged(argument) },
            parent.onOrientationChanged.listen { argument -> onOrientationChanged(argument) },
            parent.onRequestPermissionsResult.listen { argument ->
                onRequestPermissionsResult(argument)
            },
            parent.onSaveInstanceState.listen { argument -> onSaveInstanceState(argument) },
            parent.onViewVisibilityChanged.listen { updateVisibilityChanged() }
        )
    }

    protected open fun onLowMemory() = onLowMemory.fire()

    protected open fun onBack(goBack: CSProperty<Boolean>) {
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
        } ?: throw unexpected
    }

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

    override fun onViewVisibilityChanged() {
        super.onViewVisibilityChanged()
        invalidateOptionsMenu()
    }

    override fun register(registration: CSEventRegistration) =
        registration.let { eventRegistrations.add(it) }

    fun cancel(registration: CSEventRegistration) =
        eventRegistrations.cancel(registration)

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

