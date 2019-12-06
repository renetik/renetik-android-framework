package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import renetik.android.base.application
import renetik.android.client.request.CSOperation
import renetik.android.client.request.CSProcess
import renetik.android.client.request.CSServerData
import renetik.android.extensions.isNetworkConnected
import renetik.android.java.common.CSConstants
import renetik.android.java.extensions.collections.map
import renetik.android.java.extensions.notNull
import renetik.android.java.extensions.primitives.isFalse
import renetik.android.java.extensions.primitives.isTrue
import renetik.android.json.data.CSJsonData
import renetik.android.json.data.toJsonObject
import renetik.android.logging.CSLog
import java.io.File
import java.util.concurrent.TimeUnit

fun <ServerDataType : CSServerData> CSOkHttpClient.upload(
    action: String, file: File, data: ServerDataType
) = CSProcess("$url/$action", data).also { response ->
    val request = AndroidNetworking.upload(response.url).addMultipartFile("file", file).build()
    CSLog.logInfo("upload ${request.url} $file")
    request.setUploadProgressListener { uploaded, total ->
        response.progress = total / uploaded
    }.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
}

fun CSOkHttpClient.get(url: String, params: Map<String, String> = map()) = get(null, url, params)

fun CSOkHttpClient.get(
    operation: CSOperation<CSServerData>? = null, url: String,
    params: Map<String, String> = map()
) = get(operation, url, CSServerData(), params)

fun <ServerDataType : CSServerData> CSOkHttpClient.get(
    operation: CSOperation<*>?, action: String, data: ServerDataType,
    params: Map<String, String> = map()
) = CSProcess("$url/$action", data).also { response ->
    val builder = AndroidNetworking.get(response.url).addQueryParameter(params)

    if (operation?.isCached.isFalse) builder.doNotCacheResponse()

    operation?.expireMinutes.notNull { minutes ->
        builder.setMaxStaleCacheControl(minutes * CSConstants.MINUTE, TimeUnit.MILLISECONDS)
    }

    if (operation?.isRefresh.isTrue) builder.responseOnlyFromNetwork
    else if (!application.isNetworkConnected && operation?.isCached.isTrue
        || operation?.isJustUseCache.isTrue) {
        builder.responseOnlyIfCached
    }

    builder.build().apply {
        CSLog.logInfo("get $url")
        getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
    }
}

fun CSOkHttpClient.post(url: String, params: Map<String, String> = map()) =
    post(url, params, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(
    action: String, params: Map<String, String>, responseData: ResponseData
) = CSProcess("$url/$action", responseData).also { response ->
    val request = AndroidNetworking.post(response.url).addBodyParameter(params).build()
    CSLog.logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
}

fun CSOkHttpClient.post(url: String, data: CSJsonData) = post(url, data, CSServerData())

fun <ResponseData : CSServerData> CSOkHttpClient.post(
    url: String, data: CSJsonData, responseData: ResponseData
) = CSProcess("${this.url}/$url", responseData).also { response ->
    val request = AndroidNetworking.post(response.url).addJSONObjectBody(data.toJsonObject()).build()
    CSLog.logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, response))
}