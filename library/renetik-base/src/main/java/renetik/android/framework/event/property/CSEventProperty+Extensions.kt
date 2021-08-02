package renetik.android.framework.event.property

import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue

fun CSEventPropertyImpl<Boolean>.onFalse(function: () -> Unit) =
    onChange { if (it.isFalse) function() }

fun CSEventPropertyImpl<Boolean>.onTrue(function: () -> Unit) =
    onChange { if (it.isTrue) function() }