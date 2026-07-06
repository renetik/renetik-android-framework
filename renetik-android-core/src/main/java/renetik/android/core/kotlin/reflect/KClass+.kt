package renetik.android.core.kotlin.reflect

import renetik.android.core.lang.catchAllWarnReturnNull
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

fun <T : Any> KClass<T>.createInstance(): T? = catchAllWarnReturnNull {
    createInstance()
}

fun <T : Any> KClass<T>.createInstance(param: Any) = catchAllWarnReturnNull {
    primaryConstructor?.call(param)
}

inline fun <reified T : Any> createInstance(): T = T::class.createInstance()