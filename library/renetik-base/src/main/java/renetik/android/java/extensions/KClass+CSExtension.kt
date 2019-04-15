package renetik.android.java.extensions

import renetik.android.java.common.tryAndWarn
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.createInstance() = tryAndWarn {
    java.getDeclaredConstructor().run {
        isAccessible = true
        newInstance()
    }
}