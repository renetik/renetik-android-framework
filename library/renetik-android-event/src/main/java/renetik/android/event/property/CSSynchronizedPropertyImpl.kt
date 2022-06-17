package renetik.android.event.property

class CSSynchronizedPropertyImpl<T>(
    value: T,
    val onChange: ((value: T) -> Unit)? = null)
    : CSSynchronizedProperty<T> {

    @get:Synchronized
    @set:Synchronized
    override var value: T = value
        set(value) {
            field = value
            onChange?.invoke(value)
        }

    fun value(value: T) {
        this.value = value
    }
}