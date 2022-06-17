package renetik.android.core.kotlin.reflect

import renetik.android.core.lang.catchAllWarnReturnNull
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.createInstance() = catchAllWarnReturnNull {
    java.getDeclaredConstructor().run {
        isAccessible = true
        newInstance()
    }
}