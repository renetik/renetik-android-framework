package renetik.android.store.property

import android.content.Context
import renetik.android.store.CSStore.Companion.store

object CSStoreEventPropertyFunctions {

    fun Context.property(key: String, default: String,
                         onApply: ((value: String) -> Unit)? = null) =
        store.property(key, default, onApply)

    fun Context.property(key: String, default: Float,
                         onApply: ((value: Float) -> Unit)? = null) =
        store.property(key, default, onApply)

    fun Context.property(key: String, default: Long,
                         onApply: ((value: Long) -> Unit)? = null) =
        store.property(key, default, onApply)

    fun Context.property(key: String, default: Int,
                         onApply: ((value: Int) -> Unit)? = null) =
        store.property(key, default, onApply)

    fun Context.property(
        key: String, default: Boolean,
        onChange: ((value: Boolean) -> Unit)? = null
    ) = store.property(key, default, onChange)

    fun <T> Context.property(
        key: String, values: List<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = store.property(key, values, default, onApply)

    fun <T> Context.property(
        key: String, values: List<T>, defaultIndex: Int,
        onApply: ((value: T) -> Unit)? = null
    ) = property(key, values, values[defaultIndex], onApply)

    fun <T> Context.property(
        key: String, values: Array<T>, default: T,
        onApply: ((value: T) -> Unit)? = null
    ) = store.property(key, values, default, onApply)

    fun <T> Context.property(
        key: String, values: List<T>,
        onApply: ((value: T) -> Unit)? = null
    ) = store.property(key, values, onApply)

    fun <T> Context.property(
        key: String, values: Array<T>, defaultIndex: Int = 0,
        onApply: ((value: T) -> Unit)? = null
    ) = store.property(key, values, defaultIndex, onApply)
}