package renetik.android.store

import renetik.android.core.lang.ArgFun
import renetik.android.event.lifecycle.CSHasDestruct
import renetik.android.event.lifecycle.parent
import renetik.android.store.CSStore
import renetik.android.store.property.listenLoad
import renetik.android.store.property.listenLoadOnce
import renetik.android.store.property.value.CSBooleanValueStoreProperty
import renetik.android.store.property.value.CSDoubleValueStoreProperty
import renetik.android.store.property.value.CSFloatValueStoreProperty
import renetik.android.store.property.value.CSIntValueStoreProperty
import renetik.android.store.property.value.CSLongValueStoreProperty
import renetik.android.store.property.value.CSStringListValueStoreProperty
import renetik.android.store.property.value.CSStringValueStoreProperty

fun CSStore.property(
    key: String, default: String,
    onChange: ArgFun<String>? = null,
) = CSStringValueStoreProperty(this, key, default, onChange)

fun CSStore.property(
    key: String, default: () -> String,
    onChange: ArgFun<String>? = null,
) = CSStringValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: String,
    onChange: ArgFun<String>? = null,
): CSStringValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: String,
    onChange: ArgFun<String>? = null,
) = CSStringValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: Boolean,
    onChange: ArgFun<Boolean>? = null,
) = CSBooleanValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: Boolean,
    onChange: ArgFun<Boolean>? = null,
): CSBooleanValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: Boolean,
    onChange: ArgFun<Boolean>? = null,
) = CSBooleanValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: Int,
    onChange: ArgFun<Int>? = null
) = CSIntValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: Int,
    onChange: ArgFun<Int>? = null
): CSIntValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: Int,
    onChange: ArgFun<Int>? = null,
) = CSIntValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: ()->Int,
    onChange: ArgFun<Int>? = null,
) = CSIntValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: Double,
    onChange: ArgFun<Double>? = null,
) = CSDoubleValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: Double,
    onChange: ArgFun<Double>? = null,
): CSDoubleValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: Double,
    onChange: ArgFun<Double>? = null,
) = CSDoubleValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: Float,
    onChange: ArgFun<Float>? = null,
) = CSFloatValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: Float,
    onChange: ArgFun<Float>? = null,
): CSFloatValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: Float,
    onChange: ArgFun<Float>? = null,
) = CSFloatValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: Long,
    onChange: ArgFun<Long>? = null,
) = CSLongValueStoreProperty(this, key, default, onChange)

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: Long,
    onChange: ArgFun<Long>? = null,
) = CSLongValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()

fun CSStore.property(
    key: String, default: List<String>, onChange: ArgFun<List<String>>? = null
) = CSStringListValueStoreProperty(this, key, default, onChange)

fun CSStore.dataProperty(
    key: String, default: List<String>, onChange: ArgFun<List<String>>? = null
): CSStringListValueStoreProperty = property(key, default, onChange).listenLoadOnce()

fun CSStore.property(
    parent: CSHasDestruct,
    key: String, default: List<String>, onChange: ArgFun<List<String>>? = null
) = CSStringListValueStoreProperty(this, key, default, onChange)
    .parent(parent).listenLoad()