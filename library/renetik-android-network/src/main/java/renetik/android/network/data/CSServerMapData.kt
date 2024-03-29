package renetik.android.network.data

import renetik.android.json.parseJsonMap
import renetik.android.store.type.CSJsonObjectStore

open class CSServerMapData : CSJsonObjectStore(), CSHttpResponseData {
    var code: Int? = null

    override fun onHttpResponse(code: Int, message: String, content: String?) {
        content?.parseJsonMap()?.let {
            load(it)
        } ?: set("message", PARSING_FAILED)
    }

    override val success: Boolean get() = getBoolean("success") ?: false
    override val message: String? get() = getString("message")
}