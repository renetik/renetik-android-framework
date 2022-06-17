package renetik.android.core.lang

interface CSValue<T> {
    val value: T

    companion object {
        fun <T> value(value: T) = object : CSSynchronizedValue<T> {
            override val value: T = value
        }
    }
}

interface CSSynchronizedValue<T> : CSValue<T>