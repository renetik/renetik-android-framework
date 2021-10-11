package renetik.android.framework.lang.property

import renetik.android.framework.lang.CSValue

interface CSProperty<T> : CSValue<T> {
    override var value: T
}