package renetik.android.network.okhttp3

import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener
import okhttp3.OkHttpClient
import okhttp3.Response
import renetik.android.core.lang.catchError
import renetik.android.core.lang.catchErrorReturn
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.core.logging.CSLogMessage.Companion.message
import renetik.android.network.data.CSHttpResponseData
import renetik.android.network.process.CSHttpProcess
import java.io.IOException

const val APPLICATION_ERROR = "Application error or invalid data"

class CSOkHttpResponseListener<Data : CSHttpResponseData>(
    private val client: OkHttpClient, private val process: CSHttpProcess<Data>)
    : OkHttpResponseAndStringRequestListener {

    override fun onResponse(http: Response, content: String) {
        logInfo { message("${process.url} ${http.code}, ${http.message}, $content") }
        process.data!!.onHttpResponse(http.code, http.message, content)
        catchErrorReturn<Unit, Exception>({
            if (process.data!!.success) process.success()
            else onResponseError(process.data!!.message, null)
        }) { exception -> onResponseError(APPLICATION_ERROR, exception) }
    }

    override fun onError(error: ANError) {
        process.data!!.onHttpResponse(error.errorCode, error.errorBody ?: error.errorDetail, null)
        onResponseError(error.errorBody ?: error.errorDetail, error)
    }

    private fun onResponseError(message: String?, exception: Throwable?) {
        invalidate(process.url!!)
        process.failed(exception, message)
    }

    private fun invalidate(url: String) = catchError<IOException> {
        client.cache?.urls()?.apply {
            while (this.hasNext()) if (this.next().contains(url)) this.remove()
        }
    }
}