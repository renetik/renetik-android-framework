package renetik.android.viewbase

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import renetik.android.java.event.event
import renetik.android.java.event.fire
import renetik.android.java.lang.CSValue
import renetik.android.lang.CSLang.iterate
import renetik.android.lang.tryAndWarn
import renetik.android.viewbase.menu.CSOnMenu
import renetik.android.viewbase.menu.CSOnMenuItem
import renetik.android.viewbase.menu.GeneratedMenuItems

abstract class CSActivity : AppCompatActivity(), CSViewControllerParent {

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
    override fun activity() = this

    private var controller: CSViewController<*>? = null
    abstract fun createController(): CSViewController<*>

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
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
        controller!!.view.let { onDestroyUnbindDrawables(it) }
        onDestroy.fire()
        controller = null
        System.gc()
    }

    private fun onDestroyUnbindDrawables(view: View) {
        view.background?.callback = null
        if (view is ViewGroup) {
            for (index in iterate(view.childCount)) onDestroyUnbindDrawables(view.getChildAt(index))
            tryAndWarn { view.removeAllViews() }
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
        val goBack = CSValue(true)
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val onMenuItem = CSOnMenuItem(item)
        onOptionsItemSelected.fire(onMenuItem)
        return onMenuItem.isConsumed
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        onRequestPermissionsResult.fire(CSRequestPermissionResult(requestCode, permissions, results))
    }

    override fun onLowMemory() {
        onLowMemory.fire()
        super.onLowMemory()
    }
}
