package renetik.android.client.okhttp3

import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import okhttp3.OkHttpClient
import okhttp3.Response
import renetik.android.client.request.CSProcess
import renetik.android.client.request.CSServerData
import renetik.android.java.common.tryAndCatch
import renetik.android.java.common.tryAndError
import renetik.android.logging.CSLog.logInfo
import java.io.IOException

const val APPLICATION_ERROR = "Application error or invalid data"

class CSOkHttpResponseListener<Data : CSServerData>(
    private val client: OkHttpClient, private val process: CSProcess<Data>)
    : OkHttpResponseAndStringRequestListener {

    override fun onResponse(http: Response, content: String) {
        logInfo("${process.url} ${http.code()}, ${http.message()}, $content")
        process.data!!.loadHttp(http.code(), http.message(), content)
        var success = false
        tryAndCatch({
            if (process.data!!.success) success = true
            else onResponseError(process, process.data!!.message, null)
        }) { exception -> onResponseError(process, APPLICATION_ERROR, exception) }
        if (success) process.success()
    }

    override fun onError(error: ANError) = onResponseError(process, error.errorBody, error)

    private fun onResponseError(process: CSProcess<*>, message: String?, exception: Throwable?) {
        invalidate(process.url!!)
        process.failed(exception, message)
    }

    private fun invalidate(url: String) = tryAndError(IOException::class) {
        client.cache().urls().apply {
            while (this.hasNext())
                if (this.next().contains(url)) this.remove()
        }
    }
}