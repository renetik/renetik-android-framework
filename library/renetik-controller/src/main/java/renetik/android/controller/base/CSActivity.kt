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
import renetik.android.framework.event.CSEventRegistrations
import renetik.android.framework.event.CSVisibility
import renetik.android.framework.event.CSEvent.Companion.event
import renetik.android.framework.event.fire
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.lang.property.CSProperty


abstract class CSActivity : AppCompatActivity(), CSActivityViewInterface, CSVisibility {

    val onCreate = event<Bundle?>()
    val onSaveInstanceState = event<Bundle>()
    val onStart = event<Unit>()
    override val eventResume = event<Unit>()
    override val eventPause = event<Unit>()
    val onStop = event<Unit>()
    override val eventDestroy = event<Unit>()
    override val eventBack = event<CSProperty<Boolean>>()
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

    override fun activity(): CSActivity = this
    var activityView: CSActivityView<out ViewGroup>? = null
    var configuration = Configuration()
    override val view: View get() = window.decorView
    override val context: Context get() = this

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
        activityView!!.onDestroy()
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

    override fun onDestroy() {
        super.onDestroy()
        eventDestroy.fire()
        activityView = null
        System.gc()
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.fire(CSActivityResult(requestCode, resultCode, data))
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

    override val eventRegistrations = CSEventRegistrations()
}
