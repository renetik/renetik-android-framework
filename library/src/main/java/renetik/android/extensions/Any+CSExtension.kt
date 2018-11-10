package renetik.android.extensions

import renetik.android.java.collections.CSMap
import renetik.android.lang.CSLang.*
import kotlin.reflect.KClass

fun Any.whenNull(value: CSMap<String, Any>?, function: () -> Unit): Boolean {
    if (value == null) {
        function()
        return YES
    }
    return NO
}

fun Any.whenNotNull(value: CSMap<String, Any>?, function: () -> Unit): Boolean {
    if (value != null) {
        function()
        return YES
    }
    return NO
}

fun <T, R> T.notNull(block: (T) -> R): R? {
    if (this != null) return block(this)
    return null
}

@Suppress("upper_bound_violated")
public val <T> KClass<T>.j: Class<T>
    get() = this.java

fun string(value: Any?) = value?.toString() ?: ""

fun string(separator: String, values: Iterable<*>): String {
    val text = textBuilder()
    for (value in values) if (set(value)) text.add(value).add(separator)
    if (!text.isEmpty) text.deleteLast(separator.length)
    return text.toString()
}