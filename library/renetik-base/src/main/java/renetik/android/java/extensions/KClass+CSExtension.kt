package renetik.android.java.extensions

import renetik.android.java.common.catchAllWarnReturnNull
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.createInstance() = catchAllWarnReturnNull {
    java.getDeclaredConstructor().run {
        isAccessible = true
        newInstance()
    }
}