package renetik.kotlin

import renetik.android.framework.lang.catchAllWarn
import renetik.android.framework.lang.catchAllWarnReturnNull
import renetik.java.lang.createInstance

const val INVOKE_FAILED = "invoke_failed"

fun <T> Any.privateField(name: String): T? = catchAllWarnReturnNull<T> {
    val field = this::class.java.getDeclaredField(name)
    field.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    return field.get(this) as T
}

fun <T> Any.setPrivateField(name: String, fieldValue: T) = catchAllWarn {
    val field = this::class.java.getDeclaredField(name)
    field.isAccessible = true
    field.set(this, fieldValue)
}

inline fun <reified ClassType : Any> ClassType.setPrivateField2(name: String, fieldValue: Any) = catchAllWarn {
    val field = ClassType::class.java.getDeclaredField(name)
    field.isAccessible = true
    field.set(this, fieldValue)
}

@Suppress("UNCHECKED_CAST")
fun <T> createInstance(className: String) =
    (Class.forName(className) as Class<T>).createInstance()

fun invokeFunction(type: Class<*>, name: String,
                   argumentTypes: Array<Class<*>>, arguments: Array<Any>
): Any? = try {
    type.getDeclaredMethod(name, *argumentTypes)
        .apply { isAccessible = true }.invoke(null, *arguments)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun Any.invokeFunction(name: String): Any? = try {
    javaClass.getMethod(name, null).invoke(this)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun Any.invokeFunction(name: String, argumentTypes: Array<Class<*>>,
                       arguments: Array<Any>): Any? = try {
    javaClass.getMethod(name, *argumentTypes).invoke(this, *arguments)
} catch (e: Exception) {
    INVOKE_FAILED
}

fun <T> Any.invokeFunction(name: String, argumentType: Class<T>, argument: T): Any? = try {
    javaClass.getMethod(name, argumentType).invoke(this, argument)
} catch (e: Exception) {
    INVOKE_FAILED
}