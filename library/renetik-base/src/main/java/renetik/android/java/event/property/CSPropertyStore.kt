package renetik.android.java.event.property

interface CSPropertyStoreInterface {
    fun property(
        key: String, default: String, onChange: ((value: String) -> Unit)? = null
    ): CSEventProperty<String>

    fun property(
        key: String, default: Boolean, onApply: ((value: Boolean) -> Unit)? = null
    ): CSEventProperty<Boolean>

    fun property(
        key: String, default: Int, onApply: ((value: Int) -> Unit)? = null
    ): CSEventProperty<Int>

    fun property(
        key: String, default: Double,
        onApply: ((value: Double) -> Unit)? = null
    ): CSEventProperty<Double>

    fun property(
        key: String, default: Float,
        onApply: ((value: Float) -> Unit)? = null
    ): CSEventProperty<Float>

    fun <T> property(
        key: String, values: List<T>, default: T, onApply: ((value: T) -> Unit)? = null
    ): CSListStoreEventProperty<T>

    fun <T> property(
        key: String, values: Array<T>, default: T, onApply: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), default, onApply)

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = property(key, values, values[defaultIndex], onApply)

    fun <T> property(
        key: String, values: Array<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = property(key, values.asList(), values[defaultIndex], onApply)
}