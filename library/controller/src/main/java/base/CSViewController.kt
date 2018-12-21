package renetik.android.controller.base

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import renetik.android.base.CSLayoutId
import renetik.android.base.CSView
import renetik.android.controller.common.CSNavigationController
import renetik.android.controller.menu.CSMenuItem
import renetik.android.controller.menu.CSOnMenu
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.extensions.view.findViewRecursive
import renetik.android.logging.CSLog.logWarn
import renetik.java.collections.list
import renetik.java.event.CSEvent.CSEventRegistration
import renetik.java.event.CSEventRegistrations
import renetik.java.event.event
import renetik.java.event.execute
import renetik.java.event.fire
import renetik.java.extensions.exception
import renetik.java.extensions.isNull
import renetik.java.lang.CSValue

var root: CSViewController<*>? = null
val rootActivity get() = root?.activity()

abstract class CSViewController<ViewType : View> : CSView<ViewType>, CSViewControllerParent {
    override val onCreate = event<Bundle?>()
    override val onSaveInstanceState = event<Bundle>()
    override val onStart = event<Unit>()
    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    override val onStop = event<Unit>()
    override val onDestroy = event<Unit>()
    override val onBack = event<CSValue<Boolean>>()
    override val onConfigurationChanged = event<Configuration>()
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

    var state: Bundle? = null
    var parentController: CSViewController<*>? = null
    private var activity: AppCompatActivity? = null
    private val parentRegistrations: CSEventRegistrations
    private var viewId: Int? = null
    private val menuItems = list<CSMenuItem>()
    private var showingInContainer: Boolean? = null
    private var isShowing = false
    private var onViewShowingCalled = false
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


    constructor(parent: CSViewController<*>, viewId: Int) : super(parent) {
        parentController = parent
        this.viewId = viewId
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSNavigationController, layoutId: CSLayoutId) : super(parent.view, layoutId) {
        parentController = parent
        parentRegistrations = initialize(parent)
    }

    protected open fun onCreate(state: Bundle?) {
        if (isCreated) throw exception("Already created")
        this.state = state
        onCreate()
    }

    protected open fun onCreate() {
        isCreated = true
        isResumed = false
        isStarted = false
        isPaused = false
        onCreate.fire(state)
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

    protected fun onResumeFirstTime() {}

    protected fun onResumeRestore() {}

    open fun onPause() {
        if (!isResumed)
            logWarn(Throwable(), "Not Resumed while paused, should be resumed first", this)
        isResumed = false
        isPaused = true
        updateVisibilityChanged()
        onPause.fire()
    }

    protected open fun onStop() {
        if (!isPaused) logWarn(Throwable(), "Not paused while stopped, should be paused first", this)
        isStarted = false
        state = null
        updateVisibilityChanged()
        onStop.fire()
    }

    protected override fun onDestroy() {
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

    fun initialize(state: Bundle?) {
        onCreate(state)
        onStart()
        if (parentController!!.isResumed) onResume()
    }

    fun onDeinitialize() {
        if (isResumed && !isPaused) onPause()
        onStop()
        onDestroy()
    }

    fun startActivityForUri(uri: Uri, onActivityNotFound: ((ActivityNotFoundException) -> Unit)?) =
            startActivityForUriAndType(uri, null, onActivityNotFound)

    fun startActivityForUriAndType(uri: Uri, type: String?,
                                   onActivityNotFound: ((ActivityNotFoundException) -> Unit)?) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, type)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        try {
            startActivity(intent)
        } catch (exception: ActivityNotFoundException) {
            logWarn(exception)
            onActivityNotFound?.invoke(exception)
        }
    }

    private fun initialize(parent: CSViewControllerParent): CSEventRegistrations {
        activity = parent.activity()
        parentController.isNull { root = this }
        return CSEventRegistrations(
                parent.onCreate.execute { argument -> onCreate(argument) },
                parent.onStart.execute { onStart() },
                parent.onResume.execute { onResume() },
                parent.onPause.execute { onPause() },
                parent.onStop.execute { onStop() },
                parent.onDestroy.execute { onDestroy() },
                parent.onBack.execute { argument -> onBack(argument) },
                parent.onActivityResult.execute { argument -> onActivityResult(argument) },
                parent.onCreateOptionsMenu.execute { argument -> onCreateOptionsMenu(argument) },
                parent.onOptionsItemSelected.execute { argument -> onOptionsItemSelected(argument) },
                parent.onPrepareOptionsMenu.execute { argument -> onPrepareOptionsMenu(argument) },
                parent.onKeyDown.execute { argument -> onKeyDown(argument) },
                parent.onNewIntent.execute { argument -> onNewIntent(argument) },
                parent.onUserLeaveHint.execute { onUserLeaveHint() },
                parent.onLowMemory.execute { onLowMemory() },
                parent.onConfigurationChanged.execute { argument -> onConfigurationChanged(argument) },
                parent.onRequestPermissionsResult.execute { argument -> onRequestPermissionsResult(argument) },
                parent.onSaveInstanceState.execute { argument -> onSaveInstanceState(argument) },
                parent.onViewVisibilityChanged.execute { updateVisibilityChanged() }
        )
    }

    protected open fun onLowMemory() = onLowMemory.fire()

    protected fun onBack(goBack: CSValue<Boolean>) {
        onBack.fire(goBack)
        if (goBack.value && isShowing) {
            hideKeyboard()
            goBack.value = onGoBack()
        }
    }

    protected open fun onGoBack() = true

    fun goBack(): Unit = parentController?.goBack() ?: let { activity().onBackPressed() }

    protected fun onActivityResult(result: CSActivityResult) = onActivityResult.fire(result)

    protected fun onCreateOptionsMenu(menu: CSOnMenu) = onCreateOptionsMenu.fire(menu)

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

    protected fun onPrepareOptionsMenu(onMenu: CSOnMenu) {
        onPrepareOptionsMenu.fire(onMenu)
        for (item in menuItems) if (item.isVisible && isShowing) onMenu.show(item)
    }

    protected fun onKeyDown(onKey: CSOnKeyDownResult) = onKeyDown.fire(onKey)

    protected fun onNewIntent(intent: Intent) = onNewIntent.fire(intent)

    protected fun onUserLeaveHint() = onUserLeaveHint.fire()

    override fun createView(): ViewType? {
        return parentController?.let { parent ->
            @Suppress("UNCHECKED_CAST")
            viewId?.let { id -> parent.view.findViewRecursive<ViewType>(id) }
                    ?: parentController!!.view as ViewType
        } ?: throw exception("This should not happen man ;)")
    }

    protected fun onHideByInViewController() {}

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
        if (parentController?.isShowing == false) return false;
        return true
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        isShowing = showing
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
        onViewVisibilityChanged.fire(isShowing)
    }

    protected open fun onViewShowing() {
        if (!onViewShowingCalled) {
            onViewShowingFirstTime()
            onViewShowingCalled = true
        } else onViewShowingAgain()
    }

    protected open fun onViewShowingFirstTime() {}

    protected open fun onViewShowingAgain() {}

    protected fun onViewHiding() {
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

    open fun onConfigurationChanged(newConfig: Configuration) = onConfigurationChanged.fire(newConfig)

    open fun onRequestPermissionsResult(requestPermissionResult: CSRequestPermissionResult) =
            onRequestPermissionsResult.fire(requestPermissionResult)

    open fun onSaveInstanceState(outState: Bundle) = onSaveInstanceState.fire(outState)

    fun invalidateOptionsMenu() = activity?.invalidateOptionsMenu()

    private fun addMenuItem(item: CSMenuItem) = item.apply {
        menuItems.put(this)
        invalidateOptionsMenu()
    }

    protected fun menu(id: Int) = addMenuItem(CSMenuItem(this, id))

    protected fun menu(title: String) = addMenuItem(CSMenuItem(this, title))

    protected fun menu(title: String, iconResource: Int) =
            addMenuItem(CSMenuItem(this, title)).setIconResourceId(iconResource)

    override fun hideKeyboard() {
        val view = rootActivity!!.currentFocus ?: view
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
                .hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }
}
