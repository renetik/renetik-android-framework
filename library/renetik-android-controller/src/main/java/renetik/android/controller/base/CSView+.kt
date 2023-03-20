package renetik.android.controller.base

import android.hardware.SensorManager
import android.view.OrientationEventListener
import android.view.View
import renetik.android.core.extensions.content.CSDisplayOrientation
import renetik.android.core.extensions.content.orientation
import renetik.android.core.kotlin.primitives.isTrue
import renetik.android.event.property.CSActionInterface
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.cancel
import renetik.android.event.registration.register
import renetik.android.event.registration.start
import renetik.android.ui.extensions.afterGlobalLayout
import renetik.android.ui.extensions.view.alphaToDisabled
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.onLongClick


fun <T : CSView<*>> T.reusable() = apply { lifecycleStopOnRemoveFromParentView = false }

fun CSView<*>.inflateView(layoutId: Int) = inflate<View>(layoutId)

fun <Type : CSView<*>> Type.disabledIf(condition: Boolean) = apply { isEnabled = !condition }

fun <T : CSView<*>> T.onClick(action: CSActionInterface): T =
    apply { contentView.onClick(action) }

fun <T : CSView<*>> T.onClick(function: (view: T) -> Unit): T =
    apply { contentView.onClick { function(this) } }

fun <T : CSView<*>> T.onLongClick(function: (view: T) -> Unit): T =
    apply { contentView.onLongClick { function(this) } }

fun <Type : CSView<*>> Type.disabledByAlpha(condition: Boolean = true, disable: Boolean = true) {
    if (disable) disabledIf(condition)
    view.alphaToDisabled(condition)
}

fun CSView<*>.onOrientationChange(
    function: (CSRegistration, CSDisplayOrientation) -> Unit
): CSRegistration {
    lateinit var registration: CSRegistration
    registration = onOrientationChange { orientation ->
        function(registration, orientation)
    }
    return registration
}

fun CSView<*>.onOrientationChange(
    function: (CSDisplayOrientation) -> Unit
): CSRegistration {
    var currentOrientation = orientation
    var afterGlobalLayoutRegistration: CSRegistration? = null
    val listener = object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
        override fun onOrientationChanged(orientation: Int) {
            if (afterGlobalLayoutRegistration?.isActive.isTrue)
                cancel(afterGlobalLayoutRegistration)
            afterGlobalLayoutRegistration = afterGlobalLayout {
                if (this@onOrientationChange.orientation != currentOrientation) {
                    currentOrientation = this@onOrientationChange.orientation
                    function(this@onOrientationChange.orientation)
                }
            }
        }
    }
    return register(
        CSRegistration.CSRegistration(onResume = { listener.enable() },
            onPause = { listener.disable() }).start()
    )
}