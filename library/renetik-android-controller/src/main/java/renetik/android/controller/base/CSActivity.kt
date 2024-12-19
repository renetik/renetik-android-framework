package renetik.android.controller.base

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.lang.variable.CSVariable
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.destruct
import renetik.android.event.fire
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSRegistrationsMap
import renetik.android.ui.extensions.clearContentView
import renetik.android.ui.protocol.CSVisibility

abstract class CSActivity<ActivityView : CSActivityView<out ViewGroup>> : AppCompatActivity(),
    CSActivityViewInterface, CSVisibility {

    companion object {
        val activity get() = app.activity as? CSActivity<*>
    }

    //CSActivityViewInterface
    override val eventResume = event<Unit>()
    override val eventPause = event<Unit>()
    override val eventBack = event<CSVariable<Boolean>>()
    override fun activity(): CSActivity<ActivityView> = this

    val onActivityResult = event<CSActivityResult>()
    val onRequestPermissionsResult = event<CSRequestPermissionResult>()

    //CSVisibility
    override val isVisible = property(true)

    var activityView: ActivityView? = null
    val configuration = Configuration()

    //CSViewInterface
    override val view: View get() = window.decorView

    //CSHasContext
    override val context: Context get() = this

    abstract fun createView(): ActivityView

    override fun onCreate(state: Bundle?) {
        super.onCreate(state)
        configuration.setTo(resources.configuration)
        activityView = createView()
        setContentView(activityView!!.view)
    }

    var isRecreateView = false

    fun recreateView() {
        destroyActivityView()
        createActivityView()
    }


    fun destroyActivityView() {
        isRecreateView = true
        clearContentView()
        activityView!!.destruct()
        configuration.setTo(resources.configuration)
        activityView = null
    }

    private fun createActivityView() {
        activityView = createView()
        setContentView(activityView!!.view)
        activityView!!.onResume()
        isRecreateView = false
    }

    public override fun onResume() {
        eventResume.fire()
        super.onResume()
    }

    public override fun onPause() {
        eventPause.fire()
        super.onPause()
    }

    final override val registrations by lazy { CSRegistrationsMap(this) }
    final override val eventDestruct = event<Unit>()
    override val isDestructed: Boolean get() = isDestroyed
    override fun onDestruct() {
        super.onDestroy()
        registrations.cancel()
        eventDestruct.fire().clear()
        activityView = null
    }

    @SuppressLint("MissingSuperCall")
    public override fun onDestroy() {
        destruct()
    }

    @Deprecated("Deprecated in Java")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        onActivityResult.fire(CSActivityResult(requestCode, resultCode, data))
        @Suppress("DEPRECATION")
        super.onActivityResult(requestCode, resultCode, data)
    }

    @Suppress("DEPRECATION")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val goBack = property(true)
        eventBack.fire(goBack)
        if (goBack.value) super.onBackPressed()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        configuration.setTo(newConfig)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, results: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, results)
        onRequestPermissionsResult.fire(
            CSRequestPermissionResult(requestCode, permissions, results)
        )
    }
}
