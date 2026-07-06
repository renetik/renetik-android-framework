package renetik.android.core.java.lang

import renetik.android.core.lang.catchAllWarnReturnNull

@Suppress("DEPRECATION")
fun <T> Class<T>.createInstance(): T? = catchAllWarnReturnNull { this.newInstance() }

@Suppress("UNCHECKED_CAST")
fun <T> Class<T>.createInstance(param: Any): T? = catchAllWarnReturnNull {
    constructors.firstOrNull {
        it.parameterTypes.size == 1
    }?.also {
        it.isAccessible = true
    }?.newInstance(param) as? T
}