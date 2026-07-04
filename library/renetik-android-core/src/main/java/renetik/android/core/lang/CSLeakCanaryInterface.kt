package renetik.android.core.lang

interface CSLeakCanaryInterface {
    fun Any.expectWeaklyReachable(description: () -> String) = Unit
    val isEnabled: Boolean get() = false
    fun enable() = Unit
    fun disable() = Unit
}