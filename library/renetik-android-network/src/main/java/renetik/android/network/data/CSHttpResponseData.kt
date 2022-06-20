package renetik.android.network.data

interface CSHttpResponseData {
    fun onHttpResponse(code: Int, message: String, content: String?)
    val success: Boolean
    val message: String?
}

const val PARSING_FAILED = "Parsing data as json failed"