package cs.android.extensions

import cs.java.collections.CSMap
import cs.java.lang.CSLang.*
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