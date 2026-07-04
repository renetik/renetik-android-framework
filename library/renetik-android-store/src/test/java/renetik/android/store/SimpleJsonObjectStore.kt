package renetik.android.store

import renetik.android.store.extensions.dataLateStringProperty
import renetik.android.store.extensions.dataNullStringProperty
import renetik.android.store.extensions.dataProperty
import renetik.android.store.type.CSJsonObjectStore

class SimpleJsonObjectStore(
    string: String? = null,
    nullString: String? = null,
    lateString: String? = null
) : CSJsonObjectStore() {

    var string: String by dataProperty("stringId", "defaultString")
    var nullString: String? by dataNullStringProperty("nullStringId")
    var lateString: String by dataLateStringProperty("lateStringId")

    init {
        string?.let { this.string = it }
        nullString?.let { this.nullString = it }
        lateString?.let { this.lateString = it }
    }
}

