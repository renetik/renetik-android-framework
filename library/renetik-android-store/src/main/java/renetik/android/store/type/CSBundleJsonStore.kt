package renetik.android.store.type

import android.os.Bundle
import renetik.android.core.kotlin.primitives.isFalse

class CSBundleJsonStore(
    private val bundle: Bundle = Bundle(),
    val key: String = "store",
    private val isPretty: Boolean = false
) : CSJsonObjectStore() {

    override val data: MutableMap<String, Any?> = mutableMapOf()

    init {
        loadJson(bundle.getString(key, "{}"))
    }

    override fun onChanged() {
        super.onChanged()
        isOperation.isFalse { bundle.putString(key, toJson(isPretty)) }
    }
}