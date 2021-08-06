package renetik.android.framework.event.property

import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue

fun CSEventProperty<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventProperty<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }