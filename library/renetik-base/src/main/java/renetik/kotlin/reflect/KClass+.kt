package renetik.kotlin.reflect

import renetik.android.framework.lang.catchAllWarnReturnNull
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.createInstance() = catchAllWarnReturnNull {
    java.getDeclaredConstructor().run {
        isAccessible = true
        newInstance()
    }
}