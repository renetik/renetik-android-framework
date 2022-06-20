package renetik.android.network.data

import renetik.android.json.parseJsonList

open class CSServerListData : renetik.android.json.CSJsonArray(), CSHttpResponseData {

    private var _message = ""

    override fun onHttpResponse(code: Int, message: String, content: String?) {
        content?.parseJsonList()?.let {
            load(it)
        } ?: let {
            _message = PARSING_FAILED
        }
    }

    override val success: Boolean get() = message != PARSING_FAILED
    override val message: String? get() = _message
}