package renetik.android.framework.util

import renetik.android.framework.logging.CSLog.logInfo
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class CSSynchronizedProperty<T>(defaultValue: T) : ReadWriteProperty<Any, T> {

    companion object {
        fun <T> synchronize(defaultValue: T) = CSSynchronizedProperty(defaultValue)
    }

    private var backingField = defaultValue

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        return synchronized(this) { backingField }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        synchronized(this) { backingField = value }
    }

    fun pica(){
        logInfo("")
    }
}