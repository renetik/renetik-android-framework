package renetik.android.controller.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils.loadAnimation
import androidx.appcompat.app.AppCompatActivity
import renetik.android.controller.R.anim.activity_recreate_fade_in
import renetik.android.controller.R.anim.activity_recreate_fade_out
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.destruct
import renetik.android.event.fire
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.ui.protocol.CSVisibility

abstract class CSActivity : AppCompatActivity(), CSActivityViewInterface, CSVisibility {

    companion object {
        val activity get() = app.activity as? CSActivity
    }

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
        destroyActivityView()
        createActivityView()
    }

    fun destroyActivityView(animation: Boolean = false) {
        isRecreateView = true
        if (animation)
            activityView!!.view.startAnimation(loadAnimation(this, activity_recreate_fade_out))
        activityView!!.destruct()
        configuration.updateFrom(resources.configuration)
        activityView = null
    }

    fun createActivityView(animation: Boolean = false) {
        activityView = createView()
        if (animation)
            activityView!!.view.startAnimation(loadAnimation(this, activity_recreate_fade_in))
        setContentView(activityView!!.view)
        activityView!!.onResume()
        isRecreateView = false
    }

    public override fun onStart() {
        onStart.fire()
        super.onStart()
    }

    public override fun onResume() {
        eventResume.fire()
        super.onResume()
    }

    public override fun onPause() {
        eventPause.fire()
        super.onPause()
    }

    public override fun onStop() {
        onStop.fire()
        super.onStop()
    }

    private val lazyRegistrations = lazy { CSRegistrationsMap(this) }
    final override val registrations by lazyRegistrations
    final override val eventDestruct = event<Unit>()
    override val isDestructed: Boolean get() = isDestroyed
    override fun onDestruct() {
        super.onDestroy()
        if (lazyRegistrations.isInitialized()) registrations.cancel()
        eventDestruct.fire().clear()
        activityView = null
    }

    @SuppressLint("MissingSuperCall")
    public override fun onDestroy() = onDestruct()

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.fire(CSActivityResult(requestCode, resultCode, data))
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        onKeyDown.fire(CSOnKeyDownResult(keyCode, event))
        return super.onKeyDown(keyCode, event)
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        onSaveInstanceState.fire(outState)
    }

    public override fun onNewIntent(intent: Intent) {
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

    public override fun onUserLeaveHint() {
        onUserLeaveHint.fire()
        super.onUserLeaveHint()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onConfigurationChanged.fire(newConfig)
        if (configuration.orientation != newConfig.orientation)
            onOrientationChanged.fire(newConfig)
        configuration.updateFrom(newConfig)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        onRequestPermissionsResult.fire(
            CSRequestPermissionResult(requestCode, permissions, results)
        )
    }

    override fun onLowMemory() {
        onLowMemory.fire()
        super.onLowMemory()
    }
}
