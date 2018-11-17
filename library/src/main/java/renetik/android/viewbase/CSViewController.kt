package renetik.android.viewbase

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import renetik.android.extensions.isNull
import renetik.android.extensions.view.findViewRecursive
import renetik.android.java.collections.list
import renetik.android.java.event.CSEvent.CSEventRegistration
import renetik.android.java.event.CSEventRegistrations
import renetik.android.java.event.event
import renetik.android.java.event.execute
import renetik.android.java.event.fire
import renetik.android.java.lang.CSValue
import renetik.android.lang.CSLang.*
import renetik.android.view.CSNavigationController
import renetik.android.viewbase.menu.CSMenuItem
import renetik.android.viewbase.menu.CSOnMenu
import renetik.android.viewbase.menu.CSOnMenuItem

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

    var isStarted = NO
    var isCreated = NO
    var isResumed = NO
    var isResumeFirstTime = NO
    var isPaused = NO
    var isDestroyed = NO

    var state: Bundle? = null
    var parentController: CSViewController<*>? = null
    private var activity: AppCompatActivity? = null
    private val parentRegistrations: CSEventRegistrations
    private var viewId: Int? = null
    private val menuItems = list<CSMenuItem>()
    private var showingInContainer: Boolean? = null
    private var isShowing = NO
    private var onViewShowingCalled = NO
    val isRoot get() = root === this
    val actionBar get() = activity().supportActionBar

    constructor(activity: CSActivity, layout: CSLayoutId? = null) : super(activity, layout) {
        this.activity = activity
        parentRegistrations = initialize(activity)
    }

    constructor(parent: CSViewController<*>) : super(parent) {
        this.parentController = parent
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSViewController<*>, viewId: Int) : super(parent) {
        this.parentController = parent
        this.viewId = viewId
        parentRegistrations = initialize(parent)
    }

    constructor(parent: CSNavigationController, layoutId: CSLayoutId) : super(parent.view, layoutId) {
        this.parentController = parent
        parentRegistrations = initialize(parent)
    }

    protected open fun onCreate(state: Bundle?) {
        if (isCreated) throw exception("Already created")
        this.state = state
        onCreate()
    }

    protected open fun onCreate() {
        isCreated = YES
        isResumed = NO
        isStarted = NO
        isPaused = NO
        onCreate.fire(state)
    }

    protected open fun onStart() {
        isStarted = true
        onStart.fire()
    }

    protected open fun onResume() {
        if (isResumed) warn("already resumed", this)
        isResumed = true
        isPaused = false
        if (!isResumeFirstTime) onResumeFirstTime()
        else onResumeRestore()
        isResumeFirstTime = YES
        onResume.fire()
        updateVisibilityChanged()
    }

    protected fun onResumeFirstTime() {}

    protected fun onResumeRestore() {}

    open fun onPause() {
        if (!isResumed)
            warn(Throwable(), "Not Resumed while paused, should be resumed first", this)
        isResumed = NO
        isPaused = YES
        updateVisibilityChanged()
        onPause.fire()
    }

    protected open fun onStop() {
        if (!isPaused) warn(Throwable(), "Not paused while stopped, should be paused first", this)
        isStarted = NO
        state = null
        updateVisibilityChanged()
        onStop.fire()
    }

    protected override fun onDestroy() {
        super.onDestroy()
        if (isStarted) warn(Throwable(), "Started while destroyed, should be stopped first")
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
            warn(exception)
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
        if (goBack.value && isShowing) goBack.setValue(onGoBack())
    }

    protected open fun onGoBack() = YES

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
        onMenu.onPrepareItems(menuItems)
    }

    protected fun onKeyDown(onKey: CSOnKeyDownResult) = onKeyDown.fire(onKey)

    protected fun onNewIntent(intent: Intent) = onNewIntent.fire(intent)

    protected fun onUserLeaveHint() = onUserLeaveHint.fire()

    protected override fun createView(): ViewType? {
        return parentController?.let { parent ->
            viewId?.let { id -> parent.view.findViewRecursive<ViewType>(id) }
                    ?: parentController!!.view as ViewType
        } ?: throw exception("This should not happen man ;)")
    }

    protected fun onHideByInViewController() {}

    fun showingInContainer(isShowing: Boolean) {
        if (equal(showingInContainer, isShowing)) return
        showingInContainer = isShowing
        updateVisibilityChanged()
    }

    protected fun updateVisibilityChanged() {
        if (checkIfIsShowing()) {
            if (!isShowing) onViewVisibilityChanged(YES)
        } else if (isShowing) onViewVisibilityChanged(NO)
    }

    protected fun checkIfIsShowing(): Boolean {
        if (!isResumed) return NO
        if (showingInContainer == NO) return NO
        if (parentController?.isShowing == NO) return NO;
        return YES
    }

    private fun onViewVisibilityChanged(showing: Boolean) {
        isShowing = showing
        if (isShowing) {
            isVisibleEventRegistrations.setActive(YES)
            invalidateOptionsMenu()
            onViewShowing()
        } else {
            isVisibleEventRegistrations.setActive(NO)
            invalidateOptionsMenu()
            onViewHiding()
            whileShowingEventRegistrations.cancel()
        }
        onViewVisibilityChanged.fire(isShowing)
    }

    protected open fun onViewShowing() {
        if (!onViewShowingCalled) {
            onViewShowingFirstTime()
            onViewShowingCalled = YES
        } else onViewShowingAgain()
    }

    protected open fun onViewShowingFirstTime() {}

    protected open fun onViewShowingAgain() {}

    protected fun onViewHiding() {
        if (!onViewShowingCalled) {
            onViewHidingFirstTime()
            onViewShowingCalled = YES
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
}
