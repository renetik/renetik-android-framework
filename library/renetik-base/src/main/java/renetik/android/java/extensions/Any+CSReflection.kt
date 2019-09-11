package renetik.android.java.extensions

import kotlin.reflect.KClass

const val INVOKE_FAILED = "invoke_failed"

@Suppress("UNCHECKED_CAST")
fun <T> createInstance(className: String) =
    (Class.forName(className) as Class<T>).createInstance()

fun invoke(
    function: String, type: Class<*>,
    paramTypes: Array<Class<*>>, arguments: Array<Any>
): Any? = try {

    type.getDeclaredMethod(function, *paramTypes)
        .apply { isAccessible = true }.invoke(null, *arguments)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun Any.invoke(methodName: String): Any? = try {
    javaClass.getMethod(methodName, null).invoke(this)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun Any.invoke(methodName: String, types: Array<Class<*>>, argument: Array<Any>): Any? = try {
    javaClass.getMethod(methodName, *types).invoke(this, *argument)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun <T> Any.invoke(methodName: String, parameterType: Class<T>, argument: T): Any? = try {
    javaClass.getMethod(methodName, parameterType).invoke(this, argument)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun Any.isInstanceOf(type: KClass<*>) = type.java.isInstance(this)