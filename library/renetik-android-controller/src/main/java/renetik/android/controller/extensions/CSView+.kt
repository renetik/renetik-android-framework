package renetik.android.controller.extensions

import android.hardware.SensorManager
import android.view.OrientationEventListener
import androidx.core.view.doOnAttach
import kotlinx.coroutines.delay
import renetik.android.controller.base.CSView
import renetik.android.core.extensions.content.CSDisplayOrientation
import renetik.android.core.extensions.content.orientation
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLog.logWarn
import renetik.android.event.CSEvent
import renetik.android.event.property.CSProperty
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.CSRegistration.Companion.CSRegistration
import renetik.android.event.registration.launch
import renetik.android.event.registration.plus
import renetik.android.event.registration.start
import renetik.android.ui.extensions.view.alphaToDisabled
import renetik.android.ui.extensions.view.clearLongClick
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.onLongClick

inline fun <reified Type : Any> CSView<*>.find(): Type? {
    var type: Type?
    var parent: CSView<*>? = this
    do {
        type = parent as? Type
        parent = parent?.parentView as? CSView<*>
    } while (type == null && parent != null)
    return type
}

fun <T : CSView<*>> T.reusable() = apply { lifecycleStopOnRemoveFromParentView = false }

fun <Type : CSView<*>> Type.disabledIf(condition: Boolean) = apply { isEnabled = !condition }

fun <T : CSView<*>> T.onClick(action: CSProperty<Boolean>): T =
    apply { contentView.onClick(action) }

fun <T : CSView<*>> T.onClick(action: CSEvent<Unit>): T =
    apply { contentView.onClick(action) }

fun <T : CSView<*>> T.onClick(function: (view: T) -> Unit): T =
    apply { contentView.onClick { function(this) } }

fun <T : CSView<*>> T.onLongClick(function: (view: T) -> Unit): T =
    apply { contentView.onLongClick { function(this) } }

fun <T : CSView<*>> T.clearLongClick() =
    apply { contentView.clearLongClick() }

fun <Type : CSView<*>> Type.disabledByAlpha(condition: Boolean = true, disable: Boolean = true) {
    if (disable) disabledIf(condition)
    view.alphaToDisabled(condition)
}

fun CSView<*>.onSensorOrientationChange(
    function: (CSRegistration?, CSDisplayOrientation) -> Unit
): CSRegistration? {
    var registration: CSRegistration? = null
    registration = onSensorOrientationChange { orientation ->
        function(registration, orientation)
    }
    return registration
}

fun CSView<*>.onSensorOrientationChange(
    function: (CSDisplayOrientation) -> Unit
): CSRegistration? {
    var currentOrientation = orientation
    val listener = object : OrientationEventListener(this, SensorManager.SENSOR_DELAY_NORMAL) {
        override fun onOrientationChanged(orientation: Int) {
            launch("onOrientationChange") {
                delay(50)
                if (this@onSensorOrientationChange.orientation != currentOrientation) {
                    currentOrientation = this@onSensorOrientationChange.orientation
                    function(this@onSensorOrientationChange.orientation)
                }
            }
        }
    }
    if (!listener.canDetectOrientation()) {
        logInfo { "Cannot detect orientation" }
        return null
    }
    var sensorUsable = true
    return this + CSRegistration(onResume = {
        if (sensorUsable) view.doOnAttach {
            runCatching { listener.enable() }.onFailure {
                sensorUsable = false
                logWarn(it) { "Failed to enable OrientationEventListener — disabling sensor use" }
                runCatching { listener.disable() }
            }
        }
    }, onPause = {
        if (sensorUsable) runCatching { listener.disable() }
            .onFailure { logWarn(it) { "Failed to disable listener" } }
    }).start()
}