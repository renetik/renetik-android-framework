package renetik.android.viewbase

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import renetik.android.java.lang.CSValue
import renetik.android.lang.CSLang.warn
import renetik.android.viewbase.menu.CSOnMenu
import renetik.android.viewbase.menu.CSOnMenuItem
import renetik.android.viewbase.menu.GeneratedMenuItems

abstract class CSActivity : AppCompatActivity() {

    private var controller: CSViewController<*>? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        controller?.onActivityResult(CSActivityResult(requestCode, resultCode, data))
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val onKeyDown = CSOnKeyDownResult(keyCode, event)
        controller?.onKeyDown(onKeyDown)
        return super.onKeyDown(keyCode, event)
    }

    override fun onLowMemory() {
        controller?.onLowMemory()
        super.onLowMemory()
    }

    override fun onPause() {
        controller?.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller?.onSaveInstanceState(outState)
    }

    override fun onNewIntent(intent: Intent) {
        controller?.onNewIntent(intent)
        super.onNewIntent(intent)
    }

    override fun onResume() {
        controller?.onResume()
        super.onResume()
    }

    override fun onStart() {
        controller?.onStart()
        super.onStart()
    }

    public override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        controller = createController()
//        controller?.view//TODO its ok removed ?
        controller?.onBeforeCreate(state)
        setContentView(controller?.view)
        controller?.onCreate(state)
    }

    override fun onStop() {
        controller?.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        controller?.view?.let { onDestroyUnbindDrawables(it) }
        controller?.onDestroy()
        controller = null
        System.gc()
    }

    private fun onDestroyUnbindDrawables(view: View) {
        if (view.background != null) view.background.callback = null
        if (view is ViewGroup) {
            for (index in 0 until view.childCount) onDestroyUnbindDrawables(view.getChildAt(index))
            try {
                view.removeAllViews()
            } catch (e: Exception) {
                warn(e)
            }
        }
    }

    override fun onBackPressed() {
        val goBack = CSValue(true)
        controller?.onBack(goBack)
        if (goBack.value) super.onBackPressed()
    }

    override fun onUserLeaveHint() {
        controller?.onUserLeaveHint()
        super.onUserLeaveHint()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val onMenu = CSOnMenu(this, menu)
        controller?.onCreateOptionsMenu(onMenu)
        return onMenu.showMenu.value
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.removeGroup(GeneratedMenuItems)
        val onMenu = CSOnMenu(this, menu)
        controller?.onPrepareOptionsMenuImpl(onMenu)
        return onMenu.showMenu.value
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        controller?.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val onMenuItem = CSOnMenuItem(item)
        controller?.onOptionsItemSelectedImpl(onMenuItem)
        return onMenuItem.consumed.value
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        controller?.onRequestPermissionsResult(CSRequestPermissionResult(requestCode, permissions, results))
    }

    abstract fun createController(): CSViewController<*>
}
