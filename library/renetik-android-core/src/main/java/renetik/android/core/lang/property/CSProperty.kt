package renetik.android.core.lang.property

import renetik.android.core.lang.CSValue

interface CSProperty<T> : CSValue<T> {
    override var value: T
}