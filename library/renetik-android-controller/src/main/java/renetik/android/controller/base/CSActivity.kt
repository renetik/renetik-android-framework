package renetik.android.controller.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import renetik.android.controller.menu.CSOnMenu
import renetik.android.controller.menu.CSOnMenuItem
import renetik.android.controller.menu.GeneratedMenuItems
import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.destroy
import renetik.android.event.fire
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.ui.protocol.CSVisibility

abstract class CSActivity : AppCompatActivity(), CSActivityViewInterface, CSVisibility {
    val onCreate = event<Bundle?>()
    val onSaveInstanceState = event<Bundle>()
    val onStart = event<Unit>()

    //CSActivityViewInterface
    override val eventResume = event<Unit>()
    override val eventPause = event<Unit>()
    override val eventBack = event<CSVariable<Boolean>>()
    override fun activity(): CSActivity = this

    val onStop = event<Unit>()
    val onConfigurationChanged = event<Configuration>()
    val onOrientationChanged = event<Configuration>()
    val onLowMemory = event<Unit>()
    val onUserLeaveHint = event<Unit>()
    val onPrepareOptionsMenu = event<CSOnMenu>()
    val onOptionsItemSelected = event<CSOnMenuItem>()
    val onCreateOptionsMenu = event<CSOnMenu>()
    val onActivityResult = event<CSActivityResult>()
    val onKeyDown = event<CSOnKeyDownResult>()
    val onNewIntent = event<Intent>()
    val onRequestPermissionsResult = event<CSRequestPermissionResult>()

    //CSVisibility
    override val isVisible: Boolean get() = true
    override val eventVisibility = event<Boolean>()
    override fun updateVisibility() = Unit

    var activityView: CSActivityView<out ViewGroup>? = null
    val configuration by lazy { Configuration() }

    //CSViewInterface
    override val view: View get() = window.decorView

    //CSHasContext
    override val context: Context get() = this

    //CSHasRegistrations
    final override val registrations by lazy { CSRegistrationsMap(this) }

    abstract fun createView(): CSActivityView<out ViewGroup>

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        configuration.updateFrom(resources.configuration)
        activityView = createView()
        setContentView(activityView!!.view)
        onCreate.fire(state)
    }

    var isRecreateView = false

    fun recreateView() {
        isRecreateView = true
        activityView!!.destroy()
        configuration.updateFrom(resources.configuration)
        activityView = createView()
        setContentView(activityView!!.view)
        activityView!!.onResume()
        isRecreateView = false
    }

    override fun onStart() {
        onStart.fire()
        super.onStart()
    }

    override fun onResume() {
        eventResume.fire()
        super.onResume()
    }

    override fun onPause() {
        eventPause.fire()
        super.onPause()
    }

    override fun onStop() {
        onStop.fire()
        super.onStop()
    }

    // CSHasDestruct
    override val isDestructed: Boolean get() = isDestroyed
    override val eventDestruct = event<Unit>()
    override fun onDestruct() {
        super.onDestroy()
        registrations.cancel()
        eventDestruct.fire().clear()
        activityView = null
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.fire(CSActivityResult(requestCode, resultCode, data))
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        onKeyDown.fire(CSOnKeyDownResult(keyCode, event))
        return super.onKeyDown(keyCode, event)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveInstanceState.fire(outState)
    }

    override fun onNewIntent(intent: Intent) {
        onNewIntent.fire(intent)
        super.onNewIntent(intent)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val goBack = property(true)
        eventBack.fire(goBack)
        if (goBack.value) super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onUserLeaveHint() {
        onUserLeaveHint.fire()
        super.onUserLeaveHint()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val onMenu = CSOnMenu(this, menu)
        onCreateOptionsMenu.fire(onMenu)
        return onMenu.showMenu.value
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.removeGroup(GeneratedMenuItems)
        val onMenu = CSOnMenu(this, menu)
        onPrepareOptionsMenu.fire(onMenu)
        return onMenu.showMenu.value
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onConfigurationChanged.fire(newConfig)
        if (configuration.orientation != newConfig.orientation)
            onOrientationChanged.fire(newConfig)
        configuration.updateFrom(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val onMenuItem = CSOnMenuItem(item)
        onOptionsItemSelected.fire(onMenuItem)
        return onMenuItem.isConsumed
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        onRequestPermissionsResult.fire(CSRequestPermissionResult(requestCode,
            permissions,
            results))
    }

    override fun onLowMemory() {
        onLowMemory.fire()
        super.onLowMemory()
    }
}
