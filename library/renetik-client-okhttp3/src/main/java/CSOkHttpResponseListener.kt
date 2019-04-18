package renetik.android.client.okhttp3

import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import okhttp3.OkHttpClient
import okhttp3.Response
import renetik.android.client.request.CSProcess
import renetik.android.client.request.CSServerData
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndError
import renetik.android.json.parseJson
import renetik.android.logging.CSLog.logInfo
import java.io.IOException

const val INVALID_RESPONSE = "Invalid response from client"
const val APPLICATION_ERROR = "Application error or invalid data"

class CSOkHttpResponseListener<Data : CSServerData>(
        private val client: OkHttpClient, private val process: CSProcess<Data>)
    : OkHttpResponseAndStringRequestListener {

    override fun onResponse(http: Response, content: String) = onContent(content, process)

    override fun onError(error: ANError) = onError(process, error)

    @Suppress("unchecked_cast")
    private fun onContent(content: String, process: CSProcess<Data>) {
        logInfo("${process.url} $content")
        content.parseJson<MutableMap<String, Any?>>()?.let { process.data().load(it) }
                ?: onError(process, INVALID_RESPONSE, null)
        tryAndCatch({
            if (process.data().success) process.success()
            else onError(process, process.data().message, null)
        }) { exception -> onError(process, APPLICATION_ERROR, exception) }
    }

    private fun onError(process: CSProcess<*>, error: ANError) = onError(process, error.errorBody, error)

    private fun onError(process: CSProcess<*>, message: String?, exception: Throwable?) {
        invalidate(process.url!!)
        process.failed(exception, message)
    }

    private fun invalidate(url: String) = tryAndError(IOException::class) {
        client.cache().urls().apply { while (this.hasNext()) if (this.next().contains(url)) this.remove() }
    }
}