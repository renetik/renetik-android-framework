package renetik.android.client.okhttp3

import com.androidnetworking.AndroidNetworking
import org.json.JSONObject
import org.json.JSONTokener
import renetik.android.client.request.CSHttpProcess
import renetik.android.client.request.CSHttpResponseData
import renetik.android.client.request.CSOperation
import renetik.android.content.isNetworkConnected
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.lang.CSTimeConstants.Minute
import renetik.android.java.extensions.notNull
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.data.toJsonObject
import renetik.android.framework.json.toJSONArray
import renetik.android.framework.json.toJSONObject
import renetik.android.framework.json.toJsonString
import renetik.android.framework.logging.CSLog.logInfo
import renetik.android.primitives.isFalse
import renetik.android.primitives.isTrue
import java.io.File
import java.util.concurrent.TimeUnit

fun <ServerDataType : CSHttpResponseData> CSOkHttpClient.upload(
    service: String,
    file: File,
    data: ServerDataType
) = CSHttpProcess("$url/$service", data).also { process ->
    val request = AndroidNetworking.upload(process.url).addMultipartFile("file", file).build()
    logInfo("upload ${request.url} $file")
    request.setUploadProgressListener { uploaded, total ->
        process.progress = total / uploaded
    }.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ServerDataType : CSHttpResponseData> CSOkHttpClient.get(
    url: String, data: ServerDataType, params: Map<String, String> = emptyMap()) =
    get(null, url, data, params)

fun <ServerDataType : CSHttpResponseData> CSOkHttpClient.get(
    operation: CSOperation<*>?, service: String, data: ServerDataType,
    params: Map<String, String> = emptyMap()
) = CSHttpProcess("$url/$service", data).also { process ->
    val builder = AndroidNetworking.get(process.url!!).addQueryParameter(params)

    if (operation?.isCached.isFalse) builder.doNotCacheResponse()
    operation?.expireMinutes.notNull {
        builder.setMaxStaleCacheControl(it * Minute, TimeUnit.MILLISECONDS)
    }
    if (operation?.isRefresh.isTrue) builder.responseOnlyFromNetwork
    else if (!application.isNetworkConnected && operation?.isCached.isTrue
        || operation?.isJustUseCache.isTrue
    ) {
        builder.responseOnlyIfCached
    }

    builder.build().apply {
        logInfo("get $url")
        getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
    }
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    service: String, responseData: ResponseData, params: Map<String, String>
) = CSHttpProcess("$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addBodyParameter(params).build()
    logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJson(
    service: String, responseData: ResponseData, data: Map<String, *>
) = post(service, responseData, data.toJSONObject())

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJsonObject(
    service: String, responseData: ResponseData, data: String
) = post(service, responseData, JSONTokener(data).nextValue() as JSONObject)

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    service: String, responseData: ResponseData, data: JSONObject
) = CSHttpProcess("$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONObjectBody(data).build()
    logInfo("post:${request.url} json:${data.toJsonString(true)}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.postJson(
    service: String, responseData: ResponseData, data: List<*>
) = CSHttpProcess("$url/$service", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONArrayBody(data.toJSONArray()).build()
    logInfo("post:${request.url} json:${data.toJsonString(true)}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}

fun <ResponseData : CSHttpResponseData> CSOkHttpClient.post(
    url: String, data: CSJsonMapStore, responseData: ResponseData
) = CSHttpProcess("${this.url}/$url", responseData).also { process ->
    val request = AndroidNetworking.post(process.url).addJSONObjectBody(data.toJsonObject()).build()
    logInfo("post ${request.url}")
    request.getAsOkHttpResponseAndString(CSOkHttpResponseListener(client, process))
}