package renetik.android.framework.store

import renetik.android.framework.event.property.CSEventPropertyBase

class CSStoreStringEventProperty(
    private var store: CSStoreInterface, val key: String, val default: String,
    onChange: ((value: String) -> Unit)?) : CSEventPropertyBase<String>(onChange), CharSequence {

    override var _value: String
        get() = store.getString(key, default)
        set(value) = store.save(key, value)

    fun store(store: CSStoreInterface) = apply {
        this.store = store
        reload()
    }

    fun reload() = value(store.getString(key, default))

    //CharSequence
    override val length get() = value.length
    override fun get(index: Int) = value[index]
    override fun subSequence(startIndex: Int, endIndex: Int) =
        value.subSequence(startIndex, endIndex)
}