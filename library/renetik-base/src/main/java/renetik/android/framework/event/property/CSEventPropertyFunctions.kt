package renetik.android.framework.event.property

import renetik.android.framework.CSApplication.Companion.application

//TODO move to CSEventProperty Companion
object CSEventPropertyFunctions {

    fun <T> property(value: T, onApply: ((value: T) -> Unit)? = null) =
        CSEventPropertyImpl(value, onApply)

    fun <T> synchronizedProperty(value: T, onApply: ((value: T) -> Unit)? = null) =
        CSSynchronizedEventPropertyImpl(value, onApply)

    fun <T> lateProperty(onApply: ((value: T) -> Unit)? = null) =
        CSLateEventProperty(onApply)

    fun <T> nullableProperty(onApply: ((value: T?) -> Unit)? = null): CSEventProperty<T?> =
        CSEventPropertyImpl(null, onApply)

    fun property(key: String, default: String,
                 onApply: ((value: String) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(key: String, default: Float,
                 onApply: ((value: Float) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(key: String, default: Int,
                 onApply: ((value: Int) -> Unit)? = null) =
        application.store.property(key, default, onApply)

    fun property(
        key: String, default: Boolean,
        onApply: ((value: Boolean) -> Unit)? = null
    ) = application.store.property(key, default, onApply)

    fun <T> property(
        key: String, values: List<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, default, onApply)

    fun <T> property(
        key: String, values: List<T>, defaultIndex: Int,
        onApply: ((value: T) -> Unit)? = null
    ) = property(key, values, values[defaultIndex], onApply)

    fun <T> property(
        key: String, values: Array<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, default, onApply)

    fun <T> property(
        key: String, values: List<T>,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, onApply)

    fun <T> property(
        key: String, values: Array<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = application.store.property(key, values, defaultIndex, onApply)
}