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
import renetik.android.framework.common.catchAllWarn
import renetik.android.framework.event.*
import renetik.android.framework.event.property.CSEventPropertyFunctions.property
import renetik.android.framework.lang.CSProperty


abstract class CSActivity : AppCompatActivity(), CSActivityViewInterface, CSVisibility {

    val onCreate = event<Bundle?>()
    val onSaveInstanceState = event<Bundle>()
    val onStart = event<Unit>()
    override val onResume = event<Unit>()
    override val onPause = event<Unit>()
    val onStop = event<Unit>()
    override val onDestroy = event<Unit>()
    override val onBack = event<CSProperty<Boolean>>()
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
    override val onViewVisibilityChanged = event<Boolean>()
    override fun updateVisibility() = Unit

    override fun activity(): CSActivity = this
    var controller: CSActivityView<out ViewGroup>? = null
    var configuration = Configuration()
    override val view: View get() = window.decorView
    override val context: Context get() = this

    abstract fun createController(): CSActivityView<out ViewGroup>

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        configuration.updateFrom(resources.configuration)
        controller = createController()
        setContentView(controller!!.view)
        onCreate.fire(state)
    }

    override fun onStart() {
        onStart.fire()
        super.onStart()
    }

    override fun onResume() {
        onResume.fire()
        super.onResume()
    }

    override fun onPause() {
        onPause.fire()
        super.onPause()
    }

    override fun onStop() {
        onStop.fire()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        onDestroy.fire()
//        onDestroyUnbindDrawables(controller!!.view)
        controller = null
        System.gc()
    }

    private fun onDestroyUnbindDrawables(view: View) {
        view.background?.callback = null
        if (view is ViewGroup) {
            for (index in 0 until view.childCount)
                onDestroyUnbindDrawables(view.getChildAt(index))
            catchAllWarn { view.removeAllViews() }
        }
    }

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
        onBack.fire(goBack)
        if (goBack.value) super.onBackPressed()
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

    private val eventRegistrations = CSEventRegistrations()
    override fun register(registration: CSEvent.CSEventRegistration) =
        registration.also { eventRegistrations.add(it) }
}
