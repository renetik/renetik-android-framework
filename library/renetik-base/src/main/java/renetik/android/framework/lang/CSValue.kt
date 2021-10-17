package renetik.android.framework.lang

import renetik.android.framework.lang.property.CSProperty
import renetik.android.primitives.Empty
import renetik.android.primitives.isFalse

interface CSValue<T> {
    val value: T

    companion object {
        fun <T> value(value: T) = object : CSValue<T> {
            override val value: T = value
        }
    }
}