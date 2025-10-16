package renetik.android.controller.base

import android.view.ViewGroup
import renetik.android.core.base.CSApplication.Companion.app
import renetik.android.core.lang.variable.CSVariable
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.fire
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.ui.protocol.CSVisibility

abstract class CSViewActivity<ActivityView : CSActivityView<out ViewGroup>>
    : CSActivity(), CSActivityViewInterface, CSVisibility {
    companion object {
        val activity get() = app.activity as? CSViewActivity<*>
    }

    //CSActivityViewInterface
    override val eventResume = event<Unit>()
    override val eventPause = event<Unit>()
    override val eventBack = event<CSVariable<Boolean>>()
    override fun activity(): CSViewActivity<ActivityView> = this
    abstract fun createView(): ActivityView
    val activityView: ActivityView by lazy { createView() }

    fun setContentView() = setContentView(activityView.view)

    public override fun onResume() {
        eventResume.fire()
        super.onResume()
        logInfo()
    }

    public override fun onPause() {
        eventPause.fire()
        super.onPause()
        logInfo()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val goBack = property(true)
        eventBack.fire(goBack)
        if (goBack.value) super.onBackPressed()
        logInfo()
    }
}
