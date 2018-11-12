package renetik.android.extensions

import renetik.android.java.collections.CSMap
import renetik.android.lang.CSLang.*
import kotlin.reflect.KClass

val YES = true
val NO = false

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

fun Any.invoke(methodName: String): Any {
    try {
        return javaClass.getMethod(methodName, null).invoke(this)
    } catch (e: Exception) {
        return INVOKE_FAILED
    }

}

fun Any.invoke(methodName: String, types: Array<Class<*>>, argument: Array<Any>): Any {
    try {
        return javaClass.getMethod(methodName, *types).invoke(this, *argument)
    } catch (e: Exception) {
        return INVOKE_FAILED
    }

}

fun <T> Any.invoke(methodName: String, parameterType: Class<T>, argument: T): Any {
    try {
        return javaClass.getMethod(methodName, parameterType).invoke(this, argument)
    } catch (e: Exception) {
        return INVOKE_FAILED
    }
}

fun invokePrivate(methodName: String, targetClass: Class<*>,
                      paramTypes: Array<Class<*>>,
                      arguments: Array<Any>): Any {
    try {
        val method = targetClass.getDeclaredMethod(methodName, *paramTypes)
        method.isAccessible = true
        return method.invoke(null, *arguments)
    } catch (e: Exception) {
        return INVOKE_FAILED
    }
}