package renetik.android.core.lang

object CSLeakCanary : CSLeakCanaryInterface {
    private var current: CSLeakCanaryInterface = object : CSLeakCanaryInterface {}

    fun install(implementation: CSLeakCanaryInterface) {
        current = implementation
    }

    override val isEnabled get() = current.isEnabled
    override fun enable() = current.enable()
    override fun disable() = current.disable()
    override fun Any.expectWeaklyReachable(description: () -> String) =
        with(current) { this@expectWeaklyReachable.expectWeaklyReachable(description) }
}
