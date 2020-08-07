package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import renetik.android.base.CSApplicationObject.application
import renetik.android.client.request.CSOperation
import renetik.android.client.request.CSProcess
import renetik.android.client.request.CSServerData
import renetik.android.extensions.isNetworkConnected
import renetik.android.java.common.CSTimeConstants.Minute
import renetik.android.java.extensions.notNull
import renetik.android.java.extensions.primitives.isFalse
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.toJsonObject
import renetik.android.logging.CSLog
import java.io.File
import java.util.concurrent.TimeUnit

fun <ServerDataType : CSServerData> CSOkHttpClient.upload(service: String,
                                                          file: File,
                                                          data: ServerDataType) =
    CSProcess("$url/$service", data).also { process ->
        val request = AndroidNetworking.upload(process.url).addMultipartFile("file", file).build()
        CSLog.logInfo("upload ${request.url} $file")
        request.setUploadProgressListener { uploaded, total ->
            process.progress = total / uploaded
        }.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
    }

fun CSOkHttpClient.get(url: String, params: Map<String, String> = emptyMap()) =
    get(null, url, params)

fun CSOkHttpClient.get(
    operation: CSOperation<CSServerData>? = null, url: String,
    params: Map<String, String> = emptyMap()
) = get(operation, url, CSServerData(), params)

fun <ServerDataType : CSServerData> CSOkHttpClient.get(
    operation: CSOperation<*>?, service: String, data: ServerDataType,
    params: Map<String, String> = emptyMap()
) = CSProcess("$url/$service", data).also { process ->
    val builder = AndroidNetworking.get(process.url!!).addQueryParameter(params)

    if (operation?.isCached.isFalse) builder.doNotCacheResponse()
    operation?.expireMinutes.notNull {
        builder.setMaxStaleCacheControl(it * Minute, TimeUnit.MILLISECONDS)
    }
    if (operation?.isRefresh.isTrue) builder.responseOnlyFromNetwork
    else if (!application.isNetworkConnected && operation?.isCached.isTrue
        || operation?.isJustUseCache.isTrue) {
        builder.responseOnlyIfCached
    }

    builder.build().apply {
        CSLog.logInfo("get $url")
        getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
    }
}

fun CSOkHttpClient.post(service: String, params: Map<String, String> = emptyMap()) =
    post(service, CSServerData(), params)

fun <ResponseData : CSServerData> CSOkHttpClient.post(
    service: String, responseData: ResponseData, params: Map<String, String>
) = CSProcess("$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addBodyParameter(params).build()
    CSLog.logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun CSOkHttpClient.post(url: String, data: CSJsonData) = post(url, data, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(
    url: String, data: CSJsonData, responseData: ResponseData
) = CSProcess("${this.url}/$url", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONObjectBody(data.toJsonObject()).build()
    CSLog.logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}