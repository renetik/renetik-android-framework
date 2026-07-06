package renetik.android.preset.property

import renetik.android.event.property.CSProperty
import renetik.android.event.property.connect
import renetik.android.event.registration.CSRegistration

fun <T> CSPresetProperty<T>.followPresetIf(
    property: CSProperty<Boolean>
): CSRegistration = isFollowPreset.connect(property)
