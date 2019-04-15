package renetik.android.client.okhttp3

import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import okhttp3.OkHttpClient
import okhttp3.Response
import renetik.android.client.request.CSResponse
import renetik.android.client.request.CSServerData
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndError
import renetik.android.json.parseJson
import renetik.android.logging.CSLog.logInfo
import java.io.IOException

const val INVALID_RESPONSE = "Invalid response from client"
const val APPLICATION_ERROR = "Application error or invalid data"

class CSOkHttpResponseListener<Data : CSServerData>(
        private val client: OkHttpClient, private val response: CSResponse<Data>)
    : OkHttpResponseAndStringRequestListener {

    override fun onResponse(http: Response, content: String) = onContent(content, response)

    override fun onError(error: ANError) = onError(response, error)

    @Suppress("unchecked_cast")
    private fun onContent(content: String, response: CSResponse<Data>) {
        logInfo("${response.url} $content")
        content.parseJson<MutableMap<String, Any?>>()?.let { response.data().load(it) }
                ?: onError(response, INVALID_RESPONSE, null)
        tryAndCatch({
            if (response.data().success) response.success()
            else onError(response, response.data().message, null)
        }) { exception -> onError(response, APPLICATION_ERROR, exception) }
    }

    private fun onError(response: CSResponse<*>, error: ANError) = onError(response, error.errorBody, error)

    private fun onError(response: CSResponse<*>, message: String?, exception: Throwable?) {
        invalidate(response.url!!)
        response.failed(exception, message)
    }

    private fun invalidate(url: String) = tryAndError(IOException::class) {
        client.cache().urls().apply { while (this.hasNext()) if (this.next().contains(url)) this.remove() }
    }
}