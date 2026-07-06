package renetik.android.core.lang

fun CSLeakCanaryInterface.active(isActive: Boolean) =
    if (isActive) enable() else disable()