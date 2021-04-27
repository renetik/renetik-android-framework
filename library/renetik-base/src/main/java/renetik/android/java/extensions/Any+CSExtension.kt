package renetik.android.java.extensions

import renetik.android.task.CSDoLaterObject
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty


fun <T : Any> T.later(delayMilliseconds: Int = 0, function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later(delayMilliseconds) { function(this) }
}

fun <T : Any> T.later(function: (T).() -> Unit): T = apply {
    CSDoLaterObject.later { function(this) }
}

// by observing(...
fun <T> observing(value: T, willSet: (T) -> Unit = { }, didSet: (T) -> Unit = { }) =
    object : ObservableProperty<T>(value) {
        override fun beforeChange(property: KProperty<*>, oldValue: T, newValue: T): Boolean {
            willSet(newValue); return true
        }

        override fun afterChange(property: KProperty<*>, oldValue: T, newValue: T) =
            didSet(oldValue)
    }