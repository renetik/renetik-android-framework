package renetik.android.store

import renetik.android.store.extensions.dataProperty
import renetik.android.store.type.CSJsonObjectStore

class StoreTypesTestData : CSJsonObjectStore() {
    var string: String by dataProperty("key1", default = "initial")
    var int: Int by dataProperty("key2", default = 5)
    val jsonObject: SimpleJsonObjectStore by dataProperty("key3")
}